package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户下载实体类
 */
@Entity
@Table(name = "t_userDownload")
public class UserDownload {
    @Id //设为主键
    @GeneratedValue(strategy=GenerationType.IDENTITY)//设置自动生成
    private Integer id;//编号

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;//所属用户

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;//下载资源类别

    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadDate;//下载日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }
}
