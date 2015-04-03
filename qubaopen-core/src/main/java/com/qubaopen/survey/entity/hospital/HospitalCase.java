package com.qubaopen.survey.entity.hospital;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mars on 15/3/19.
 */
@Entity
@Table(name = "hospital_case")
@Audited
public class HospitalCase extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = -4518386692763546159L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;

    private String title;

    @Column(length = 10000)
    String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
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
