package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 用户页面跳转控制器
 */

@Controller
public class IndexUserController {
    @Autowired
    private ArticleService articleService;
    /**
     * 跳转用户中心页面
     * @return
     */
    @RequestMapping("/toUserCenterPage")
    public ModelAndView toUserCenterPage(HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        Article s_article = new Article();
        s_article.setUser(user);
        s_article.setUseful(false);
        Long total = articleService.getTotal(s_article);
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","用户中心页面");
        mav.addObject("unUserfulCount",total);
        mav.setViewName("user/userCenter");
        return mav;
    }
}
