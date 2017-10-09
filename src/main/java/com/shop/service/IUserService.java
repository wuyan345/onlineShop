package com.shop.service;

import com.shop.common.Message;
import com.shop.pojo.User;

public interface IUserService {

	Message register(User user);
	
	Message login(String username, String password);
	
	Message<String> getUsername(String username);
	
	Message getAnswer(String username, String answer);
	
	Message setNewPwd(String username, String password, String uuid);
	
	Message editInfo(User user);
	
	Message getAllInfo(User user);
}
