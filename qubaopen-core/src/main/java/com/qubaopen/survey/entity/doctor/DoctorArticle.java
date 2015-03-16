package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    private String content;

    private Date createTime;

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
}
