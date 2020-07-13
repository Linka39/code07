package com.linka39.code07.quartzJob;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.linka39.code07.service.UserService;
import com.linka39.code07.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;


/**
 * 每天凌晨0点 重置用户所有签到信息
 * Spring内部定时器方法
 */
@Component
public class SignResetTask implements ServletContextListener{

	@Autowired
	private UserService userSerivce;
	@Autowired
	private RedisUtil<Integer> redisUtil;

	private static ServletContext application;

	//每天凌晨0点 重置用户所有签到信息
	@Scheduled(cron="0 0 0 * * ?")
	private void process(){
		application.setAttribute("signTotal", 0);
		redisUtil.set("signTotal", 0);
		userSerivce.updateAllSignInfo();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		application=sce.getServletContext();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
}
