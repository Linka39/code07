package com.linka39.code07.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片绝对地址与虚拟地址映射
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

	//文件磁盘图片url 映射
	//配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
      //可以实现数据与项目间的分离
      registry.addResourceHandler("/loginImages/thumbs/**").addResourceLocations("file:E:\\my_project\\code07\\slides\\thumbs\\");
    registry.addResourceHandler("/loginImages/**").addResourceLocations("file:E:\\my_project\\code07\\slides\\");
	registry.addResourceHandler("/image/**").addResourceLocations("file:E:\\my_project\\code07\\uploadImage\\");
	registry.addResourceHandler("/userImage/**").addResourceLocations("file:E:\\my_project\\code07\\userImage\\");

      //可以实现数据与项目间的分离 生产端
      /*registry.addResourceHandler("/loginImages/thumbs/**").addResourceLocations("file:/home/code07/slides/thumbs/");
     registry.addResourceHandler("/loginImages/**").addResourceLocations("file:/home/code07/slides/");
      registry.addResourceHandler("/image/**").addResourceLocations("file:/home/code07/uploadImage/");
      registry.addResourceHandler("/userImage/**").addResourceLocations("file:/home/code07/userImage/");*/
  }

}
