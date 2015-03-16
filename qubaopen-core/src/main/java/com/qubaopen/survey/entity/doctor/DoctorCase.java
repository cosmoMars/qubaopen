package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mars on 15/3/13.
 * 医师案例
 */
@Entity
@Table(name = "doctor_case")
@Audited
public class DoctorCase extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 5280331283467828022L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    private String title;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
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
