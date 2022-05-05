package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.User;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.service.UserDownloadService;
import com.linka39.code07.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        comment.setState(0);//设置状态
        //从session中获取用户信息可防止注入伪造
        comment.setUser((User) session.getAttribute("currentUser"));
        commentService.save(comment);
        map.put("success",true);
        return map;
    }
    /**
     * 分页查询用户评论
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(Comment s_comment,HttpSession session, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        Article s_article = new Article();
        s_article.setUser(user);
        s_comment.setArticle(s_article);
        s_comment.setState(2);//去掉审核不通过的
        String word = "";
        Pattern p_word;
        Matcher m_word;
        Comment tempComent = null;
        List<Comment> commentList = commentService.list(s_comment,page,limit, Sort.Direction.DESC,"commentDate");
        for(int i=0;i<commentList.size();i++){
            //如果评论用户不为当前登录的用户，敏感性评论需要对敏感词进行替换处理，匹配 <font>([\s\S]*?)<\/font>里的字符信息，将其替换为等量的替换字符串
            if(commentList.get(i).getState()==0 &&commentList.get(i).getUser().getId() != user.getId() ){
                String str=null;
                String regEx_font = "<font>([\\s\\S]*?)<\\/font>";
                String commentTemp = commentList.get(i).getContent();
                p_word = Pattern.compile(regEx_font, Pattern.CASE_INSENSITIVE);
                m_word = p_word.matcher(commentTemp);
                while (m_word.find()) {
                    word = m_word.group();
                    str = StringUtil.getReplaceCharsUtil(SensitiveWordFilter.replaceChar,word.length()-13);
                    commentTemp.replaceAll(word,str);
                }
                commentList.get(i).setContent(commentTemp);
            }
        }
        Long count = commentService.getTotal(s_comment);
        map.put("code",0);
        map.put("count",count);
        map.put("data",commentList);
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
        commentService.delete(id);
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
            commentService.delete(Integer.parseInt(idStr[i]));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 跳转到 评论管理页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/toCommentManagePage")
    public ModelAndView toCommentManagePage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title","评论管理页面");
        mav.setViewName("user/commentManage");
        return mav;
    }
}
