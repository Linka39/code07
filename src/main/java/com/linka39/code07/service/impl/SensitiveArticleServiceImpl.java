package com.linka39.code07.service.impl;

import com.linka39.code07.entity.SensitiveArticle;
import com.linka39.code07.repository.SensitiveArticleRepository;
import com.linka39.code07.service.SensitiveArticleService;
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
@Service("sensitiveArticleService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class SensitiveArticleServiceImpl implements SensitiveArticleService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private SensitiveArticleRepository censitiveArticleRepository;

    @Override
    public void save(SensitiveArticle censitiveArticle) {
        censitiveArticleRepository.save(censitiveArticle);
    }

    @Override
    public List<SensitiveArticle> list(SensitiveArticle s_censitiveArticle, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<SensitiveArticle> censitiveArticlePage= censitiveArticleRepository.findAll(new Specification<SensitiveArticle>() {
            @Override
            public Predicate toPredicate(Root<SensitiveArticle> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_censitiveArticle!=null){
                    if(s_censitiveArticle.getState()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("state"),s_censitiveArticle.getState()));
                    }
//                    if(s_censitiveArticle.getArticle()!=null&&s_censitiveArticle.getArticle().getId()!=null){
//                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"),s_censitiveArticle.getArticle().getId()));
//                    }
//                    if(s_censitiveArticle.getArticle()!=null&&s_censitiveArticle.getArticle().getUser()!=null&&s_censitiveArticle.getArticle().getUser().getId()!=null){
//                        predicate.getExpressions().add(cb.equal(root.get("article").get("user").get("id"),s_censitiveArticle.getArticle().getUser().getId()));
//                    }
                }
                return predicate;
            }
        },pageable);
        return censitiveArticlePage.getContent();
    }

    @Override
    public Long getTotal(SensitiveArticle s_censitiveArticle) {
        Long total=censitiveArticleRepository.count(new Specification<SensitiveArticle>() {
            @Override
            public Predicate toPredicate(Root<SensitiveArticle> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_censitiveArticle!=null){
                    if(s_censitiveArticle.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"),s_censitiveArticle.getState()));
                    }
//                    if(s_censitiveArticle.getArticle()!=null&&s_censitiveArticle.getArticle().getId()!=null){
//                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"),s_censitiveArticle.getArticle().getId()));
//                    }
                }
                return predicate;
            }
        });
        return total;
    }

    @Override
    public void delete(Integer id) {
        censitiveArticleRepository.deleteById(id);
    }

    @Override
    public SensitiveArticle find(Integer id) {
        return censitiveArticleRepository.findById(id).get();
    }

    @Override
    public SensitiveArticle findByArticleId(Integer id) {

        return censitiveArticleRepository.getSentitiveByAticleId(id);
    }

}
