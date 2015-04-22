package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.AuditStatus;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mars on 15/3/13.
 * 医师文章
 */
@Entity
@Table(name = "doctor_article")
@Audited
public class DoctorArticle extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = -3529911534521896106L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 审核状态
     */
    @Enumerated
<<<<<<< HEAD
    private Status status;

    public enum Status {
        Auditing, Failure, Success
    }
=======
    private AuditStatus status;
>>>>>>> 0a1c025ac3861431dc65be54ae6eef41c5617b71

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishTime;

    /**
     * 拒绝理由
     */
    private String refusalReason;

    private String picPath;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
