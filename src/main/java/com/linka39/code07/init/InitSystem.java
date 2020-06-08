package com.linka39.code07.init;

import com.linka39.code07.entity.ArcType;
import com.linka39.code07.service.ArcTypeService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * 初始化加载数据
 */
//纳入Spring的生命周期管理
@Component("initSystem")
public class InitSystem implements ServletContextListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 加载数据到application缓存中
     * @param application
     */
    public void loadData(ServletContext application){
        //通过Service注解的名字来获取Service
        ArcTypeService arcTypeService = (ArcTypeService) applicationContext.getBean("arcTypeService");
        List<ArcType> allArcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        application.setAttribute("allArcTypeList",allArcTypeList);
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
