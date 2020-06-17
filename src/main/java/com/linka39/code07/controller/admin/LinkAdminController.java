package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.Link;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.service.ArcTypeService;
import com.linka39.code07.service.LinkService;
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
 * 友情链接admin控制层
 */
@Controller
@RequestMapping("/admin/link")
public class LinkAdminController {
    @Autowired
    private LinkService linkService;
    @Autowired
    private InitSystem initSystem;

    /**
     * 分页查询资源类别信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询友情链接"})//设置权限
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<Link> arcTypeList = linkService.list(page,limit, Sort.Direction.ASC,"sort");
        Long count = linkService.getTotal();
        map.put("code",0);
        map.put("count",count);
        map.put("data",arcTypeList);
        return map;
    }

    /**
     * 添加修改友情链接
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"添加或者修改友情链接"})//设置权限
    @ResponseBody
    public Map<String,Object> save(Link link, HttpServletRequest request){
        linkService.save(link);
        //数据修改后重新加载初始化数据
        initSystem.loadData(request.getServletContext());
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }
    /**
     * 删除类别信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"删除友情链接"})//设置权限
    @ResponseBody
    public Map<String,Object> delete(Integer id, HttpServletRequest request)throws Exception{
        linkService.delete(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        //数据修改后重新加载初始化数据
        initSystem.loadData(request.getServletContext());
        return map;
    }
    /**
     * 根据id查询资源类别实体
     * @return
     * @throws Exception
     */
    @RequestMapping("/findById")
    @RequiresPermissions(value = {"根据id查询友情链接实体"})//设置权限
    @ResponseBody
    public Map<String,Object> findById(Integer id)throws Exception{
        Link link = linkService.get(id);
        Map<String,Object> map = new HashMap<>();
        map.put("link",link);
        map.put("success",true);
        return map;
    }
}
