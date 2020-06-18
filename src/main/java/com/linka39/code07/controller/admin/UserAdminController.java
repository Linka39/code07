package com.linka39.code07.controller.admin;
import com.linka39.code07.entity.User;

import com.linka39.code07.service.UserService;
import com.linka39.code07.util.CryptographyUtil;
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

    /**
     * 重置用户密码
     * @return
     * @throws Exception
     */
    @RequestMapping("/resetPassword")
    @RequiresPermissions(value = {"重置用户密码"})//设置权限
    @ResponseBody
    public Map<String,Object> resetPassword(Integer id)throws Exception{
        User oldUser = userService.findById(id);
        oldUser.setPassword(CryptographyUtil.md5("111",CryptographyUtil.SALT));
        userService.save(oldUser);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 用户积分充值
     * @return
     * @throws Exception
     */
    @RequestMapping("/addPoints")
    @RequiresPermissions(value = {"用户积分充值"})//设置权限
    @ResponseBody
    public Map<String,Object> addPoints(Integer id,Integer points)throws Exception{
        User oldUser = userService.findById(id);
        oldUser.setPoints(oldUser.getPoints()+points);
        userService.save(oldUser);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 修改用户VIP状态
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateVipState")
    @RequiresPermissions(value = {"修改用户VIP状态"})//设置权限
    @ResponseBody
    public Map<String,Object> updateVipState(Integer id,Boolean vip)throws Exception{
        User oldUser = userService.findById(id);
        oldUser.setVip(vip);
        userService.save(oldUser);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 修改用户状态
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateUserState")
    @RequiresPermissions(value = {"修改用户状态"})//设置权限
    @ResponseBody
    public Map<String,Object> updateUserState(Integer id,Boolean off)throws Exception{
        User oldUser = userService.findById(id);
        oldUser.setOff(off);
        userService.save(oldUser);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

}
