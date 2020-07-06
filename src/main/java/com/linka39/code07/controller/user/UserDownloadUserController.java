package com.linka39.code07.controller.user;

import com.linka39.code07.entity.Message;
import com.linka39.code07.entity.User;
import com.linka39.code07.entity.UserDownload;
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

@Controller
@RequestMapping("/user/userDownload")
public class UserDownloadUserController {
    @Autowired
    private UserDownloadService userDownloadService;

    /**
     * 判断资源是否下载过
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/exist")
    public Boolean exist(Integer id, HttpSession session)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(),id);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 分页查询用户下载资源信息
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(HttpSession session, @PathVariable(value = "id",required = false)Integer page)throws Exception{
        User user= (User) session.getAttribute("currentUser");
        UserDownload s_userDownload=new UserDownload();
        s_userDownload.setUser(user);
        user.setMessageCount(0);
        session.setAttribute("currentUser",user);//重置session
        List<UserDownload> userDownloadList =userDownloadService.list(s_userDownload,page,10, Sort.Direction.DESC,"downloadDate");
        Long total = userDownloadService.getTotal(s_userDownload);
        ModelAndView mav=new ModelAndView();
        mav.addObject("userDownloadList",userDownloadList);
        mav.addObject("title","用户已下载资源页面");
        mav.addObject("pageCode", PageUtil.genPagination("/user/message/list",total,page,10,""));
        mav.setViewName("user/listUserDownload");
        return mav;
    }

    /**
     * 判断用户积分是否足够下载
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/enough")
    public Boolean enough(Integer points,HttpSession session)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        if(user.getPoints()>=points)
            return true;
        else
            return false;
    }
}
