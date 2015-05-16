package com.qubaopen.survey.entity.hospital;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mars on 15/3/19.
 * 诊所文章
 */
@Entity
@Table(name = "hospital_article")
@Audited
public  class HospitalArticle extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = -634685690713811226L;

   @ManyToOne(fetch = FetchType.LAZY)

    private Hospital hospital;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

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
