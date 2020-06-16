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

import javax.servlet.http.HttpServletRequest;
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
    public ModelAndView list(@RequestParam(value = "typeId",required = false)Integer typeId, @PathVariable(value = "id",required = false)Integer page,
                             HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        Article s_article = new Article();
        s_article.setState(2); //设置选取审核通过的帖子
        if(typeId == null){
            mav.addObject("title","第"+page+"页");
        }else{
            ArcType arcType= InitSystem.arcTypeMap.get(typeId);
            s_article.setArcType(arcType);
            mav.addObject("title",arcType.getName()+"-第"+page+"页");
            request.getSession().setAttribute("tMenu","t_"+typeId);
        }

        List<Article> articleList = articleService.list(s_article,page,20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);
        s_article.setHot(true);//根据前面设置的类别来显示热门数据
        List<Article> hotArticleList = articleService.list(s_article,1,43, Sort.Direction.DESC,"publishDate");
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.addObject("title","第"+page+"页");
        mav.addObject("articleList",articleList);
        mav.addObject("hotArticleList",hotArticleList);
        StringBuffer param = new StringBuffer();
        if(typeId!=null){
            param.append("?typeId="+typeId);
        }
        //扩展实现分页展示资源类型类
        mav.addObject("pageCode", PageUtil.genPagination("/article/list",total,page,20,param.toString()));
        mav.setViewName("index");
        return mav;
    }

    /**
     * 根据id查询帖子详情信息
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable(value = "id")Integer id)throws Exception{
        ModelAndView mav = new ModelAndView();
        //用get()将optional类转换为实体类
        Article article=articleService.get(id);
        Article s_article = new Article();
        s_article.setHot(true);
        s_article.setArcType(article.getArcType());//获取同资源类型
        List<Article> hotArticleList = articleService.list(s_article,1,43, Sort.Direction.DESC,"publishDate");
        mav.addObject("hotArticleList",hotArticleList);
        mav.addObject("article",article);
        mav.addObject("title",article.getName());
        mav.setViewName("article");
        return mav;
    }
}
