package com.linka39.code07.init;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.entity.BgImage;
import com.linka39.code07.entity.Link;
import com.linka39.code07.service.ArcTypeService;
import com.linka39.code07.service.BgImageService;
import com.linka39.code07.service.LinkService;
import com.linka39.code07.util.RedisUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化加载数据
 */
//纳入Spring的生命周期管理
@Component("initSystem")
public class InitSystem implements ServletContextListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    public static Map<Integer,ArcType> arcTypeMap = new HashMap<Integer,ArcType>();
    @Autowired
    private RedisUtil<Integer> redisUtil;
    /**
     * 加载数据到application缓存中
     * @param application
     */
    public void loadData(ServletContext application){
        //通过Service注解的名字来获取Service
        ArcTypeService arcTypeService = (ArcTypeService) applicationContext.getBean("arcTypeService");
        LinkService linkService = (LinkService) applicationContext.getBean("linkService");
        BgImageService bgImageService = (BgImageService) applicationContext.getBean("bgImageService");
        //在上下文启动时初始化一个系统变量
        List<BgImage> allBgImageList = bgImageService.listAll(Sort.Direction.ASC,"id");
        List<ArcType> allArcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        List<Link> allLinkList = linkService.listAll(Sort.Direction.ASC,"sort");
        for(ArcType arcType:allArcTypeList){
            arcTypeMap.put(arcType.getId(),arcType);
        }
        application.setAttribute("allBgImageList",allBgImageList);
        application.setAttribute("allArcTypeList",allArcTypeList);
        application.setAttribute("allLinkList",allLinkList);
        if(redisUtil.get("signTotal")!=null){
            //项目一启动就设置
            Integer signTotal = (Integer) redisUtil.get("signTotal");
            application.setAttribute("signTotal",signTotal);
        }else{
            redisUtil.set("signTotal",0);
            application.setAttribute("signTotal",0);
        }
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //监听获取当前的域
        this.loadData(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //application为整个项目的域
        this.applicationContext = applicationContext;
    }
}
