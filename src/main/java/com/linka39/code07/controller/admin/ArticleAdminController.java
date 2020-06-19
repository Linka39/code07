package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Link;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.LinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

}
