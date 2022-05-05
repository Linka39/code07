package com.linka39.code07.service;

import com.linka39.code07.entity.Dic;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 字典Service接口
 */
public interface DicService {

    public void save(Dic Dic);

    public List<Dic> list(Dic s_Dic, Integer page, Integer pageSize, Direction direction, String...properties );

    public Long getTotal(Dic s_Dic);

    public void delete(Integer id);

    public Dic find(Integer id);

    public List<Dic> getZdByzddm(String zddm);
    public String getNoteByzddm(String zddm,Integer zdz);

    public List<String> getZdzByzddm(String zddm);


}

