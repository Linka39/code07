package com.linka39.code07.service.impl;

import com.linka39.code07.entity.SensitiveWord;
import com.linka39.code07.repository.SensitiveWordRepository;
import com.linka39.code07.service.DicService;
import com.linka39.code07.service.SensitiveWordService;
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
@Service("sensitiveWordService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class SensitiveWordServiceImpl implements SensitiveWordService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private SensitiveWordRepository sensitiveWordRepository;

    @Override
    public void save(SensitiveWord sensitiveWord) {
        sensitiveWordRepository.save(sensitiveWord);
    }

    @Override
    public List<SensitiveWord> list(SensitiveWord s_sensitiveWord, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<SensitiveWord> dicPage= sensitiveWordRepository.findAll(new Specification<SensitiveWord>() {
            @Override
            public Predicate toPredicate(Root<SensitiveWord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_sensitiveWord!=null){
                    if(s_sensitiveWord.getEmotion()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("emotion"),s_sensitiveWord.getEmotion()));
                    }
                    if(s_sensitiveWord.getWord()!=null){
                        predicate.getExpressions().add(cb.like(root.get("word"),"%"+s_sensitiveWord.getWord().trim()+"%"));
                    }
                }
                return predicate;
            }
        },pageable);
        return dicPage.getContent();
    }

    @Override
    public Long getTotal(SensitiveWord s_sensitiveWord) {
        Long total=sensitiveWordRepository.count(new Specification<SensitiveWord>() {
            @Override
            public Predicate toPredicate(Root<SensitiveWord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if( s_sensitiveWord!=null){
                    if(s_sensitiveWord.getEmotion()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("emotion"),s_sensitiveWord.getEmotion()));
                    }
                    if(s_sensitiveWord.getWord()!=null){
                        predicate.getExpressions().add(cb.like(root.get("word"),"%"+s_sensitiveWord.getWord().trim()+"%"));
                    }
                }
                return predicate;
            }
        });
        return total;
    }

    @Override
    public void delete(Integer id) {
        sensitiveWordRepository.deleteById(id);
    }

    @Override
    public SensitiveWord find(Integer id) {
        return sensitiveWordRepository.findById(id).get();
    }

    @Override
    public List<String> getAllSensitiveWord() {
        return sensitiveWordRepository.getAllSensitiveWord(1);
    }

    @Override
    public List<String[]> getAllSensitiveWordByEmotion(Integer emotion) {
        return sensitiveWordRepository.getAllSensitiveWordByEmotion(emotion);
    }
}
