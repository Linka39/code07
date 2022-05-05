package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 敏感文章流水
 */
@Entity
@Table(name = "t_sensitive_article")
//数据序列化，方便在网络中存储
public class SensitiveArticle implements Serializable {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号//编号，同一资源文章的id可以有多条敏感文章资源，如此方便以后查看提交流水，归档后则删除此流水

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;//提交用户的id

    @OneToOne
    @JoinColumn(name = "articleId")
    private Article article;//提交的资源文章id

    @Column(length = 200)
    private String name;    //资源名称
    private Integer count;  //文章词数量 v-high[5000,+), high[3000,5000),
    private Integer sensitivecount;  //敏感词数/词数，v-high[0.2,+), high[0.1,0.2), med[0.05,0.1), low[0,0.05)
    @Column(length = 10)
    private String emotion; //敏感词情感分类，1,2, 3, 4, 5
    private Integer papers;  //用户发表文章数，用户在提交那一刻的审核通过的发表数量，low[0,5), med[5,30), high[30,+)
    private Integer points;   //用户积分，用户在提交那一刻所拥有的积分，low[0,10), med[10,50), high[50,+)
    @Column(length = 10)
    private String level;   //决策树给定的敏感词评级，four, three, two, one
    private Date uploadDate;   //文章提交时间
    @Lob//大文本模式
    @Column(columnDefinition = "longtext")
    private String content; //资源描述

    @Lob//大文本模式
    @Column(columnDefinition = "longtext")
    private String sensitiveWord; //提交处理后的敏感词词袋
    private Integer state; //敏感词文章状态 1-未审核 2-审核通过 3-审核不通过

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }



    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Integer getPapers() {
        return papers;
    }

    public void setPapers(Integer papers) {
        this.papers = papers;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSensitivecount() {
        return sensitivecount;
    }

    public void setSensitivecount(Integer sensitivecount) {
        this.sensitivecount = sensitivecount;
    }
}
