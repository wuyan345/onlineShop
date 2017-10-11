package com.shop.controller.portal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.pojo.User;
import com.shop.service.IPayService;

@Controller
@RequestMapping("/pay")
public class PayController {

	private static final Logger logger = LoggerFactory.getLogger(PayController.class);
	@Autowired
	private LoginCheck loginCheck;
	@Autowired
	private IPayService iPayService;
	
	
	@RequestMapping("/alipay")
	@ResponseBody
	public Message pay(Integer orderId, HttpSession session, HttpServletRequest request){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		String path = request.getSession().getServletContext().getRealPath("WEB-INF/upload");
		return iPayService.pay(orderId, user.getId(), path);
	}
	
	@RequestMapping("/alipayRefund")
	@ResponseBody
	public Message refund(Integer orderId, HttpSession session, HttpServletRequest request){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录或无权限");
		return null;
	}
	
	@RequestMapping("/alipayCallback")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request){
		Map<String, String> map = new HashMap<>();
		Map<String, String[]> paramMap = request.getParameterMap();
		Iterator<String> iter = paramMap.keySet().iterator();
		for( ; iter.hasNext(); ){
			String paramName = iter.next();
			map.put(paramName, request.getParameter(paramName));
//			logger.info(paramName + ": " + request.getParameter(paramName));
		}
		logger.info("alipay异步通知, out_trade_no: " + map.get("out_trade_no"));
		logger.info("alipay异步通知, trade_status: " + map.get("trade_status"));
		map.remove("sign_type");
		try {
			boolean alipayRSACheckV2 = AlipaySignature.rsaCheckV2(map, Configs.getAlipayPublicKey(), "utf-8", AlipayConstants.SIGN_TYPE_RSA2);
			if(!alipayRSACheckV2)
				return Message.errorMsg("sign验证失败");
		} catch (AlipayApiException e) {
			logger.error("支付宝回调验证sign失败", e);
			return Message.errorMsg("sign验证失败");
		}
		Message message = iPayService.alipayCallback(map);
		if(message.isSuccess())
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
}
