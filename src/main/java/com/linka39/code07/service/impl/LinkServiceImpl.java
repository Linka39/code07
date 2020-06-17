package com.linka39.code07.service.impl;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Link;
import com.linka39.code07.repository.ArcTypeRepository;
import com.linka39.code07.repository.LinkRepository;
import com.linka39.code07.service.ArcTypeService;
import com.linka39.code07.service.LinkService;
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
@Service("linkService")
public class LinkServiceImpl implements LinkService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private LinkRepository linkRepository;

    @Override
    public List<Link> listAll(Sort.Direction direction, String... properties) {
        Sort sort=new Sort(direction,properties);
        return linkRepository.findAll(sort);
    }

    @Override
    public List<Link> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable =  PageRequest.of(page-1,pageSize,direction,properties);
        Page<Link> linkPage = linkRepository.findAll(pageable);
        return linkPage.getContent();
    }

    @Override
    public Long getTotal() {
        return linkRepository.count();
    }

    @Override
    public Link get(Integer id) {
        return linkRepository.findById(id).get();
    }

    @Override
    public void save(Link link) {
        linkRepository.save(link);
    }

    @Override
    public void delete(Integer id) {
        linkRepository.deleteById(id);
    }
}
