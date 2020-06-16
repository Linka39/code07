package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.Article;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.util.PageUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * admin url控制层
 */
@Controller
public class IndexAdminController {

    @Autowired
    private ArticleService articleService;

    /**
     *根目录请求
     * @return
     */
    @RequiresPermissions(value = {"进入管理员主页"})
    @RequestMapping("/toAdminUserCenterPage")
    public ModelAndView toAdminUserCenterPage(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("title","管理员主页页面");
        mav.setViewName("admin/adminUserCenter");
        return mav;
    }
}
