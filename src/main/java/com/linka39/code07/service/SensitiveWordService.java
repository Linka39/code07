package com.linka39.code07.service;


import com.linka39.code07.entity.SensitiveWord;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 历史敏感文章Service接口
 */
public interface SensitiveWordService {
    public void save(SensitiveWord sensitiveWord);

    public List<SensitiveWord> list(SensitiveWord s_sensitiveWord, Integer page, Integer pageSize, Direction direction, String...properties );

    public Long getTotal(SensitiveWord s_sensitiveWord);

    public void delete(Integer id);

    public List<String> getAllSensitiveWord();

    public SensitiveWord find(Integer id);


}

