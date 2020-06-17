package com.linka39.code07.service.impl;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.repository.ArcTypeRepository;
import com.linka39.code07.service.ArcTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源类别Service实现类
 */
@Service("arcTypeService")
public class ArcTypeServiceImpl implements ArcTypeService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private ArcTypeRepository arcTypeRepository;

    @Override
    public List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<ArcType> pageArctype = arcTypeRepository.findAll(pageable);
        return pageArctype.getContent();
    }

    @Override
    public Long getTotal() {
        return arcTypeRepository.count();
    }

    @Override
    public ArcType get(Integer id) {
        return arcTypeRepository.findById(id).get();
    }

    @Override
    public void save(ArcType arcType) {
        arcTypeRepository.save(arcType);
    }

    @Override
    public void delete(Integer id) {
        arcTypeRepository.deleteById(id);
    }

    @Override
    public List<ArcType> listAll(Sort.Direction direction, String... properties) {
        //根据某个属性来排序
        Sort sort = new Sort(direction,properties);
        return arcTypeRepository.findAll(sort);
    }
}
