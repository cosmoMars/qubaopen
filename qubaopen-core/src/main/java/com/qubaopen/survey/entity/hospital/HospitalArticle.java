package com.qubaopen.survey.entity.hospital;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mars on 15/3/19.
 * 诊所文章
 */
@Entity
@Table(name = "hosptial_article")
@Audited
public  class HospitalArticle extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = -634685690713811226L;

   @ManyToOne(fetch = FetchType.LAZY)

    private Hospital hospital;

    private String title;

    private String content;

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
