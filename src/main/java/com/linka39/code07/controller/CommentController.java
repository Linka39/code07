package com.linka39.code07.controller;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.util.PageUtil;
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
 * 评论控制器
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 根据条件分页查询评论信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public List<Comment> list(Comment s_comment,Integer page)throws Exception{
        s_comment.setState(1);//选择审核通过的
        return commentService.list(s_comment,page,6,Sort.Direction.DESC,"commentDate");
    }

}
