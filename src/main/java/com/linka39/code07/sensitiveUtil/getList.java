package com.linka39.code07.sensitiveUtil;

import com.linka39.code07.service.DicService;
import com.linka39.code07.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("getlist")
public class getList {
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Autowired
    private DicService dicService;


    public List<String> getWordList(){
        List<String> wordList;
        return wordList = sensitiveWordService.getAllSensitiveWord();
    }
}
