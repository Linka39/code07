package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.User;
import com.linka39.code07.repository.UserRepository;
import com.linka39.code07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户Service实现类
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByEmail(String Email) {
        return userRepository.findByEmail(Email);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }
}
