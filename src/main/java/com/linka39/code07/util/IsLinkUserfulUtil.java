package com.linka39.code07.util;

import org.quartz.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class IsLinkUserfulUtil {
    public static void main(String[] args) throws SchedulerException {
        String url_str = "https://pan.baidu.com/s/1WUzC_GcxP_4SYbhz0uTrug";
        if (TestUserful(url_str) == true) {
            System.out.println("该链接未失效！");
        } else {
            System.out.println("该链接已失效！");
        }
    }
    public static Boolean TestUserful(String url_str){
        Boolean isclosed = true; //标志是否失效，默认不失效
        try {
            URL url = new URL(url_str);//这里输入你要查询的链接
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String data = br.readLine();
            while (data != null) {
                data = br.readLine();
                sb.append(data);
            }
            br.close();
            isr.close();
            is.close();
            String finaldata = sb.toString();
            if (finaldata.contains("分享的文件已经被取消了") || finaldata.contains("该分享文件已过期") ||
            finaldata.contains("此链接分享内容可能因为涉及侵权") || finaldata.contains("你所访问的页面不存在了"))
                isclosed = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isclosed;
    }
}
