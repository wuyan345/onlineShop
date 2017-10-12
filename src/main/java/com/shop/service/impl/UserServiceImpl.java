package com.shop.service.impl;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.LevelMapper;
import com.shop.dao.RoleMapper;
import com.shop.dao.UserMapper;
import com.shop.pojo.Level;
import com.shop.pojo.Role;
import com.shop.pojo.User;
import com.shop.service.IUserService;
import com.shop.utils.MD5Util;
import com.shop.utils.RedisUtil;

@Service("iUserService")
@Transactional()
public class UserServiceImpl implements IUserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private LevelMapper levelMapper;
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public Message register(User user) {
		// 注册时用户名、密码必填，其余选填
		if(!checkValid(Const.USERNAME, user.getUsername()))
			return Message.errorMsg("用户名格式错误");
		if(!checkValid(Const.PASSWORD, user.getPassword()))
			return Message.errorMsg("密码格式错误");
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		try{
			int count = userMapper.insert(user);
			if(count <= 0)
				return Message.errorMsg("注册失败");
		}catch (DuplicateKeyException e) {
			logger.error("注册的用户名{}在数据库重名", user.getUsername());
			return Message.errorMsg("用户名已被注册");
		}
		Level level = new Level();
		Role role = new Role();
		level.setUserId(user.getId());
		level.setLevel(1);
		level.setExp(0);
		role.setRoleNo(Const.NORMAL_USER);
		role.setUserId(user.getId());
		levelMapper.insert(level);
		roleMapper.insert(role);
		return Message.successMsg("注册成功");
	}

	private boolean checkValid(String type, String value){
		if(type.equals(Const.USERNAME)){
			if(StringUtils.isBlank(value))
				return false;
			// 检测用户名格式
			if(formatCheck(value, Const.FormatValid.USERNAME))
				return true;
			return false;
		}
		if(type.equals(Const.PASSWORD)){
			if(StringUtils.isBlank(value))
				return false;
			// 检测密码格式
			if(formatCheck(value, Const.FormatValid.PASSWORD))
				return true;
			return false;
		}
		if(type.equals(Const.PHONE)){
			if(StringUtils.isBlank(value))
				return false;
			//todo 检测手机号格式
			return true;
		}
		if(type.equals(Const.EMAIL)){
			if(StringUtils.isBlank(value))
				return false;
			//todo 检测邮箱格式
			if(formatCheck(value, Const.FormatValid.EMAIL))
				return true;
			return false;
		}
		// type不正确
		return false;
	}
	
	private boolean formatCheck(String str, String type){
		Pattern pattern = Pattern.compile(type);
		Matcher matcher = pattern.matcher(str);
//		System.out.println(matcher.matches());
		return matcher.matches();
	}
	
	@Override
	public Message login(String username, String password) {
		password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.checkLogin(username, password);
		if(user == null)
			return Message.errorMsg("登录失败，用户名不存在或密码不正确");
		user.setPassword(StringUtils.EMPTY);
		user.setQuestion(StringUtils.EMPTY);
		user.setAnswer(StringUtils.EMPTY);
		return Message.successData(user);
	}

	// 先检查用户名是否存在，再检查有没有设置问题
	@Override
	public Message<String> getUsername(String username) {
		User user = userMapper.selectByUsername(username);
		if(user == null)
			return Message.errorMsg("用户名不存在");
		if(StringUtils.isBlank(user.getQuestion()))
			return Message.errorMsg("该用户的问题是空的");
		return Message.successData(user.getQuestion());
	}

	@Override
	public Message getAnswer(String username, String answer) {
		User user = userMapper.checkAnswer(username, answer);
		if(user == null)
			return Message.errorMsg("答案错误");
		String uuid = UUID.randomUUID().toString();
		//todo uuid有效期30分钟
		RedisUtil.setKey("findPwd_" + username, uuid);
		return Message.successData(uuid);
	}

	@Override
	public Message setNewPwd(String username, String password, String uuid) {
		String token = RedisUtil.getKey("findPwd_" + username);
		if(token == null)
			return Message.errorMsg("需要token");
		if(!token.equals(uuid))
			return Message.errorMsg("token错误");
		int count = userMapper.updatePasswordByUsername(username, password);
		if(count <= 0)
			return Message.errorMsg("修改密码失败");
		RedisUtil.delKey("findPwd_" + username);
		return Message.successMsg("修改密码成功");
	}

	@Override
	public Message editInfo(User user) {
		//todo 如果有填写邮箱，检查是否有重复
		//todo 问题与答案不能只写一个
//		if(user.getQuestion() != null){
//			if("".equals(user.getQuestion().trim())){
//				
//			}
//		}
//		if ((StringUtils.isNotBlank(user.getQuestion()) && StringUtils.isBlank(user.getAnswer()))
//				|| (StringUtils.isBlank(user.getQuestion()) && StringUtils.isNotBlank(user.getAnswer()))) {
//			return Message.errorMsg("问题与答案不能只写一个");
//		}
		int count = userMapper.updateByPrimaryKeySelective(user);
		if(count <= 0)
			return Message.errorMsg("更新个人资料失败");
		return Message.successMsg("更新个人资料成功");
	}

	@Override
	public Message getAllInfo(User user) {
		User user2 = userMapper.selectByPrimaryKey(user.getId());
		if(user2 == null)
			return Message.errorMsg("用户信息获取失败");
		user2.setPassword(StringUtils.EMPTY);
		return Message.successData(user2);
	}

}
