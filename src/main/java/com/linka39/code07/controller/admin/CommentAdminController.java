package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息admin控制层
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {
    @Autowired
    private CommentService commentService;

    /**
     * 分页查询评论信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询评论信息"})//设置权限
    @ResponseBody
    public Map<String,Object> list(Comment s_comment, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        s_comment=null;
        List<Comment> commentList = commentService.list(s_comment,page,limit, Sort.Direction.DESC,"commentDate");
        Long count = commentService.getTotal(s_comment);
        map.put("code",0);
        map.put("count",count);
        map.put("data",commentList);
        return map;
    }

    /**
     * 修改评论状态
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateState")
    @RequiresPermissions(value = {"修改评论状态"})//设置权限
    @ResponseBody
    public Map<String,Object> updateState(Integer id,Boolean state)throws Exception{
        Comment oldComment = commentService.find(id);
        if(state){
            oldComment.setState(1);
        }else{
            oldComment.setState(2);
        }
        commentService.save(oldComment);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 根据id删除一条
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"删除评论"})//设置权限
    @RequestMapping("/delete")
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> map = new HashMap<>();
        commentService.delete(id);
        map.put("success",true);
        return map;
    }
    /**
     * 多选删除
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"删除评论"})//设置权限
    @RequestMapping("/deleteSelected")
    public Map<String,Object> delete(String ids)throws Exception{
        String[] idStr = ids.split(",");
        for(int i=0;i<idStr.length;++i){
            commentService.delete(Integer.parseInt(idStr[i]));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
}
