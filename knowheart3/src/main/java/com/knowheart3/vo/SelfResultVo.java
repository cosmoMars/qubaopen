package com.knowheart3.vo;

import java.util.Date;

/**
 * Created by mars on 15/3/30.
 */
public class SelfResultVo {

    /**
     * 用户答题答案
     */
    private String suqId;

    /**
     * 自测id
     */
    private String selfId;

    /**
     * 自测组id
     */
    private String groupId;

    /**
     * 自测标题
     */
    private String selfTitle;

    /**
     * 自测组名称
     */
    private String groupName;

    /**
     * 自测组内容
     */
    private String groupContent;

    /**
     * 发现时间
     */
    private Date ddTime;

    /**
     * 完成时间
     */
    private Date suqTime;

    /**
     * 是否完成
     */
    private boolean done;

    public String getSuqId() {
        return suqId;
    }

    public void setSuqId(String suqId) {
        this.suqId = suqId;
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSelfTitle() {
        return selfTitle;
    }

    public void setSelfTitle(String selfTitle) {
        this.selfTitle = selfTitle;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupContent() {
        return groupContent;
    }

    public void setGroupContent(String groupContent) {
        this.groupContent = groupContent;
    }

    public Date getDdTime() {
        return ddTime;
    }

    public void setDdTime(Date ddTime) {
        this.ddTime = ddTime;
    }

    public Date getSuqTime() {
        return suqTime;
    }

    public void setSuqTime(Date suqTime) {
        this.suqTime = suqTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
