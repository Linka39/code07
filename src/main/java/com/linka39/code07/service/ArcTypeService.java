package com.linka39.code07.service;

import com.linka39.code07.entity.ArcType;

import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 资源类别Service接口
 */
public interface ArcTypeService {
    /**
     * 查询所有资源类别
     * @param direction
     * @param properties
     * @return
     */
    //可变长参数，这个位置可以传入任意个该类型参数,或者传入该类型的数组
    public List<ArcType> listAll(Direction direction,String...properties );
}

