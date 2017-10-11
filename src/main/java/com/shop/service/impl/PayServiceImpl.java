package com.shop.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.OrderItemMapper;
import com.shop.dao.OrderMapper;
import com.shop.dao.PayInfoMapper;
import com.shop.pojo.Order;
import com.shop.pojo.OrderItem;
import com.shop.pojo.PayInfo;
import com.shop.service.IPayService;
import com.shop.utils.BigDecimalUtil;
import com.shop.utils.DateTimeUtil;
import com.shop.utils.FTPUtil;

@Service("iPayService")
@Transactional
public class PayServiceImpl implements IPayService {

	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private PayInfoMapper payInfoMapper;
	
	// 支付宝当面付2.0服务
    private static AlipayTradeService   tradeService;
    
	static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }
	
	@Override
	public Message pay(Integer orderId, Integer userId, String path) {
		if(orderId == null)
			return Message.errorMsg("订单id未填写");
		Order order = orderMapper.selectByOrderIdUserId(orderId, userId);
		if(order == null)
			return Message.errorMsg("该订单不存在");
		if(order.getStatus() != Const.OrderStatus.NOT_PAY)
			return Message.errorMsg("该订单已付款");
		
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuffer().append("shop二维码支付，订单号").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = BigDecimalUtil.add(order.getPayment().doubleValue(), order.getPostage().doubleValue()).toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuffer().append("订单号").append(outTradeNo)
        		.append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
//        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);
        
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        for (OrderItem orderItem : orderItemList) {
			GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getGoodsId().toString(), orderItem.getName(), 
															BigDecimalUtil.mul(orderItem.getPrice().doubleValue(), new Double(100).doubleValue()).longValue(), 
															orderItem.getQuantity());
			goodsDetailList.add(goodsDetail);
		}

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl("http://vdd8r7.natappfree.cc/shop/pay/alipayCallback")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);


        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                this.dumpResponse(response);

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrName = String.format("qr-%s.png", response.getOutTradeNo());
                logger.info("qrPath:" + qrPath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File file = new File(path, qrName);
                Message message = FTPUtil.upload(file);
                return message;
//                break;

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return Message.errorMsg("支付宝预下单失败");
//                break;

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return Message.errorMsg("系统异常，预下单状态未知!!!");
//                break;

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return Message.errorMsg("不支持的交易状态，交易返回异常!!!");
//                break;
        }
	}

	// 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    /**
     * 该方法实现单个子订单退款
     */
	@Override
	public Message refund(Integer orderId, Integer orderItemId) {
		if(orderId == null)
			return Message.errorMsg("订单id未填写");
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if(order == null)
			return Message.errorMsg("该订单不存在");
		OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderItemId);
		if(orderItem == null)
			return Message.errorMsg("该子订单不存在");
		if(order.getStatus() != Const.OrderStatus.REFUND)
			return Message.errorMsg("该订单退款没申请或没批准");
		
		// (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = orderItem.getTotalPrice().toString();

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = orderItem.getId().toString();

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "正常退款，测试用";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
            .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
            .setOutRequestNo(outRequestNo).setStoreId(storeId);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝退款成功: )");
                AlipayTradeRefundResponse response = result.getResponse();
                if("N".equals(response.getFundChange()))
                	return Message.errorMsg("该子订单重复退款，退款失败");
                OrderItem newOrderItem = new OrderItem();
                newOrderItem.setId(orderItemId);
                newOrderItem.setStatus(Const.OrderItemStatus.REFUNDED);
                newOrderItem.setRefundTime(response.getGmtRefundPay());
                int count = orderItemMapper.updateByPrimaryKeySelective(newOrderItem);
                if(count <= 1)
                	return Message.errorMsg("更新子订单失败");
                return Message.success();
//                break;

            case FAILED:
            	logger.error("支付宝退款失败!!!");
            	return Message.errorMsg("支付宝退款失败!!!");
//                break;

            case UNKNOWN:
            	logger.error("系统异常，订单退款状态未知!!!");
            	return Message.errorMsg("系统异常，订单退款状态未知!!!");
//                break;

            default:
            	logger.error("不支持的交易状态，交易返回异常!!!");
            	return Message.errorMsg("不支持的交易状态，交易返回异常!!!");
