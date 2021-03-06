package com.shop.common;

public class Const {

	public static final String HTTP_IMAGE_PREFIX = "http://img.zxshopdemo.com/";
	
	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
	
	public static final int NORMAL_USER = 1;
	public static final int MANAGER = 0;
	
	public static final String CURRENT_USER = "currentUser";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String PHONE = "phone";
	public static final String EMAIL = "email";
	
	public interface FormatValid{
		// 字母、数字组合，4-10个字符
		String USERNAME = "^[0-9a-zA-Z]{4,10}$";
		// 字母、数字组合，6-20个字符
		String PASSWORD = "^[0-9a-zA-Z]{6,10}$";
		// xxx@xxx.xxx
		String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$";
	}
	
	public interface Category{
		int NORMAL = 0;
		int ABANDONED = 1;
	}
	
	public interface Cart{
		int SELECTED = 0;
		int UNSELECT = 1;
	}
	
	public interface PaymentType{
		int PAY_ONLINE = 0;
		int CASH_ON_DELIVERY = 1;
	}
	
	public interface OrderStatus{
		int NOT_PAY = 0;
		int PAID = 1;
		int SHIPPED = 2;
		int RECEIVED_GOODS = 3;
		// 由管理员来确认退款
		int REFUND = 4;
		int TRADE_SUCCESS = 5;
		int ORDER_CLOSE = 6;
	}
	
	public interface OrderItemStatus{
		int NORMAL = 0;
		int REFUNDABLE = 1;
		int REFUNDED = 2;
	}
	
	public interface AlipayCallback{
		String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
		String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
		String TRADE_STATUS_TRADE_FINISHED = "TRADE_FINISHED";
		String TRADE_STATUS_TRADE_CLOSED = "TRADE_CLOSED";
		String RESPONSE_SUCCESS = "success";
		String RESPONSE_FAILED = "failed";
	}
	
	public interface PayInfo{
		int PAY_PLATFORM_ALIPAY = 1;
		int PAY_PLATFORM_WECHAT = 2;
	}
}
