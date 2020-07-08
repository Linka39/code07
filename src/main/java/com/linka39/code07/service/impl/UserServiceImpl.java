package com.linka39.code07.service.impl;

import com.linka39.code07.util.StringUtil;
import org.springframework.data.domain.PageRequest;
import com.linka39.code07.entity.User;
import com.linka39.code07.repository.UserRepository;
import com.linka39.code07.service.UserService;
import com.sun.javafx.scene.traversal.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Override
    public List<User> list(User s_user, Integer page, Integer pageSize, Sort.Direction direction, String...properties) {
        Pageable pageable=PageRequest.of(page-1,pageSize,direction,properties);
        Page<User> userPage= userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_user!=null){
                    if(StringUtil.isNotEmpty(s_user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"),"%"+s_user.getUserName().trim()+"%"));
                    }
                    if(StringUtil.isNotEmpty(s_user.getEmail())){
                        //进行sql表达式编辑
                        predicate.getExpressions().add(cb.like(root.get("email"),"%"+s_user.getEmail()+"%"));
                    }
                }
                return predicate;
            }
        },pageable);
        return userPage.getContent();
    }

    @Override
    public void updateAllSignInfo() {
        userRepository.updateAllSignInfo();
    }

    @Override
    public Long getTotal(User s_user) {
        Long total=userRepository.count(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_user!=null){
                    if(StringUtil.isNotEmpty(s_user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"),"%"+s_user.getUserName().trim()+"%"));
                    }
                    if(StringUtil.isNotEmpty(s_user.getEmail())){
                        //进行sql表达式编辑
                        predicate.getExpressions().add(cb.like(root.get("email"),"%"+s_user.getEmail()+"%"));
                    }
                }
                return predicate;
            }
        });
        return total;
    }
}
