package com.linka39.code07.controller;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.UserService;
import com.linka39.code07.util.CryptographyUtil;
import com.linka39.code07.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页url控制层
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    @ResponseBody//要加入ResponseBody,或RestController，将返回的信息转换为json格式
    public Map<String,Object> register(@Valid User user, BindingResult bindingResult) throws Exception{
        Map<String,Object> map = new HashMap<> ();
        if(bindingResult.hasErrors()){
            map.put("success",false);
            map.put("errorInfo",bindingResult.getFieldError().getDefaultMessage());
        }else if(userService.findByUserName(user.getUserName())!=null){
            map.put("success", false);
            map.put("errorInfo", "用户名已存在，请更换！");
        }else if(userService.findByEmail(user.getEmail())!=null) {
            map.put("success", false);
            map.put("errorInfo", "邮箱已存在，请更换！");
        } else{
            user.setPassword(CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            user.setRegisterDate(new Date());
            user.setImageName("default.jpg");
            userService.save(user);
            map.put("success",true);
        }
        return map;
    }
}
