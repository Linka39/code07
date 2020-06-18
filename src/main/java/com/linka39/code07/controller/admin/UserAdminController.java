package com.linka39.code07.controller.admin;
import com.linka39.code07.entity.User;

import com.linka39.code07.service.UserService;
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
@RequestMapping("/admin/user")
public class UserAdminController {
    @Autowired
    private UserService userService;

    /**
     * 分页查询用户信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询用户信息"})//设置权限
    @ResponseBody
    public Map<String,Object> list(User s_user, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<User> userList = userService.list(s_user,page,limit, Sort.Direction.DESC,"registerDate");
        Long count = userService.getTotal(s_user);
        map.put("code",0);
        map.put("count",count);
        map.put("data",userList);
        return map;
    }

}
