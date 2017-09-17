package com.shop.pojo;

import java.util.Date;

public class Level {
    private Integer id;

    private Integer userId;

    private Integer level;

    private Integer exp;

    private Date createTime;

    private Date updateTime;

    public Level(Integer id, Integer userId, Integer level, Integer exp, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.level = level;
        this.exp = exp;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Level() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}