package com.qubaopen.survey.entity.user;

import com.qubaopen.survey.entity.doctor.Doctor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by mars on 15/5/21.
 */
@Entity
@Table
@Audited
public class UserAuthorization extends AbstractPersistable<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Doctor doctor;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
