package com.linka39.code07.controller;

import com.linka39.code07.entity.Article;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import sun.java2d.pipe.AATileGenerator;

import java.util.List;

/**
 * 首页url控制层
 */
@Controller
public class IndexContoller {

    @Autowired
    private ArticleService articleService;

    /**
     *根目录请求
     * @return
     */
    @RequestMapping("/")
    public ModelAndView root(){
        Article s_article = new Article();
        s_article.setState(2); //设置选取审核通过的帖子
        List<Article> indexArticleList = articleService.list(s_article,1,20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);
        ModelAndView mav = new ModelAndView();
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.addObject("title","首页");
        mav.addObject("pageCode", PageUtil.genPagination("/article/list",total,1,20,""));

        mav.addObject("articleList",indexArticleList);
        mav.setViewName("index");
        return mav;
    }
}
