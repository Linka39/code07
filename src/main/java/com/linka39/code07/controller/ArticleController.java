package com.linka39.code07.controller;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Article;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 资源帖子控制器
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 根据条件分页查询资源帖子信息
     * @return
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(@RequestParam(value = "typeId",required = false)Integer typeId, @PathVariable(value = "id",required = false)Integer page){
        ModelAndView mav = new ModelAndView();
        Article s_article = new Article();
        s_article.setState(2); //设置选取审核通过的帖子
        if(typeId == null){
            mav.addObject("title","第"+page+"页");
        }else{
            ArcType arcType= InitSystem.arcTypeMap.get(typeId);
            s_article.setArcType(arcType);
            mav.addObject("title",arcType.getName()+"-第"+page+"页");
        }

        List<Article> indexArticleList = articleService.list(s_article,page,20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.addObject("title","第"+page+"页");
        mav.addObject("articleList",indexArticleList);
        StringBuffer param = new StringBuffer();
        if(typeId!=null){
            param.append("?typeId="+typeId);
        }
        mav.addObject("pageCode", PageUtil.genPagination("/article/list",total,page,20,param.toString()));
        mav.setViewName("index");
        return mav;
    }
}
