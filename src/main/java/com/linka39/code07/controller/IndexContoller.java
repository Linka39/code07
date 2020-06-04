package com.linka39.code07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页url控制层
 */
@Controller
public class IndexContoller {
    /**
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView root(){
        ModelAndView mav = new ModelAndView();
        //mav.setViewName 默认的引擎就为tyhmleaf
        mav.setViewName("index");
        return mav;
    }
}