//                break;
        }
	}
	
    /**
     * 该方法实现订单整体退款
     */
	@Override
	public Message refundForOrder(Integer orderId) {
		if(orderId == null)
			return Message.errorMsg("订单id未填写");
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if(order == null)
			return Message.errorMsg("该订单不存在");
		if(order.getStatus() == Const.OrderStatus.NOT_PAY)
			return Message.errorMsg("该订单没付款，不能退款");
		
		// (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = order.getPayment().toString();

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = order.getId().toString();

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "用户不需要，正常退款";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
            .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
            .setOutRequestNo(outRequestNo).setStoreId(storeId);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝退款成功: )");
                AlipayTradeRefundResponse response = result.getResponse();
//                if("N".equals(response.getFundChange()))
//                	return Message.errorMsg("该订单重复退款，退款失败");
                return Message.successData(response.getGmtRefundPay());
//                break;

            case FAILED:
            	logger.error("支付宝退款失败!!!");
            	return Message.errorMsg("支付宝退款失败!!!");
//                break;

            case UNKNOWN:
            	logger.error("系统异常，订单退款状态未知!!!");
            	return Message.errorMsg("系统异常，订单退款状态未知!!!");
//                break;

            default:
            	logger.error("不支持的交易状态，交易返回异常!!!");
            	return Message.errorMsg("不支持的交易状态，交易返回异常!!!");
//                break;
        }
	}
	
	@Override
	public Message alipayCallback(Map<String, String> map) {
		long orderNo = Long.parseLong(map.get("out_trade_no"));
		String status = map.get("trade_status");
		String tradeNo = map.get("trade_no");
		
		Order order = orderMapper.selectByOrderNo(orderNo);
		if(order == null)
			return Message.errorMsg("没有该订单");
		if("TRADE_SUCCESS".equals(status)){
			if(order.getStatus() != Const.OrderStatus.NOT_PAY)
				return Message.errorMsg("重复通知");
			Order newOrder = new Order();
			newOrder.setId(order.getId());
			newOrder.setStatus(Const.OrderStatus.PAID);
			newOrder.setPaymentTime(DateTimeUtil.strToDate(map.get("gmt_payment")));
			int count = orderMapper.updateByPrimaryKeySelective(newOrder);
			if(count <= 0){
				logger.error("订单修改失败");
				return Message.errorMsg("订单修改失败");
			}
			PayInfo payInfo = new PayInfo();
			payInfo.setOrderNo(orderNo);
			payInfo.setUserId(order.getUserId());
			payInfo.setPlatformNumber(tradeNo);
			payInfo.setPayPlatform(Const.PayInfo.PAY_PLATFORM_ALIPAY);
			payInfo.setPlatformStatus(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS);
			count = payInfoMapper.insert(payInfo);
			if(count <= 0){
				logger.error("支付信息新增失败");
				return Message.errorMsg("支付信息新增失败");
			}
			return Message.success();
		}else if("WAIT_BUYER_PAY".equals(status)){
			logger.info("WAIT_BUYER_PAY: " + tradeNo);
			return Message.success();
		}else if("TRADE_FINISHED".equals(status)){
			return Message.success();
		}else if("TRADE_CLOSED".equals(status)){
			if(order.getStatus() == Const.OrderStatus.NOT_PAY){
				logger.info("orderNo: " + tradeNo + "超时未支付，订单关闭");
				Order newOrder = new Order();
				newOrder.setId(order.getId());
				newOrder.setStatus(Const.OrderStatus.ORDER_CLOSE);
				int count = orderMapper.updateByPrimaryKeySelective(newOrder);
				if(count <= 0){
					logger.error("订单修改失败");
					return Message.errorMsg("订单修改失败");
				}
				return Message.success();
			}else if(order.getStatus() >= Const.OrderStatus.PAID){
				logger.info("orderNo: " + tradeNo + "支付完成后全额退款，订单关闭");
				Order newOrder = new Order();
				newOrder.setId(order.getId());
				// 表明已退款，订单关闭
				newOrder.setStatus(Const.OrderStatus.ORDER_CLOSE);
				int count = orderMapper.updateByPrimaryKeySelective(newOrder);
				if(count <= 0){
					logger.error("订单修改失败");
					return Message.errorMsg("订单修改失败");
				}
				PayInfo payInfo = payInfoMapper.selectByOrderNo(orderNo);
				payInfo.setPlatformStatus(Const.AlipayCallback.TRADE_STATUS_TRADE_CLOSED);
				count = payInfoMapper.updateByPrimaryKeySelective(payInfo);
				if(count <= 0){
					logger.error("支付信息修改失败");
					return Message.errorMsg("支付信息修改失败");
				}
				return Message.success();
			}
		}
		logger.error("alipay回调参数trade_status错误: " + status);
		return Message.errorMsg("alipay回调参数trade_status错误");
	}
    
}
