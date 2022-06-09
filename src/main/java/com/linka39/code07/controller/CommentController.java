package com.linka39.code07.controller;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.User;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.sensitiveUtil.SensitiveWordInit;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.util.PageUtil;
import com.linka39.code07.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<Comment> list(Comment s_comment,Integer page, HttpSession session)throws Exception{
        User user =(User) session.getAttribute("currentUser");
        s_comment.setState(2);//去掉审核不通过的
        String word = "";
        Pattern p_word;
        Matcher m_word;
        int userid = (int)user.getId();
        List<Comment> myCommentArr = commentService.list(s_comment,page,6,Sort.Direction.DESC,"commentDate");
        for(int i=0;i<myCommentArr.size();i++){
            //敏感性评论需要对敏感词进行替换处理
            if(myCommentArr.get(i).getState()==0 && userid != myCommentArr.get(i).getUser().getId()){
                String str=null;
                String regEx_font = "<font>([\\s\\S]*?)<\\/font>";
                String commentTemp = myCommentArr.get(i).getContent();
                p_word = Pattern.compile(regEx_font, Pattern.CASE_INSENSITIVE);
                m_word = p_word.matcher(commentTemp);
                while (m_word.find()) {
                    word = m_word.group();
                    str = StringUtil.getReplaceCharsUtil(SensitiveWordFilter.replaceChar,word.length()-13);
                    commentTemp = commentTemp.replaceAll(word,str);
                }
                myCommentArr.get(i).setContent(commentTemp);
            }
        }
        return myCommentArr;
    }

}
