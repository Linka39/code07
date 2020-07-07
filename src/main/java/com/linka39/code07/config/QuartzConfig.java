package com.linka39.code07.config;

import org.springframework.context.annotation.Configuration;

/**
 * 邮件配置类
 * @author Administrator
 *
 */
@Configuration
public class QuartzConfig {
	/*@Autowired
	QuartzJobFactory quartzJobFactory;//注入我们自己的factory，防止无法在job中注入service层

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setJobFactory(quartzJobFactory);
		return schedulerFactoryBean;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));
		//在quartz.properties中的属性被读取并注入后再初始化对象
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	*//*
	 * quartz初始化监听器
	 *//*
	@Bean
	public QuartzInitializerListener executorListener() {
		return new QuartzInitializerListener();
	}

	*//*
	 * 通过SchedulerFactoryBean获取Scheduler的实例
	 *//*
	@Bean(name = "MyScheduler")
	public Scheduler MyScheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}*/
}
