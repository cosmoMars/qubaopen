package com.qubaopen.survey.entity.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mars on 15/4/1.
 */
public class HelpCommentVo implements Serializable {

    private static final long serialVersionUID = -1618164244784582663L;

    private String commentId;

    private String doctorId;

    private String doctorName;

    private String hospitalId;

    private String hospitalName;

    private String doctorPath;

    private String hospitalPath;

    private String commentContent;

    private Date commentTime;

    private int gSize;

    private String userId;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDoctorPath() {
        return doctorPath;
    }

    public void setDoctorPath(String doctorPath) {
        this.doctorPath = doctorPath;
    }

    public String getHospitalPath() {
        return hospitalPath;
    }

    public void setHospitalPath(String hospitalPath) {
        this.hospitalPath = hospitalPath;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public int getgSize() {
        return gSize;
    }

    public void setgSize(int gSize) {
        this.gSize = gSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
