package com.linka39.code07.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
@Table(name = "t_quartzParam")
public class QuartzParam {
    @Id //设为主键
    @GeneratedValue(strategy=GenerationType.IDENTITY)//设置自动生成
    private Integer id;//编号
    @Size(min = 1, message = "类(名)不能为空")
    @NotNull(message = "类(名)不能为空")
    private String className;
    @Size(min = 1, message = "任务(名)不能为空")
    @NotNull(message = "任务(名)不能为空")
    private String jobName;
    @Size(min = 1, message = "任务(名)不能为空")
    @NotNull(message = "任务(名)不能为空")
    private String jobGroup;
    @Size(min = 1, message = "触发器(名)不能为空")
    @NotNull(message = "触发器(名)不能为空")
    private String tgName;
    @Size(min = 1, message = "触发器(组)不能为空")
    @NotNull(message = "触发器(组)不能为空")
    private String tgGroup;
    @Size(min = 1, message = "触发器(规则)不能为空")
    @NotNull(message = "触发器(规则)不能为空")
    private String trigger;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTgName() {
        return tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName;
    }

    public String getTgGroup() {
        return tgGroup;
    }

    public void setTgGroup(String tgGroup) {
        this.tgGroup = tgGroup;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }
}
