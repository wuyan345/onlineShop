package com.shop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.bo.order.CartGoodsBo;
import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.CartMapper;
import com.shop.dao.GoodsMapper;
import com.shop.dao.OrderItemMapper;
import com.shop.dao.OrderMapper;
import com.shop.dao.ShippingMapper;
import com.shop.pojo.Cart;
import com.shop.pojo.Goods;
import com.shop.pojo.Order;
import com.shop.pojo.OrderItem;
import com.shop.pojo.Shipping;
import com.shop.pojo.User;
import com.shop.service.IOrderService;
import com.shop.service.IPayService;
import com.shop.utils.BigDecimalUtil;
import com.shop.utils.DateTimeUtil;
import com.shop.vo.OrderVo;
import com.shop.vo.PreOrderVo;

@Service("iOrderService")
@Transactional
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private ShippingMapper shippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private IPayService iPayService;
	
	@Override
	public Message preorder(User user) {
		// cart、goods、shipping合并为一个类，让数据库返回到这个类
		List<CartGoodsBo> cartGoodsBoList = cartMapper.querySelectedCartGoods(user.getId());
		if(cartGoodsBoList.isEmpty())
			return Message.errorMsg("没有已勾选的购物车");
		List<Shipping> shippingList = shippingMapper.selectByUserId(user.getId());
		PreOrderVo preOrderVo = new PreOrderVo();
		preOrderVo.setCartGoodsBoList(cartGoodsBoList);
		preOrderVo.setShippingList(shippingList);
		return Message.successData(preOrderVo);
	}

	@Override
	public Message generateOrder(Integer shippingId, User user) {
		// 检测shippingId是否存在
		if(shippingId == null)
			return Message.errorMsg("缺少收货人信息");
		// 检测shippingId是否属于该用户的
		int count = shippingMapper.checkShippingId(shippingId, user.getId());
		if(count <= 0)
			return Message.errorMsg("收货人信息不属于该用户");
		List<CartGoodsBo> cartGoodsBoList = cartMapper.querySelectedCartGoods(user.getId());
		if(cartGoodsBoList.size() == 0)
			return Message.errorMsg("无法生成订单，因没有已勾选的购物车");
		// 插入新订单
		Order order = new Order();
		order.setUserId(user.getId());
		order.setShippingId(shippingId);
		order.setOrderNo(this.generateOrderNo());
		order.setPayment(this.calculateTotalPrice(cartGoodsBoList));
		order.setPaymentType(Const.PaymentType.PAY_ONLINE);
		order.setPostage(new BigDecimal("0"));
		order.setStatus(Const.OrderStatus.NOT_PAY);
		count = orderMapper.insert(order);
		if(count <= 0)
			return Message.errorMsg("生成订单失败");
		// 为每个勾选购物车生成订单子表
		List<OrderItem> orderItemList = new ArrayList<>();
		for(CartGoodsBo cartGoodsBo : cartGoodsBoList){
			OrderItem orderItem = new OrderItem();
			orderItem.setUserId(user.getId());
			orderItem.setOrderId(order.getId());
			orderItem.setGoodsId(cartGoodsBo.getGoodsId());
			orderItem.setOrderNo(order.getOrderNo());
			orderItem.setQuantity(cartGoodsBo.getQuantity());
			orderItem.setName(cartGoodsBo.getName());
			orderItem.setMainImage(cartGoodsBo.getMainImage());
			orderItem.setDetail(cartGoodsBo.getDetail());
			orderItem.setStatus(Const.OrderItemStatus.NORMAL);
			orderItem.setPrice(cartGoodsBo.getGoodsPrice());
			orderItem.setTotalPrice(BigDecimalUtil.mul(orderItem.getPrice().doubleValue(), 
													orderItem.getQuantity().doubleValue()));
			orderItemList.add(orderItem);
		}
		// 批量insert orderItem
		count = orderItemMapper.batchInsert(orderItemList);
		if(count < orderItemList.size()){
			// 部分没插入，则手动把这个方法整块事务回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Message.errorMsg("生成订单失败");
		}
		//清空购物车
		count = cartMapper.batchDelete(cartGoodsBoList);
		// 批量update减库存
		count = goodsMapper.batchUpdateQuantity(cartGoodsBoList);
		if(count < cartGoodsBoList.size()){
			// 部分库存不足，则手动把这个方法整块事务回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Message.errorMsg("库存不足，生成订单失败");
		}
		return Message.successMsgData("生成订单成功", order);
	}
	
	// 时间戳 + 随机数
	private Long generateOrderNo(){
		long orderNo = System.currentTimeMillis();
		orderNo = orderNo + new Random().nextInt(100);
		return orderNo;
	}
	
	private BigDecimal calculateTotalPrice(List<CartGoodsBo> cartGoodsBoList){
		BigDecimal total = new BigDecimal("0");
		for (CartGoodsBo cartGoodsBo : cartGoodsBoList) {
			BigDecimal price = BigDecimalUtil.mul(cartGoodsBo.getGoodsPrice().doubleValue(), 
												cartGoodsBo.getQuantity().doubleValue());
			total = BigDecimalUtil.add(total.doubleValue(), price.doubleValue());
		}
		return total;
	}

	@Override
	public Message saveOrEditAddress(Shipping shipping, User user) {
		if(shipping.getId() == null){
			shipping.setUserId(user.getId());
			int count = shippingMapper.insert(shipping);
			if(count <= 0)
				return Message.errorMsg("保存地址失败");
			return Message.successMsg("保存地址成功");
		}else {
			int count = shippingMapper.updateByPrimaryKeySelective(shipping);
			if(count <= 0)
				return Message.errorMsg("编辑地址失败");
			return Message.successMsg("编辑地址成功");
		}
	}

	@Override
	public Message listOrder(User user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orderList = orderMapper.selectByUserId(user.getId());
		List<OrderItem> orderItemList = orderItemMapper.batchSelectByOrderList(orderList);
		List<Shipping> shippingList = shippingMapper.batchSelectByOrderList(orderList);
		List<OrderVo> orderVoList = new ArrayList<>();
		for (Order order : orderList) {
			OrderVo orderVo = new OrderVo();
			orderVo.setOrder(order);
			List<OrderItem> orderItemList2 = new ArrayList<>();
			for (OrderItem orderItem : orderItemList) {
				if(order.getId().equals(orderItem.getOrderId())){
					orderItem.setDetail("");
					orderItemList2.add(orderItem);
				}
			}
			for (Shipping shipping : shippingList) {
				if(order.getShippingId().equals(shipping.getId())){
					orderVo.setShipping(shipping);
					break;
				}
			}
			orderVo.setOrderItemList(orderItemList2);
			orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
			orderVoList.add(orderVo);
		}
		PageInfo pageInfo = new PageInfo<>(orderList);
		pageInfo.setList(orderVoList);
		return Message.successData(pageInfo);
	}

	@Override
	public Message cancelOrder(Integer orderId) {
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if(order == null)
			return Message.errorMsg("没有该订单");
		// 发了货就不能取消订单
		if(order.getStatus() >= Const.OrderStatus.SHIPPED)
			return Message.errorMsg("不能取消");
		List<OrderItem> orderItemList = orderItemMapper.selectQuantityByOrderNo(order.getOrderNo());
		Order newOrder = new Order();
		newOrder.setId(orderId);
		newOrder.setStatus(Const.OrderStatus.ORDER_CANCEL);
		orderMapper.updateByPrimaryKeySelective(newOrder);
		// 补回库存
		int count = goodsMapper.batchUpdateQuantityForCancelOrder(orderItemList);
		if(count < orderItemList.size()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Message.errorMsg("取消订单失败");
		}
		// 已付款未发货取消订单还要退款
		if(order.getStatus() == Const.OrderStatus.PAID){
			Message message = iPayService.refundForOrder(orderId);
            newOrder.setId(orderId);
            newOrder.setRefundTime((Date)message.getData());
            count = orderMapper.updateByPrimaryKeySelective(newOrder);
            if(count <= 0)
            	return Message.errorMsg("更新订单失败");
		}
		return Message.successMsg("取消订单成功");
	}

	@Override
	public Message getPaymentStatus(Integer orderId, Integer userId) {
		Order order = orderMapper.selectByOrderIdUserId(orderId, userId);
		if(order == null)
			return Message.errorMsg("没有该订单");
		return Message.successData(order.getStatus());
	}
	
}
