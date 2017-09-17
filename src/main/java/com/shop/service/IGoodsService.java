package com.shop.service;

import com.github.pagehelper.PageInfo;
import com.shop.common.Message;

public interface IGoodsService {

	Message<PageInfo> list(Integer categoryId, int pageNum, int pageSize);
	
	Message getGoodsDetail(int goodsId);
	
	Message<PageInfo> searchGoods(String keyword, int pageNum, int pageSize);
}
