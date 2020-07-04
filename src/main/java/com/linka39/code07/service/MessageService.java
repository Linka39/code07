package com.linka39.code07.service;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.Message;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 友情链接Service接口
 */
public interface MessageService {
    //查询该用户的消息总数
    public Integer getCountByUserId(Integer userId);

    public void save(Message message);
}

