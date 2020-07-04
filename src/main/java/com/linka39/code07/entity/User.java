package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 */
@Entity
//前后端都需要进行校验，前端是为了减少后端压力，后端是为了防止post请求发送校验
@Table(name = "t_user")
public class User implements Serializable {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id; //编号

    private Integer messageCount;//未查看消息记录数

    @NotEmpty(message = "请输入用户名！")
    @Column(length = 100)
    private String userName; //用户名

    @NotEmpty(message = "请输入密码！")
    @Column(length = 100)
    private String password; //密码

    @Email(message = "邮箱地址格式有误！")
    @NotEmpty(message = "请输入邮箱地址！")
    @Column(length = 100)
    private String email;   //验证邮箱
    @Column(length = 100)
    private String imageName;   //用户头像地址
    private Integer points=0;   //积分，新创建默认为0
    private Boolean isVip=false;    //是否为Vip
    private boolean isOff=false;    //是否被封禁
    private String roleName="会员";    //角色名称，会员 管理员两种，默认会员
    private Date registerDate;  //用户注册日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {
        isVip = vip;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }
}
