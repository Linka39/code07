package com.linka39.code07.entity;

import javax.persistence.*;

/**
 * 友情链接实体类
 */
@Entity
@Table(name = "t_link")
public class Link {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号

    @Column(length = 100)
    private String name;    //名称
    @Column(length=500)
    private String url; // 链接地址
    private Integer sort;   //排序（从小到大排序）

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
