package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.Message;
import com.linka39.code07.repository.LinkRepository;
import com.linka39.code07.repository.MessageRepository;
import com.linka39.code07.service.LinkService;
import com.linka39.code07.service.MessageService;
import com.linka39.code07.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

import java.util.List;

/**
 * 资源类别Service实现类
 */
@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Integer getCountByUserId(Integer userId) {
        return messageRepository.getCountByUserId(userId);
    }

    @Override
    public List<Message> list(Message s_message, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        //根据审核状态来筛选
        Page<Message> pageMessage= messageRepository.findAll(new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_message!=null){
                    if(s_message.getUser()!=null && s_message.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"),s_message.getUser().getId()));
                    }

                }
                return predicate;
            }
        },pageable);
        return pageMessage.getContent();
    }

    @Override
    public Long getTotal(Message s_message) {
        Long count=messageRepository.count(new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_message!=null){
                    if(s_message.getUser()!=null && s_message.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"),s_message.getUser().getId()));
                    }

                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void updateState(Integer userId) {
         messageRepository.updateState(userId);
    }
}
