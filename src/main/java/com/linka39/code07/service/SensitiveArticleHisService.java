package com.linka39.code07.service;

import com.linka39.code07.entity.SensitiveArticleHis;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 敏感文章Service接口
 */
public interface SensitiveArticleHisService {
    /**
     * 查询所有背景图片
     * @param direction
     * @param properties
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<SensitiveArticleHis> listAll(Direction direction, String... properties);

    /**
     * 根据条件分页查询背景图片信息
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<SensitiveArticleHis> list(Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 根据条件查询总记录数
     * @return
     */
    public Long getTotal();

    /**
     * 根据id获取内容
     * @param id
     * @return
     */
    public SensitiveArticleHis get(Integer id);

    /**
     * 保存当前资源
     * @return
     */
    public void save(SensitiveArticleHis sensitiveArticleHis);
}

