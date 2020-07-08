package com.linka39.code07.service;

import com.linka39.code07.entity.User;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService {
   public void save(User user);
   public User findByUserName(String userName);
   public User findByEmail(String Email);
   public User findById(Integer id);
   public List<User> list(User s_user, Integer page, Integer pageSize, Direction direction, String...properties );
   public Long getTotal(User s_user);
   public void updateAllSignInfo();
}

