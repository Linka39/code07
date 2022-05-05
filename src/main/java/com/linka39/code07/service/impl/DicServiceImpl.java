package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Dic;
import com.linka39.code07.repository.DicRepository;
import com.linka39.code07.repository.DicRepository;
import com.linka39.code07.service.DicService;
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
@Service("dicService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class DicServiceImpl implements DicService {

    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private DicRepository dicRepository;

    @Override
    public void save(Dic dic) {
        dicRepository.save(dic);
    }

    @Override
    public List<Dic> list(Dic s_dic, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<Dic> dicPage= dicRepository.findAll(new Specification<Dic>() {
            @Override
            public Predicate toPredicate(Root<Dic> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_dic!=null){
                    if(s_dic.getZddm()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("zddm"),s_dic.getZddm()));
                    }
                    if(s_dic.getZdz()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("zdz"),s_dic.getZdz()));
                    }
                }
                return predicate;
            }
        },pageable);
        return dicPage.getContent();
    }

    @Override
    public Long getTotal(Dic s_dic) {
        Long total=dicRepository.count(new Specification<Dic>() {
            @Override
            public Predicate toPredicate(Root<Dic> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_dic!=null){
                    if(s_dic.getZddm()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("zddm"),s_dic.getZddm()));
                    }
                    if(s_dic.getZdz()!=null){
                        predicate.getExpressions().add(cb.notEqual(root.get("zdz"),s_dic.getZdz()));
                    }
                }
                return predicate;
            }
        });
        return total;
    }

    @Override
    public void delete(Integer id) {
        dicRepository.deleteById(id);
    }

    @Override
    public List<Dic> getZdByzddm(String zddm) {
        return dicRepository.getZdByzddm(zddm);
    }

    @Override
    public String getNoteByzddm(String zddm,Integer zdz) {
        return dicRepository.getNoteByzddm(zddm,zdz);
    }

    @Override
    public Dic find(Integer id) {
        return dicRepository.findById(id).get();
    }

    @Override
    public List<String> getZdzByzddm(String zddm) {
        return dicRepository.getZdzByzddm(zddm);
    }
}
