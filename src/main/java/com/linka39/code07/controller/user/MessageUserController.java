package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Message;
import com.linka39.code07.entity.User;
import com.linka39.code07.service.MessageService;
import com.linka39.code07.service.UserDownloadService;
import com.linka39.code07.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 消息资源控制器
 */
@Controller
@RequestMapping("/user/message")
public class MessageUserController {
    @Autowired
    private MessageService messageService;

    /**
     * 查看用户系统消息，状态都改为已查看
     */
    @RequestMapping("/see")
    public ModelAndView see( HttpSession session)throws Exception{
        User user= (User) session.getAttribute("currentUser");
        Message s_message=new Message();
        s_message.setUser(user);
        messageService.updateState(user.getId());
        user.setMessageCount(0);
        session.setAttribute("currentUser",user);//重置session
        List<Message> messageList =messageService.list(s_message,1,10, Sort.Direction.DESC,"publishDate");
        Long total = messageService.getTotal(s_message);
        ModelAndView mav=new ModelAndView();
        mav.addObject("messageList",messageList);
        mav.addObject("title","linka39");
        mav.addObject("pageCode", PageUtil.genPagination("/message/list",total,1,10,""));
        mav.setViewName("user/listMessage");
        return mav;
    }

    /**
     * 分页查询消息帖子
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(HttpSession session, @PathVariable(value = "id",required = false)Integer page)throws Exception{
        User user= (User) session.getAttribute("currentUser");
        Message s_message=new Message();
        s_message.setUser(user);

        messageService.updateState(user.getId());
        user.setMessageCount(0);
        session.setAttribute("currentUser",user);//重置session
        List<Message> messageList =messageService.list(s_message,page,10, Sort.Direction.DESC,"publishDate");
        Long total = messageService.getTotal(s_message);
        ModelAndView mav=new ModelAndView();
        mav.addObject("messageList",messageList);
        mav.addObject("title","linka39");
        mav.addObject("pageCode", PageUtil.genPagination("/user/message/list",total,page,10,""));
        mav.setViewName("user/listMessage");
        return mav;
    }
}
