package com.linka39.code07.controller.user;

import com.linka39.code07.entity.User;
import com.linka39.code07.service.UserDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
