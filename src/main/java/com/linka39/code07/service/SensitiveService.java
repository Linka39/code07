package com.linka39.code07.service;

import com.alibaba.fastjson.JSONObject;
import com.linka39.code07.entity.Message;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 敏感词Service接口
 */
public interface SensitiveService {
    //包含敏感词
    public JSONObject sensitiveWordExist(String text);

    //获取所属敏感词
    public JSONObject getArticleSensitiveWord(String text);

    //计算敏感词所属分类
    public JSONObject sensitiveWordClassify(ArrayList<String> words);
    //获取用户属性
    public JSONObject getUserAttr(Integer userId);

    //提取转换用户属性
    public JSONObject formatUserAttr(Integer userId);

    //
    public JSONObject sensitiveDataWrite(LinkedList<String[]> sxList, int insertMod);

}

