package com.linka39.code07.controller;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Comment;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.lucene.ArticleIndex;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.util.PageUtil;
import com.linka39.code07.util.RedisUtil;
import com.linka39.code07.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleIndex articleIndex;
    @Autowired
    private RedisUtil<Article> redisUtil;

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
     * 关键字分词搜索
     * @return
     */
    @RequestMapping("/search")
    public ModelAndView search(String q,@RequestParam(value = "page",required = false)String page,HttpServletRequest request) throws Exception {
        //通过session来获取资源类型
        request.getSession().setAttribute("tMenu","t_0");
        if(StringUtil.isEmpty(page)){
            page="1";
        }
        Article s_article = new Article();
        s_article.setState(2); //设置选取审核通过的帖子
        s_article.setHot(true);//设置选取热门的帖子
        List<Article> indexHotArticleList = articleService.list(s_article,1,43, Sort.Direction.DESC,"publishDate");
        List<Article> articleList=articleIndex.search(q);
        Integer pageSize = 10;
        Integer curPage = Integer.parseInt(page);
        Integer toIndex=articleList.size()>curPage*10?curPage*10:articleList.size();
        ModelAndView mav = new ModelAndView();
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.addObject("title",q);
        mav.addObject("pageCode", this.genUpAndDownPageCode(curPage,articleList.size(),q,pageSize));
        mav.addObject("q",q);
        mav.addObject("resultTotal",articleList.size());
        mav.addObject("articleList",   articleList.subList((curPage-1)*10,toIndex));//对集合框架进行截取
        mav.addObject("hotArticleList", indexHotArticleList);
        mav.setViewName("result");
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
        String key="article_"+id;
        Article article = null;
        if(redisUtil.hasKey(key)){
            article= (Article) redisUtil.get(key);
        }else{
            //用get()将optional类转换为实体类
             article=articleService.get(id);
            redisUtil.set(key,article,20*60);
        }

        List<Article> hotArticleList =null;
        String hKey="hotArticleList_type_"+article.getArcType().getId();
        if(redisUtil.hasKey(hKey)){
            hotArticleList= redisUtil.lGet(hKey,0,-1);
        }else{
            Article s_article = new Article();
            s_article.setHot(true);
            s_article.setState(2);
            s_article.setArcType(article.getArcType());//获取同资源类型
            hotArticleList = articleService.list(s_article,1,43, Sort.Direction.DESC,"publishDate");
            redisUtil.lSet(hKey,hotArticleList,10*60);
        }

        mav.addObject("hotArticleList",hotArticleList);
        Comment s_comment = new Comment();
        s_comment.setArticle(article);
        s_comment.setState(2);//去掉审核不通过的
        //todo 敏感词
        mav.addObject("commentCount",commentService.getTotal(s_comment));
        mav.addObject("article",article);
        mav.addObject("title",article.getName());
        mav.setViewName("article");
        return mav;
    }
    //加载相关资源
    @RequestMapping("/loadRelatedResources")
    @ResponseBody
    public List<Article> loadRelatedResourcs(String q)throws Exception{
        if(StringUtil.isEmpty(q)){
            return null;
        }
        List<Article> articleList= articleIndex.searchNoHighLighter(q);
        return articleList;
    }

    /**
     * 生成上一页，下一页代码
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @return
     */
    private String genUpAndDownPageCode(Integer page,Integer totalNum,String q,Integer pageSize){
        long totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
        StringBuffer pageCode=new StringBuffer();
        if(totalPage==0){
            return "";
        }else{
            pageCode.append("<div class='layui-box layui-laypage layui-laypage-default'>");
            if(page>1){
                pageCode.append("<a href='/article/search?page="+(page-1)+"&q="+q+"' class='layui-laypage-prev'>上一页</a>");
            }else{
                pageCode.append("<a href='#' class='layui-laypage-prev layui-disabled'>上一页</a>");
            }
            if(page<totalPage){
                pageCode.append("<a href='/article/search?page="+(page+1)+"&q="+q+"' class='layui-laypage-next'>下一页</a>");
            }else{
                pageCode.append("<a href='#' class='layui-laypage-next layui-disabled'>下一页</a>");
            }
            pageCode.append("</div>");
        }
        return pageCode.toString();
    }
    //查看次数+1
    @RequestMapping("/updateView")
    @ResponseBody
    private void updateView(Integer id)throws Exception{
        Article article = articleService.get(id);
        article.setView(article.getView()+1);
        articleService.save(article);
    }
}
