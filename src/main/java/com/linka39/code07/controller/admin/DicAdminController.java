package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Dic;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.service.DicService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词信息admin控制层
 */
@Controller
@RequestMapping("/admin/Dic")
public class DicAdminController {
    @Autowired
    private DicService dicService;

    /**
     * 分页查询用户信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询字典参数信息"})//设置权限
    @ResponseBody
    public Map<String,Object> list(Dic s_dic, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<Dic> userList = dicService.list(s_dic,page,limit, Sort.Direction.ASC,"id");
        Long count = dicService.getTotal(s_dic);
        map.put("code",0);
        map.put("count",count);
        map.put("data",userList);
        return map;
    }


    /**
     * 添加修改友情链接
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"添加或者修改字典参数"})//设置权限
    @ResponseBody
    public Map<String,Object> save(Dic s_dic, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try{
            dicService.save(s_dic);
            if("sensitive_cs".equals(s_dic.getZddm())){
                SensitiveWordFilter.stopWordStr = s_dic.getZdz();
            }else if ("sensitive_replace_char".equals(s_dic.getZddm())){
                SensitiveWordFilter.replaceChar = s_dic.getZdz();
            }else if ("sensitive_min_class".equals(s_dic.getZddm())){
                SensitiveWordFilter.accessLevel = s_dic.getZdz();
            }else if ("sensitive_math_type".equals(s_dic.getZddm())){
                SensitiveWordFilter.sensiMatchType = Integer.parseInt(s_dic.getZdz());
            }

            map.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
        }
        return map;
    }
    /**
     * 删除类别信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"删除字典参数"})//设置权限
    @ResponseBody
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map = new HashMap<>();
        try{
            dicService.delete(id);
            map.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
        }

        return map;
    }

    /**
     * 多选删除
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"删除字典参数"})//设置权限
    @RequestMapping("/deleteSelected")
    public Map<String,Object> delete(String ids)throws Exception{
        Map<String,Object> map = new HashMap<>();
        try{
            String[] idStr = ids.split(",");
            for(int i=0;i<idStr.length;++i){
                dicService.delete(Integer.parseInt(idStr[i]));
            }
            map.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
        }
        return map;
    }

    /**
     * 根据id查询资源类别实体
     * @return
     * @throws Exception
     */
    @RequestMapping("/findById")
    @RequiresPermissions(value = {"根据id查询字典参数实体"})//设置权限
    @ResponseBody
    public Map<String,Object> findById(Integer id)throws Exception{
        Dic dic = dicService.find(id);
        Map<String,Object> map = new HashMap<>();
        map.put("dic",dic);
        map.put("success",true);
        return map;
    }

    /**
     * 根据zddm 查询字典类
     * @return
     * @throws Exception
     */
    @RequestMapping("/findByZddm")
    @ResponseBody
    public Map<String,Object> findByZddm(String zddm)throws Exception{
        List<Dic> dic_list = dicService.getZdByzddm(zddm);
        Map<String,Object> map = new HashMap<>();
        map.put("dic_list",dic_list);
        map.put("success",true);
        return map;
    }

}
