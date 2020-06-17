package com.linka39.code07.service;

import com.linka39.code07.entity.Link;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 友情链接Service接口
 */
public interface LinkService {
    /**
     * 查询所有友情链接
     * @param direction
     * @param properties
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<Link> listAll(Direction direction, String... properties);
    /**
     * 分页查询友情链接信息
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<Link> list(Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 分页查询总记录数
     * @return
     */
    public Long getTotal();

    /**
     * 根据id获取内容
     * @param id
     * @return
     */
    public Link get(Integer id);

    /**
     * 保存当前资源
     * @return
     */
    public void save(Link link);
    /**
     * 根据id删除资源
     * @return
     */
    public void delete(Integer id);
}

