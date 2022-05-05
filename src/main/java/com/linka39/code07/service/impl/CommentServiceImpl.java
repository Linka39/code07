package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.repository.CommentRepository;
import com.linka39.code07.service.CommentService;
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

/**
 * 评论Service实现类
 */
@Service("commentService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class CommentServiceImpl implements CommentService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> list(Comment s_comment, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<Comment> commentPage= commentRepository.findAll(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_comment!=null){
                    if(s_comment.getState()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("state"),s_comment.getState()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"),s_comment.getArticle().getId()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"),s_comment.getArticle().getId()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getUser()!=null&&s_comment.getArticle().getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("user").get("id"),s_comment.getArticle().getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return commentPage.getContent();
    }

    @Override
    public Long getTotal(Comment s_comment) {
        Long total=commentRepository.count(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_comment!=null){
                    if(s_comment.getState()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("state"),s_comment.getState()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"),s_comment.getArticle().getId()));
                    }
                }
                return predicate;
            }
        });
        return total;
    }

    @Override
    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment find(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void deleteByArticleId(Integer id) {
        commentRepository.deleteByArticleId(id);
    }
}
