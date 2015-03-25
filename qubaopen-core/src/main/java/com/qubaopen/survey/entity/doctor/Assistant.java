package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mars on 15/3/25.
 */
@Entity
@Table(name = "assistant")
@Audited
public class Assistant extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 7453475801025478508L;

    private String name;

    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
