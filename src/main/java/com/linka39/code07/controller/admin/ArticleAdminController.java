package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Link;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.lucene.ArticleIndex;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.service.LinkService;
import com.linka39.code07.service.UserDownloadService;
import com.linka39.code07.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源admin控制层
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleAdminController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private InitSystem initSystem;
    @Value("${articleImageFilePath}")
    private String articleImageFilePath;
    @Autowired
    ArticleIndex articleIndex;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserDownloadService userDownloadService;

    /**
     * 生成所有帖子的索引
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value = {"生成所有帖子索引"})//设置权限
    @RequestMapping(value = "/genAllIndex")
    public Boolean genAllIndex(){
        List<Article> articleList = articleService.listAll();
        for(Article article:articleList){
            if(!articleIndex.addIndex(article)){//有一索引没添加成功的话
                return false;
            }
        }
        return true;
    }

    /**
     * 修改帖子
     * @param article
     * @return
     */
    @RequestMapping("/update")
    @RequiresPermissions(value = {"修改帖子"})//设置权限
    public ModelAndView update(Article article){
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword(article.getPassword());
        oldArticle.setPoints(article.getPoints());
        if(oldArticle.getState()==2){//审核未通过时，更新Lucene索引
            articleIndex.updateIndex(oldArticle);
           //todo 修改Lucene索引，redis缓存中删除这个索引
        }
        articleService.save(oldArticle);
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","修改帖子成功页面");
        mav.setViewName("admin/modifyArticleSuccess");
        return mav;
    }
    /**
     * layedit上传图片
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value = {"图片上传"})//设置权限
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
     * 分页查询资源帖子信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询资源帖子信息"})//设置权限
    @ResponseBody
    public Map<String,Object> list(Article s_article, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<Article> articleList = articleService.list(s_article,page,limit, Sort.Direction.DESC,"checkDate");
        Long count = articleService.getTotal(s_article);
        map.put("code",0);
        map.put("count",count);
        map.put("data",articleList);
        return map;
    }

    /**
     * 跳转到帖子审核页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/toReViewArticlePage/{id}")
    @RequiresPermissions(value = {"跳转到帖子审核页面"})//设置权限
    public ModelAndView toReViewArticlePage(@PathVariable(value = "id") Integer id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","审核帖子页面");
        mav.addObject("article",articleService.get(id));
        mav.setViewName("admin/reviewArticle");
        return mav;
    }
    /**
     * 跳转到帖子修改页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/toModifyArticlePage/{id}")
    @RequiresPermissions(value = {"跳转到帖子审核页面"})//设置权限
    public ModelAndView toModifyArticlePage(@PathVariable(value = "id") Integer id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","修改帖子页面");
        mav.addObject("article",articleService.get(id));
        //返回的都为嵌入的iframe页面
        mav.setViewName("admin/modifyArticle");
        return mav;
    }

    /**
     * 更新资源帖子的状态
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateState")
    @RequiresPermissions(value = {"修改状态"})//设置权限
    @ResponseBody
    public Map<String,Object> updateState(Article article)throws Exception{
        Map<String,Object> map = new HashMap<>();
        Article oldArticle = articleService.get(article.getId());
        // todo 消息模块要添加一个
        if(article.getState()==2){
            oldArticle.setState(2);
            articleIndex.addIndex(oldArticle);
        }else{
            //todo 删除redis首页数据缓存
            oldArticle.setState(3);
            oldArticle.setReason(article.getReason());
        }
        articleService.save(oldArticle);
        map.put("success",true);
        return map;
    }
    /**
     * 根据id删除一条
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"删除帖子"})//设置权限
    @RequestMapping("/delete")
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> map = new HashMap<>();
        articleService.delete(id);
        //todo 删除该帖子下的所有评论
        //todo 删除redis索引
        commentService.deleteByArticleId(id);
        userDownloadService.deleteByArticleId(id);
        //todo 删除索引
        articleIndex.deleteIndex(String.valueOf(id));
        map.put("success",true);
        return map;
    }
    /**
     * 多选删除
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"删除帖子"})//设置权限
    @RequestMapping("/deleteSelected")
    public Map<String,Object> delete(String ids)throws Exception{
        String[] idStr = ids.split(",");
        for(int i=0;i<idStr.length;++i){
            articleService.delete(Integer.parseInt(idStr[i]));
            //todo 删除该帖子下的所有评论
            commentService.deleteByArticleId(Integer.parseInt(idStr[i]));
            userDownloadService.deleteByArticleId(Integer.parseInt(idStr[i]));
            //todo 删除redis索引
            //todo 删除索引
            articleIndex.deleteIndex(String.valueOf(idStr[i]));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 修改热门状态
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"修改热门状态"})//设置权限
    @RequestMapping("/updateHotState")
    public Map<String,Object> updateHotState(Integer id,Boolean hot)throws Exception{
        Article oldArticle = articleService.get(id);
        oldArticle.setHot(hot);
        articleService.save(oldArticle);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }


}
