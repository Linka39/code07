package com.linka39.code07.controller;

import com.google.gson.Gson;
import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.entity.VaptchaMessage;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.UserService;
import com.linka39.code07.util.CryptographyUtil;
import com.linka39.code07.util.PageUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 首页url控制层
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
