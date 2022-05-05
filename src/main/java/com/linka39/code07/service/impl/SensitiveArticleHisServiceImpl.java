package com.linka39.code07.service.impl;

import com.linka39.code07.entity.SensitiveArticleHis;
import com.linka39.code07.repository.SensitiveArticleHisRepository;
import com.linka39.code07.service.SensitiveArticleHisService;
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
@Service("sensitiveArticleHisService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class SensitiveArticleHisServiceImpl implements SensitiveArticleHisService {
    @Override
    public List<SensitiveArticleHis> listAll(Sort.Direction direction, String... properties) {
        return null;
    }

    @Override
    public List<SensitiveArticleHis> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        return null;
    }

    @Override
    public Long getTotal() {
        return null;
    }

    @Override
    public SensitiveArticleHis get(Integer id) {
        return null;
    }

    @Override
    public void save(SensitiveArticleHis sensitiveArticleHis) {

    }
}
