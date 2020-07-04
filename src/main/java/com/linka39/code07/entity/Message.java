package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户消息实体类
 */
@Entity
@Table(name = "t_message")
public class Message implements Serializable {
    @Id //设为主键
    @GeneratedValue(strategy=GenerationType.IDENTITY)//设置自动生成
    private Integer id;//编号

    @Column(length = 100)
    private String content;    //消息内容
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;//发布日期
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;  //所属用户

    private boolean isSee=false;//消息是否被查看

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSee() {
        return isSee;
    }

    public void setSee(boolean see) {
        isSee = see;
    }
}
