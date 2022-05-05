package com.linka39.code07.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 字典代码实体类
 */
@Entity
@Table(name = "t_dic")
public class Dic implements Serializable {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号

    @Column(length = 50)
    private String zddm;    //字典代码名称
    @Column(length = 100)
    private String zdmc;    //字典代码名称
    @Column(length = 1000)
    private String zdz;  //字典代码值
    @Column(length = 1000)
    private String zdnote;  //字典代码注解

    public String getZddm() {
        return zddm;
    }

    public void setZddm(String zddm) {
        this.zddm = zddm;
    }

    public String getZdz() {
        return zdz;
    }

    public void setZdz(String zdz) {
        this.zdz = zdz;
    }

    public String getZdnote() {
        return zdnote;
    }

    public void setZdnote(String zdnote) {
        this.zdnote = zdnote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZdmc() {
        return zdmc;
    }

    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }
}
