package com.linka39.code07.config;

import com.linka39.code07.util.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 邮件配置类
 * @author Administrator
 *
 */
@Configuration
public class MailConfig {

	/**
	 * 获取邮件发送实例
	 * @return
	 */
	@Bean
	//相当于
	//<beans>
	//    <bean id="mailSender" class="JavaMailSenderImpl"/>
	//</beans>
	public static JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");//指定用来发送Email的邮件服务器主机名
        mailSender.setPort(587);//默认端口，标准的SMTP端口
        mailSender.setUsername("1203440758@qq.com");//用户名
        mailSender.setPassword("qobpwviopuycbaad");//密码,输入smtp的授权码
        return mailSender;
	}
	//邮箱测试
	public static void main(String[] args) {
		String mailCode = StringUtil.genSixRandomNum();
		System.out.println(mailCode);
		SimpleMailMessage message= new SimpleMailMessage();
		message.setFrom("1203440758@qq.com");//设置发件人
		message.setTo("kawayijiujielx@163.com");//设置收件人
		message.setSubject("找回密码，测试内容");//主题
		message.setText("验证码为："+mailCode);
		JavaMailSender sender = mailSender();
		sender.send(message);
	}
}
