package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.util.DateUtil;
import com.linka39.code07.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户页面跳转控制器
 */

@Controller
@RequestMapping("/user/article")
public class ArticleUserController {
    @Value("${articleImageFilePath}")
    private String articleImageFilePath;
    @Autowired
    private ArticleService articleService;
    /**
     * 跳转到发布帖子页面
     * @return
     */
    @RequestMapping("/toPublishArticlePage")
    public ModelAndView toPublishArticlePage(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","发布帖子页面");
        mav.setViewName("user/publishArticle");
        return mav;
    }
    /**
     * 跳转到帖子管理页面
     * @return
     */
    @RequestMapping("/toArticleManagePage")
    public ModelAndView toArticleManagePage(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","帖子管理页面");
        mav.setViewName("user/articleManage");
        return mav;
    }

    /**
     * 根据条件分页查询帖子信息
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(Article s_article,HttpSession session, @RequestParam(value="page",required = false)Integer page,@RequestParam(value="limit",required = false)Integer limit)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        s_article.setUser(user);
        Map<String,Object> map = new HashMap<>();
        List<Article> articleList = articleService.list(s_article,page,limit, Sort.Direction.DESC,"publishDate");
        Long count = articleService.getTotal(s_article);
        map.put("code",0);
        map.put("count",count);
        map.put("data",articleList);
        return map;
    }

    /**
     * 添加帖子
     * @param article
     * @param session
     * @return
     */
    @RequestMapping("/add")
    public ModelAndView add(Article article,HttpSession session){
        User user= (User) session.getAttribute("currentUser");
        article.setPublishDate(new Date());
        article.setUser(user);
        article.setState(1);
        article.setView(StringUtil.randomInteger());
        ModelAndView mav = new ModelAndView();
        articleService.save(article);
        mav.addObject("title","发布帖子成功页面");
        mav.setViewName("user/publishArticleSuccess");
        return mav;
    }
    /**
     * layedit上传图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> map = new HashMap<>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename();//获取原文件名
            String suffixName= fileName.substring(fileName.lastIndexOf(".")); //获取后缀
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;    //新名字
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(articleImageFilePath+DateUtil.getCurrentDatePath()+newFileName));//根据年月日拷贝到新文件
            map.put("code",0);
            map.put("msg","上传成功");
            Map<String,Object> map2=new HashMap<>();
            //layui指定上传格式，data为标签中对应的属性
            map2.put("src","/image/"+DateUtil.getCurrentDatePath()+newFileName);
            map2.put("title",newFileName);
            map.put("data",map2);
        }
        return map;
    }
}
