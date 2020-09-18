package com.linka39.code07.service.impl;

import com.linka39.code07.entity.BgImage;
import com.linka39.code07.repository.BgImageRepository;
import com.linka39.code07.service.BgImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 背景图片Service实现类
 */
@Service("bgImageService")
public class BgImageServiceImpl implements BgImageService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private BgImageRepository bgImageRepository;

    @Override
    public List<BgImage> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        Page<BgImage> imagePage = bgImageRepository.findAll(pageable);
        return imagePage.getContent();
    }

    @Override
    public Long getTotal() {
        return bgImageRepository.count();
    }

    @Override
    public BgImage get(Integer id) {
        return bgImageRepository.findById(id).get();
    }

    @Override
    public void save(BgImage bgImage) {
        bgImageRepository.save(bgImage);
    }


    @Override
    public List<BgImage> listAll(Sort.Direction direction, String... properties) {
        //根据某个属性来排序
        Sort sort = new Sort(direction,properties);
        return bgImageRepository.findAll(sort);
    }
}
