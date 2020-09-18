package com.linka39.code07.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源类型实体类
 */
@Entity
@Table(name = "t_bgImages")
public class BgImage implements Serializable {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号

    @Column(length = 100)
    private String name;    //图片名称
    @Column(length = 1000)
    private String imgUrl;  //描述
    private Date updateDate;   //评论日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
