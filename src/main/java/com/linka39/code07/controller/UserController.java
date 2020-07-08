package com.linka39.code07.controller;

import ch.qos.logback.core.util.FileUtil;
import com.google.gson.Gson;
import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.entity.VaptchaMessage;
import com.linka39.code07.service.MessageService;
import com.linka39.code07.service.UserService;
import com.linka39.code07.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * 首页url控制层
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisUtil<Integer> redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    //@Autowired//不能用Autowired装配，其是按类型来的
    @Resource
    private JavaMailSender mailSender;

    @Value("${userImageFilePath}")
    private String userImageFilePath;

    /**
     * 用户登陆
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @ResponseBody//要加入ResponseBody,或RestController，将返回的信息转换为json格式
    //若实体类的参数 与ajax内请求中数据里""内的参数名相同，可直接用实体类代替
    public Map<String,Object> login(@Valid User user, BindingResult bindingResult, String vaptcha_token, HttpServletRequest request) throws Exception{
        Map<String,Object> map = new HashMap<> ();
        if(StringUtils.isEmpty(user.getUserName().trim())){
            map.put("success",false);
            map.put("errorInfo","请输入用户名！");
        }else if(StringUtils.isEmpty(user.getPassword().trim())){
            map.put("success",false);
            map.put("errorInfo","请输入密码！");
        } else if(vaptchaCheck(vaptcha_token,request.getRemoteHost())==false) {
            map.put("success", false);
            map.put("errorInfo", "人机验证失败！");
        }else{//登陆成功
            Subject subject = SecurityUtils.getSubject();
            //加密，将字段转换为token,subject相当于shiro的一个门，确认角色的身份
            //两个参数,userName,passWord
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            try{
                subject.login(token);
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                User currentUser = userService.findByUserName(userName);
                if(currentUser.isOff()){
                    map.put("success", false);
                    map.put("errorInfo", "该用户已经被封禁，请联系管理员！");
                    //不管try,catch返回值如何，finaly{}程序块的程序一定会执行
                    //logout()，底层会清空session
                    subject.logout();
                }else{
                    Integer messageCount = messageService.getCountByUserId(currentUser.getId());
                    currentUser.setMessageCount(messageCount);
                    map.put("success",true);
                    request.getSession().setAttribute("currentUser",currentUser);
                }

            }catch (Exception e){
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "用户名或密码错误！");
            }

        }
        return map;
    }

    /**
     * 用户注册
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    @ResponseBody//要加入ResponseBody,或RestController，将返回的信息转换为json格式
    //若实体类的参数 与ajax内请求中数据里""内的参数名相同，可直接用实体类代替
    public Map<String,Object> register(@Valid User user, BindingResult bindingResult, String vaptcha_token, ServletRequest request) throws Exception{
        Map<String,Object> map = new HashMap<> ();
        if(bindingResult.hasErrors()){
            map.put("success",false);
            map.put("errorInfo",bindingResult.getFieldError().getDefaultMessage());
        }else if(userService.findByUserName(user.getUserName())!=null){
            map.put("success", false);
            map.put("errorInfo", "用户名已存在，请更换！");
        }else if(userService.findByEmail(user.getEmail())!=null) {
            map.put("success", false);
            map.put("errorInfo", "邮箱已存在，请更换！");
        } else if(vaptchaCheck(vaptcha_token,request.getRemoteHost())==false) {
            map.put("success", false);
            map.put("errorInfo", "人机验证失败！");
        } else{
            user.setPassword(CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            user.setRegisterDate(new Date());
            user.setImageName("default.jpg");
            userService.save(user);
            map.put("success",true);
        }
        return map;
    }
    /**
     * 验证码发送邮箱
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("/sendEmail")
    @ResponseBody//要加入ResponseBody,或RestController，将返回的信息转换为json格式
    public Map<String,Object> sendEmail(String email, HttpSession session)throws Exception{
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(email)){
            map.put("success", false);
            map.put("errorInfo", "邮箱不存在！");
            return map;
        }
        User user= userService.findByEmail(email);
        if(user==null) {//select结果集为空
            map.put("success", false);
            map.put("errorInfo", "用户邮箱错误或不存在！");
            return map;
        }
        //要先在邮箱里开启SMTP客户端服务
        String mailCode = StringUtil.genSixRandomNum();
        System.out.println(mailCode);
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("1203440758@qq.com");//设置发件人
        message.setTo(email);
        message.setSubject("linka39官网，找回密码");//主题
        message.setText("验证码为："+mailCode);
        mailSender.send(message);

        //验证码，用户id需要存到session中,便于后面的验证
        session.setAttribute("mailCode",mailCode);
        session.setAttribute("userId",user.getId());
        map.put("success", true);
        return map;
    }

    /**
     * 邮件验证码判断，重置
     * @param yzm
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkYzm")
    @ResponseBody
    public Map<String,Object> checkYzm(String yzm,HttpSession session)throws Exception{
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isEmpty(yzm)){
            map.put("success", false);
            map.put("errorInfo", "验证码不能为空！");
            return map;
        }
        String mailCode = (String) session.getAttribute("mailCode");
        Integer userId = (Integer) session.getAttribute("userId");
        if(!yzm.equals(mailCode)){
            map.put("success", false);
            map.put("errorInfo", "验证码错误！");
            return map;
        }
        User user = userService.findById(userId);
        user.setPassword(CryptographyUtil.md5("111",CryptographyUtil.SALT));
        userService.save(user);
        map.put("success", true);
        return map;
    }

    /**
     * 客户密码修改
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public Map<String,Object> modifyPassword(String oldpassword,String password,HttpSession session)throws Exception{
        User user =(User) session.getAttribute("currentUser");
        Map<String,Object> map = new HashMap<>();
        if(!user.getPassword().equals(CryptographyUtil.md5(oldpassword,CryptographyUtil.SALT))){
            map.put("success", false);
            map.put("errorInfo", "原密码错误！");
            return map;
        }
        User oldUser=userService.findById(user.getId());
        oldUser.setPassword(CryptographyUtil.md5(password,CryptographyUtil.SALT));
        //按id执行响应的update语句
        userService.save(oldUser);
        map.put("success", true);
        return map;
    }

    /**
     * 实现图片上传
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public Map<String,Object> uploadImage(MultipartFile file, HttpSession session)throws Exception{
        Map<String,Object> map = new HashMap<>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename();//获取原文件名
            String suffixName= fileName.substring(fileName.lastIndexOf(".")); //获取后缀
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;    //新名字
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(userImageFilePath+newFileName));//拷贝到新文件
            map.put("code",0);
            map.put("msg","上传成功");
            Map<String,Object> map2=new HashMap<>();
            //layui指定上传格式，data为标签中对应的属性
            map2.put("src","/userImage/"+newFileName);
            map2.put("title",newFileName);
            map.put("data",map2);

            User user =(User) session.getAttribute("currentUser");
            user.setImageName(newFileName);
            userService.save(user);
            session.setAttribute("currentUser",user);
        }
        return map;
    }

    /**
     * 人机验证结果判断
     * @param token
     * @param ip
     * @return
     * @throws Exception
     */
    private boolean vaptchaCheck(String token,String ip)throws Exception{
        String body="";
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //Java像url发送Post请求。在发送post请求时用该list<NameValuePair>来存放参数
        HttpPost httpPost=new HttpPost("http://0.vaptcha.com/verify");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", "5ee0b346e7b3ff69253970f6"));
        nvps.add(new BasicNameValuePair("secretkey", "8e7a9d4970dc4b18aecb9a4a7d039ca4"));
        nvps.add(new BasicNameValuePair("scene", "0"));
        nvps.add(new BasicNameValuePair("token", token));
        nvps.add(new BasicNameValuePair("ip", ip));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        //获取响应数据
        CloseableHttpResponse r = httpClient.execute(httpPost);
        HttpEntity entity = r.getEntity();

        if(entity!=null){
            //将响应数据题转换为json格式
            /*token-error	token错误
            token-expired	token过期，token三分钟有效期*/
            body = EntityUtils.toString(entity, "utf-8");
            System.out.println(body);
        }
        r.close();
        httpClient.close();
        Gson gson = new Gson();
        //json数据转换为实体
        VaptchaMessage message=gson.fromJson(body, VaptchaMessage.class);
        if(message.getSuccess()==1){
            return true;
        }else{
            return false;
        }

    }

    @ResponseBody
    @RequestMapping("/isVip")
    public Boolean isVip(HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        return user.getVip();
    }
    @ResponseBody
    @RequestMapping("/sign")
    //HttpServletRequest获取application
    public Map<String,Object> sign(HttpSession session,HttpServletRequest request)throws Exception{
        User oldUser = (User) session.getAttribute("currentUser");
        Map<String,Object> map = new HashMap<>();
        if(oldUser==null){
            map.put("success",false);
            map.put("errorInfo","当前客户未登录");
            return map;
        }
        if(oldUser.getSign()){
            map.put("success",false);
            map.put("errorInfo","你已经签过到了，不能重复签呀");
            return map;
        }
        ServletContext application=request.getServletContext();
        Integer signTotal = (Integer) redisUtil.get("signTotal");
        redisUtil.set("signTotal", signTotal+1);
        application.setAttribute("signTotal", signTotal+1);
        //信息更新到数据库
        User user = userService.findById(oldUser.getId());
        user.setSign(true);
        user.setSignTime(new Date());
        user.setSignSort(signTotal+1);
        user.setPoints(user.getPoints()+1);
        userService.save(user);
        //用户缓存更新
        session.setAttribute("currentUser", user);
        map.put("success", true);
        return map;
    }
}
