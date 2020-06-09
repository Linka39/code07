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
}

