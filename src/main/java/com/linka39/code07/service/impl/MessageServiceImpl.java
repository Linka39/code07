package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.Message;
import com.linka39.code07.repository.LinkRepository;
import com.linka39.code07.repository.MessageRepository;
import com.linka39.code07.service.LinkService;
import com.linka39.code07.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源类别Service实现类
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Integer getCountByUserId(Integer userId) {
        return messageRepository.getCountByUserId(userId);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }
}
