package com.linka39.code07.service.impl;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Article;
import com.linka39.code07.repository.ArcTypeRepository;
import com.linka39.code07.repository.ArticleRepository;
import com.linka39.code07.service.ArcTypeService;
import com.linka39.code07.service.ArticleService;
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
import java.util.List;

/**
 * 资源Service实现类
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private ArticleRepository articleRepository;

    @Override
    public List<Article> list(Article s_article, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        //根据审核状态来筛选
        Page<Article> pageArticle= articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_article!=null){
                    if(s_article.getState()!=null){
                        //进行sql表达式编辑
                        predicate.getExpressions().add(cb.equal(root.get("state"),s_article.getState()));
                    }
                    if(s_article.getArcType()!=null && s_article.getArcType().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"),s_article.getArcType().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageArticle.getContent();
    }

    @Override
    public Long getTotal(Article article) {
       Long count = articleRepository.count(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(article!=null){
                    if(article.getState()!=null){
                        //进行sql表达式编辑
                        predicate.getExpressions().add(cb.equal(root.get("state"),article.getState()));
                    }
                    if(article.getArcType()!=null && article.getArcType().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"),article.getArcType().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}