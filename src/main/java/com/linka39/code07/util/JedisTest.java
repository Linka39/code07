package com.linka39.code07.util;

import redis.clients.jedis.Jedis;

public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis=new Jedis("39.106.83.5",6379); // 创建客户端 设置IP和端口
        jedis.auth("1111"); // 设置密码
        jedis.set("name", "过问"); // 设置值
        String value=jedis.get("name"); // 获取值
        System.out.println(value+"\n"+jedis.get("article_12"));
        jedis.close(); // 释放连接资源
    }
}
