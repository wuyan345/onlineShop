package com.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.GoodsMapper;
import com.shop.pojo.Goods;
import com.shop.service.IGoodsService;
import com.shop.vo.GoodsDetailVo;

@Service("iGoodsService")
@Transactional
public class GoodsServiceImpl implements IGoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public Message addGoods(Goods goods) {
		int count = goodsMapper.insert(goods);
		if(count <= 0)
			return Message.errorMsg("添加商品失败");
		return Message.successMsg("添加商品成功");
	}

	@Override
	public Message<PageInfo> list(Integer categoryId, int pageNum, int pageSize) {
		if(categoryId == null)
			return Message.errorMsg("参数错误");
		PageHelper.startPage(pageNum, pageSize);
		List<Goods> goodsList = new ArrayList<>();
		goodsList = goodsMapper.selectByCategoryId(categoryId);
		PageInfo pageInfo = new PageInfo<>(goodsList);
		return Message.successData(pageInfo);
	}

	@Override
	public Message getGoodsDetail(int goodsId) {
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		if(goods == null)
			return Message.errorMsg("没有该商品");
		GoodsDetailVo goodsDetailVo = createGoodsDetailVo(goods);
		return Message.successData(goodsDetailVo);
	}

	private GoodsDetailVo createGoodsDetailVo(Goods goods){
		GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
		goodsDetailVo.setGoods(goods);
		goodsDetailVo.setImgHost(Const.HTTP_IMAGE_PREFIX);
		return goodsDetailVo;
	}
	
	@Override
	public Message<PageInfo> searchGoods(String keyword, int pageNum, int pageSize) {
		if(StringUtils.isBlank(keyword))
			return Message.errorMsg("参数错误");
		keyword = "%" + keyword + "%";
		List<Goods> goodsList = new ArrayList<>();
		PageHelper.startPage(pageNum, pageSize);
		goodsList = goodsMapper.selectByKeyword(keyword);
		PageInfo pageInfo = new PageInfo<>(goodsList);
		return Message.successData(pageInfo);
	}
	
}
