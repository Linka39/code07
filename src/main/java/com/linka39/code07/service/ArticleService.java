package com.linka39.code07.service;

import com.linka39.code07.entity.Article;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 资源Service接口
 */
public interface ArticleService {
    /**
     * 根据条件分页查询资源信息
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<Article> list(Article s_article,Integer page, Integer pageSize,Direction direction,String...properties );

    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal(Article article);

    /**
     * 根据id获取内容
     * @param id
     * @return
     */
    public Article get(Integer id);

    /**
     * 保存当前资源
     * @param article
     * @return
     */
    public void save(Article article);
}

