package com.linka39.code07.controller.admin;

import com.linka39.code07.entity.BgImage;
import com.linka39.code07.init.InitSystem;
import com.linka39.code07.service.BgImageService;
import com.linka39.code07.util.DateUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 背景图片admin控制层
 */
@Controller
@RequestMapping("/admin/bgImage")
public class BgImageAdminController {
    @Autowired
    private BgImageService bgImageService;
    @Autowired
    private InitSystem initSystem;

    @Value("${bgImagePath}")
    private String bgImagePath;

    /**
     * 分页查询背景图片信息
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page",required = false)Integer page, @RequestParam(value="limit",required = false)Integer limit)throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<BgImage> BgImageList = bgImageService.list(page,limit, Sort.Direction.ASC,"id");
        Long count = bgImageService.getTotal();
        map.put("code",0);
        map.put("count",count);
        map.put("data",BgImageList);
        return map;
    }

    /**
     * 实现图片上传
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadBgImage")
    @ResponseBody
    public Map<String,Object> uploadImage(MultipartFile file, HttpSession session)throws Exception{
        Map<String,Object> map = new HashMap<>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename();//获取原文件名
            String suffixName= fileName.substring(fileName.lastIndexOf(".")); //获取后缀
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;    //新名字
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(bgImagePath+newFileName));//拷贝到新文件
            File toPic = new File(bgImagePath+ "thumbs/" +newFileName);
            Thumbnails.of(file.getInputStream()).size(100,100).toFile(toPic);//变为100*100,遵循原图比例缩或放到100*某个高度

            map.put("code",0);
            map.put("msg","背景图片上传成功");
            Map<String,Object> map2=new HashMap<>();
            //layui指定上传格式，data为标签中对应的属性
            map2.put("src","/loginImages/"+newFileName);
            map2.put("title",newFileName);
            map.put("data",map2);
        }
        return map;
    }

    /**
     * 添加修改背景图片信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String,Object> save(BgImage bgImage, HttpServletRequest request)throws Exception{
        bgImage.setUpdateDate(new Date());
        bgImageService.save(bgImage);
        //数据修改后重新加载初始化数据
        initSystem.loadData(request.getServletContext());
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

    /**
     * 初始化背景图片信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/init")
    @ResponseBody
    public Map<String,Object> initImg(String ids, HttpServletRequest request)throws Exception{
        String[] idStr = ids.split(",");
        BgImage tempObj = new BgImage();
        tempObj.setUpdateDate(new Date());
        for(int i =0;i<idStr.length;++i){
            Integer id = Integer.parseInt(idStr[i]);
            tempObj.setId(id);
            switch (id){
                case 2:
                    tempObj.setImgUrl("leaf.jpg");
                    tempObj.setName("美好的叶子");
                    break;
                case 3:
                    tempObj.setImgUrl("road.jpg");
                    tempObj.setName("高雅的路子");
                    break;
                case 4:
                    tempObj.setImgUrl("sea.jpg");
                    tempObj.setName("大海");
                    break;
                case 5:
                    tempObj.setImgUrl("shelter.jpg");
                    tempObj.setName("避难所");
                    break;
                case 6:
                    tempObj.setImgUrl("tree.jpg");
                    tempObj.setName("大树");
                    break;
                case 1:
                    tempObj.setImgUrl("bridge.jpg");
                    tempObj.setName("方便的木桥");
                    break;
            }
        }
        bgImageService.save(tempObj);
        //数据修改后重新加载初始化数据
        initSystem.loadData(request.getServletContext());
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }


    /**
     * 根据id查询背景图片实体
     * @return
     * @throws Exception
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Map<String,Object> findById(Integer id)throws Exception{
        BgImage bgImage = bgImageService.get(id);
        Map<String,Object> map = new HashMap<>();
        map.put("bgImage",bgImage);
        map.put("success",true);
        return map;
    }

    /**
     * 去往预览画面
     * @return
     * @throws Exception
     */
    @RequestMapping("/topreview")
    public ModelAndView topreview(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/bgPreview");
        return mav;
    }

    /**
     * 去往欢迎画面
     * @return
     * @throws Exception
     */
    @RequestMapping("/towelcom")
    public ModelAndView towelcom(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/welcom");
        return mav;
    }
}
