package com.shop.dao;

import org.apache.ibatis.annotations.Param;

import com.shop.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectByUsername(String username);
    
    int deleteByUsername(String username);
    
    User checkLogin(@Param("username")String username, @Param("password")String password);
    
    int checkUsername(String username);
    
    User checkAnswer(@Param("username")String username, @Param("answer")String answer);
    
    int updatePasswordByUsername(@Param("username")String username, @Param("password")String password);
    
}