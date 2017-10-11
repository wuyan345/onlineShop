package com.shop.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.pojo.User;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

	@Autowired 
	private LoginCheck loginCheck;
	
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	@ResponseBody
	public Message getInfo(HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return Message.successData(user);
	}
}
