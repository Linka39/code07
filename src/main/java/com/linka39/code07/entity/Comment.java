package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * 评论实体类
 */
@Entity
@Table(name = "t_comment")
public class Comment {
    @Id //设为主键
    @GeneratedValue(strategy=GenerationType.IDENTITY)//设置自动生成
    private Integer id;//编号

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;//评论人

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;//评论帖子

    @Column(length = 1000)
    private String content;    //名称

    private Date commentDate;   //评论日期
    //防止灌水
    private Integer state;//评论审核状态 0-未审核 1-通过 2-未通过

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
