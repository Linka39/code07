package com.linka39.code07.service.impl;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Link;
import com.linka39.code07.repository.ArcTypeRepository;
import com.linka39.code07.repository.LinkRepository;
import com.linka39.code07.service.ArcTypeService;
import com.linka39.code07.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源类别Service实现类
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private LinkRepository linkRepository;

    @Override
    public List<Link> listAll(Sort.Direction direction, String... properties) {
        Sort sort=new Sort(direction,properties);
        return linkRepository.findAll(sort);
    }
}
