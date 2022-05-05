package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 敏感词实体类
 */
@Entity
@Table(name = "t_sensitive_word")
public class SensitiveWord implements Serializable {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号

    @Column(length = 100)
    private String word;    //敏感词
    private Integer emotion;  //敏感词类型
    private Date updateDate;   //敏感词更新日期

    private Integer emotionWeight;  //敏感词权重

    public Integer getEmotionWeight() {
        return emotionWeight;
    }

    public void setEmotionWeight(Integer emotionWeight) {
        this.emotionWeight = emotionWeight;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;//敏感词登记用户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getEmotion() {
        return emotion;
    }

    public void setEmotion(Integer emotion) {
        this.emotion = emotion;
    }
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
