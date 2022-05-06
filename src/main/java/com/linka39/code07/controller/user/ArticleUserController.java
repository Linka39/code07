package com.linka39.code07.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.SensitiveArticle;
import com.linka39.code07.entity.User;
import com.linka39.code07.entity.UserDownload;
import com.linka39.code07.lucene.ArticleIndex;
import com.linka39.code07.service.*;
import com.linka39.code07.util.DateUtil;
import com.linka39.code07.util.RedisUtil;
import com.linka39.code07.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    private UserDownloadService userDownloadService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SensitiveArticleService sensitiveArticleService;
    @Autowired
    private ArticleIndex articleIndex;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisUtil<Article> redisUtil;
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
     * 跳转到帖子管理页面
     * @return
     */
    @RequestMapping("/toUnUsefulArticleManagePage")
    public ModelAndView toUnUsefulArticleManagePage(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","失效帖子管理页面");
        mav.setViewName("user/unuserfulArticleManage");
        return mav;
    }
    /**
     * 跳转到帖子修改
     * @return
     */
    @RequestMapping("/toModifyArticlePage/{id}")
    public ModelAndView toModifyArticlePage(@PathVariable("id")Integer id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("article",articleService.get(id));//Optional类型要转为article类型
        mav.addObject("title","帖子修改页面");
        mav.setViewName("user/modifyArticle");
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
     * 根据条件分页查询失效帖子信息
     * @return
     */
    @RequestMapping("/unuserfulList")
    @ResponseBody
    public Map<String,Object> unuserfulList(Article s_article,HttpSession session, @RequestParam(value="page",required = false)Integer page,@RequestParam(value="limit",required = false)Integer limit)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        s_article.setUser(user);
        s_article.setUseful(false);
        Map<String,Object> map = new HashMap<>();
        List<Article> articleList = articleService.list(s_article,page,limit, Sort.Direction.DESC,"publishDate");
        Long count = articleService.getTotal(s_article);
        map.put("code",0);
        map.put("count",count);
        session.setAttribute("unUserfulCount",count);
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
        redisUtil.set("article_"+article.getId(),article,10*60);
        articleIndex.updateIndex(article);
        mav.addObject("title","发布帖子成功页面");
        mav.setViewName("user/publishArticleSuccess");
        return mav;
    }
    /**
     * 修改帖子
     * @param article
     * @return
     */
    @RequestMapping("/update")
    public ModelAndView update(Article article){
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword(article.getPassword());
        oldArticle.setPoints(article.getPoints());
        if(oldArticle.getState()==2){
            //修改Lucene索引，redis缓存中删除这个索引
            articleIndex.updateIndex(oldArticle);
        }
        JSONObject sensitiveJson = null;
        if(!article.getReason().isEmpty()){
            sensitiveJson = JSONObject.parseObject(article.getReason());
            oldArticle.setState(4);
        }
        oldArticle.setUseful(true);
        articleService.save(oldArticle);
        redisUtil.set("article_"+oldArticle.getId(),oldArticle,10*60);
        articleIndex.updateIndex(oldArticle);
        if(sensitiveJson!=null){
            SensitiveArticle sensitiveArticle = sensitiveArticleService.findByArticleId(article.getId());
            sensitiveArticle.setCount(sensitiveJson.getIntValue("count"));
            sensitiveArticle.setSensitivecount(sensitiveJson.getIntValue("sensitiveNum"));
            sensitiveArticle.setCount(sensitiveJson.getIntValue("count"));
            sensitiveArticle.setEmotion(sensitiveJson.getString("emotion"));
            sensitiveArticle.setPapers(sensitiveJson.getIntValue("papers"));
            sensitiveArticle.setPoints(sensitiveJson.getIntValue("points"));
            sensitiveArticle.setLevel(sensitiveJson.getString("level"));
            sensitiveArticle.setName(article.getName());
            sensitiveArticle.setState(4);
            sensitiveArticle.setSensitiveWord(sensitiveJson.getJSONObject("sensitiveMap").getString("words"));
            sensitiveArticle.setUploadDate(new Date());
            sensitiveArticleService.save(sensitiveArticle);
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","修改帖子成功页面");
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

    /**
     * 根据id删除一条
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> map = new HashMap<>();
        articleService.delete(id);
        articleIndex.deleteIndex(String.valueOf(id));
        //删除该帖子下的所有评论
        commentService.deleteByArticleId(id);
        //删除用户下载该帖子的信息
        userDownloadService.deleteByArticleId(id);
        //删除redis索引
        map.put("success",true);
        return map;
    }
    /**
     * 多选删除
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    public Map<String,Object> delete(String ids)throws Exception{
        String[] idStr = ids.split(",");
        for(int i=0;i<idStr.length;++i){
            articleService.delete(Integer.parseInt(idStr[i]));
            //删除该帖子下的所有评论
            commentService.deleteByArticleId(Integer.parseInt(idStr[i]));
            userDownloadService.deleteByArticleId(Integer.parseInt(idStr[i]));
            articleIndex.deleteIndex(String.valueOf(idStr[i]));
            //删除redis索引
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

    /**
     * 跳转到资源下载页面
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/toDownloadPage/{id}")
    public ModelAndView toDownloadPage(@PathVariable(value = "id")Integer id,HttpSession session)throws Exception{
        //积分信息不能直接传入，避免别人恶意修改
        UserDownload userDownload = new UserDownload();
        Article article = articleService.get(id);

        User user = (User) session.getAttribute("currentUser");
        Boolean isDownload=false;//是否下载过
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(),id);
        if(count>0)
            isDownload=true;
        if(!isDownload){
            if(user.getPoints()-article.getPoints()<0) {//用户积分不够
                return null;
            }
            //扣下载人积分
            user.setPoints(user.getPoints()-article.getPoints());
            userService.save(user);
            //加分享人积分
            User articleUser = article.getUser();
            articleUser.setPoints(articleUser.getPoints()+article.getPoints());
            userService.save(articleUser);
            //保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(user);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);
        }
        ModelAndView mav=new ModelAndView();
        mav.addObject("article",articleService.get(id));
        mav.setViewName("user/downloadPage");
        return mav;
    }
    /**
     * 跳转到VIP下载页面
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/toVipDownloadPage/{id}")
    public ModelAndView toVipDownloadPage(@PathVariable(value = "id")Integer id,HttpSession session)throws Exception{
        //积分信息不能直接传入，避免别人恶意修改
        UserDownload userDownload = new UserDownload();
        Article article = articleService.get(id);

        User user = (User) session.getAttribute("currentUser");
        if(!user.getVip()){//判断是否vip
            return null;
        }
        Boolean isDownload=false;//是否下载过
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(),id);
        if(count>0)
            isDownload=true;
        if(!isDownload){

            //保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(user);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);
        }
        ModelAndView mav=new ModelAndView();
        mav.addObject("article",articleService.get(id));
        mav.setViewName("user/downloadPage");
        return mav;
    }
}
