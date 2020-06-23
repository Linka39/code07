package com.linka39.code07.controller;

import com.linka39.code07.entity.Article;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页url控制层
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    /**
     *根目录请求
     * @return
     */
    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request){
        //通过session来获取资源类型
        request.getSession().setAttribute("tMenu","t_0");
        Article s_article = new Article();
        s_article.setState(2); //设置选取审核通过的帖子
        List<Article> indexArticleList = articleService.list(s_article,1,20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);

        s_article.setHot(true);//设置选取热门的帖子
        List<Article> indexHotArticleList = articleService.list(s_article,1,43, Sort.Direction.DESC,"publishDate");
        ModelAndView mav = new ModelAndView();
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.addObject("title","首页");
        mav.addObject("pageCode", PageUtil.genPagination("/article/list",total,1,20,""));

        mav.addObject("articleList",indexArticleList);
        mav.addObject("hotArticleList",indexHotArticleList);
        mav.setViewName("index");
        return mav;
    }
    @RequestMapping("/admin")
    public String toAdmin(){
        return "adminLogin.html";
    }
}
