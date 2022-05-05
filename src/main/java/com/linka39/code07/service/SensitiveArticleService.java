package com.linka39.code07.service;

import com.linka39.code07.entity.SensitiveArticle;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 历史敏感文章Service接口
 */
public interface SensitiveArticleService {
    public void save(SensitiveArticle sensitiveArticle);

    public List<SensitiveArticle> list(SensitiveArticle s_sensitiveArticle, Integer page, Integer pageSize, Direction direction, String...properties );

    public Long getTotal(SensitiveArticle s_sensitiveArticle);

    public void delete(Integer id);

    public SensitiveArticle find(Integer id);

    public SensitiveArticle findByArticleId(Integer id);

}

