package com.linka39.code07.controller.admin;

import com.linka39.code07.bayesianUtil.TrainSampleDataManager;
import com.linka39.code07.config.SensitivePathConfig;
import com.linka39.code07.entity.Dic;
import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.SensitiveWord;
import com.linka39.code07.entity.User;
import com.linka39.code07.sensitiveUtil.FileUtils;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.service.DicService;
import com.linka39.code07.service.SensitiveService;
import com.linka39.code07.service.SensitiveWordService;
import com.linka39.code07.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词信息admin控制层
 */
@Controller
@RequestMapping("/admin/sensitiveWord")
public class SensitiveWordAdminController {

    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Autowired
    private DicService dicService;
    @Autowired
    private SensitivePathConfig sensitivePathConfig;

    /**
     * 分页查询用户信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"分页查询敏感词信息"})//设置权限
    @ResponseBody
    public Map<String,Object> list(SensitiveWord s_sensitiveWord, @RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<SensitiveWord> userList = sensitiveWordService.list(s_sensitiveWord,page,limit, Sort.Direction.DESC,"emotion");
        Long count = sensitiveWordService.getTotal(s_sensitiveWord);
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
    @RequiresPermissions(value = {"添加或者修改敏感词"})//设置权限
    @ResponseBody
    public Map<String,Object> save(SensitiveWord sensitiveWord, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try{
            sensitiveWordService.save(sensitiveWord);
            //数据修改后重新加载初始化数据
            SensitiveWordFilter.sensitiveWordMap = null;
            sensitivePathConfig.initSensitiveWordMap();//初始化敏感词库sensitiveWordMap
            String note = dicService.getNoteByzddm("sensitive_class",sensitiveWord.getEmotion());
            String content = sensitiveWord.getWord();
            StringBuilder stringBuilder = null;
            for(int i=0;i<sensitiveWord.getEmotionWeight();i++){
                stringBuilder.append(content + "\n");
            }
            FileUtils.deleteSensitiveWordFile(SensitivePathConfig.sensitivePath+note, content);
            FileUtils.continueWrite(sensitivePathConfig.sensitivePath+note, stringBuilder.toString());
            TrainSampleDataManager.allWordsMap = null;
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
    @RequiresPermissions(value = {"删除敏感词"})//设置权限
    @ResponseBody
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map = new HashMap<>();
        try{
            SensitiveWord sensitiveWord = sensitiveWordService.find(id);
            String note = dicService.getNoteByzddm("sensitive_class",sensitiveWord.getEmotion());
            sensitiveWordService.delete(id);

            FileUtils.deleteSensitiveWordFile(SensitivePathConfig.sensitivePath+note, sensitiveWord.getWord());
            //数据修改后重新加载初始化数据
            SensitiveWordFilter.sensitiveWordMap = null;
            sensitivePathConfig.initSensitiveWordMap();//初始化敏感词库sensitiveWordMap
            TrainSampleDataManager.allWordsMap = null;
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
    @RequiresPermissions(value = {"删除敏感词"})//设置权限
    @RequestMapping("/deleteSelected")
    public Map<String,Object> delete(String ids)throws Exception{
        Map<String,Object> map = new HashMap<>();
        try{
            String[] idStr = ids.split(",");
            for(int i=0;i<idStr.length;++i){
                sensitiveWordService.delete(Integer.parseInt(idStr[i]));
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
    @RequiresPermissions(value = {"根据id查询敏感词实体"})//设置权限
    @ResponseBody
    public Map<String,Object> findById(Integer id)throws Exception{
        SensitiveWord sensitiveWord = sensitiveWordService.find(id);
        Map<String,Object> map = new HashMap<>();
        map.put("sensitiveWord",sensitiveWord);
        map.put("success",true);
        return map;
    }
}
