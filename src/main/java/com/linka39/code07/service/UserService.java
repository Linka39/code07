package com.linka39.code07.service;

import com.linka39.code07.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
   public void save(User user);
   public User findByUserName(String userName);
   public User findByEmail(String Email);
   public User findById(Integer id);
}

