package com.linka39.code07.entity;

import javax.persistence.*;

/**
 * 资源类型实体类
 */
@Entity
@Table(name = "t_arcType")
public class ArcType {
    @Id //设为主键
    @GeneratedValue//设置自动生成
    private Integer id;//编号

    @Column(length = 100)
    private String name;    //类别名称
    @Column(length = 1000)
    private String remark;  //描述
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
