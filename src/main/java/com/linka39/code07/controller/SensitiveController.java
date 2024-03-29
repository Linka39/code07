package com.linka39.code07.controller;

import com.alibaba.fastjson.JSONObject;
import com.linka39.code07.entity.User;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 敏感词控制层
 */
@Controller
@RequestMapping("/Sensitive")
public class SensitiveController {

    @Autowired
    private SensitiveService sensitiveService;

    /**
     * 是否包含敏感词
     * @return
     * @throws Exception
     */
    @RequestMapping("/sensitiveWordExist")
    @ResponseBody
    public String sensitiveWord(String text)throws Exception{
        JSONObject json = sensitiveService.sensitiveWordExist(text);
        return json.toString();
    }

    /**
     * 判断敏感性文章
     * @return
     * @throws Exception
     */
    @RequestMapping("/sensitiveArticleExist")
    @ResponseBody
    public String sensitiveArticle(String text, HttpSession session)throws Exception{
        User currentUser = (User) session.getAttribute("currentUser");
        JSONObject json = sensitiveService.getArticleSensitiveWord(text);
        if(json.getIntValue("code")>0){
            JSONObject userJson = sensitiveService.getUserAttr(currentUser.getId());
            json.putAll(userJson);
            String formatStr = sensitiveService.formatUserAttr(json);
            json.put("level",sensitiveService.getJctreeAttr(formatStr));
            json.put("accessLevel",SensitiveWordFilter.accessLevel);
        }
        return json.toString();
    }

//    /**
//     * 追加指定敏感词
//     *
//     * @param sensitiveWord
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/sensitiveWordWrite")
//    public String sensitiveWordWrite(String sensitiveWord) {
//        JSONObject json = sensitiveService.sensitiveWordWrite(sensitiveWord,null);
//        return json.toString();
//    }
//
//    /**
//     * 删除指定敏感词
//     *
//     * @param sensitiveWord
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/sensitiveWordDelete")
//    public String sensitiveWordDelete(String sensitiveWord) {
//        JSONObject json = sensitiveService.sensitiveWordDelete(sensitiveWord);
//        return json.toString();
//    }
}
