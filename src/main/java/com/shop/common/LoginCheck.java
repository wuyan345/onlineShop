package com.shop.common;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.dao.RoleMapper;
import com.shop.pojo.Role;
import com.shop.pojo.User;

@Component("loginCheck")
public class LoginCheck {

	@Autowired
	private RoleMapper roleMapper;
	
	/**
	 * 没登录:	false<br>
	 * 权限不对:	false
	 * @param session
	 * @param roleNo
	 * @return
	 */
	public boolean check(HttpSession session, int roleNo){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null)
			return false;	// 未登录
		Role role = roleMapper.selectByUserId(user.getId());
		if(role.getRoleNo() != roleNo)
			return false;	// 权限不对
		return true;
	}

}
