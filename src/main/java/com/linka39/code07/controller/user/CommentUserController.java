package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.User;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.service.UserDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论控制器
 */
@Controller
@RequestMapping("/user/comment")
public class CommentUserController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    //@postMapping = @requestMapping(method = RequestMethod.POST)
    @PostMapping(value = "/save")//处理指定的请求方式，多用于内容的添加
    public Map<String,Object> save(Comment comment,HttpSession session)throws Exception{
        Map<String,Object> map = new HashMap<>();
        comment.setCommentDate(new Date());
        comment.setState(0);//设置状态
        //从session中获取用户信息可防止注入伪造
        comment.setUser((User) session.getAttribute("currentUser"));
        commentService.save(comment);
        map.put("success",true);
        return map;
    }
}
