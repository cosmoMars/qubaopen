package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Doctor;

@Entity
@Table(name = "user_help_comment")
@Audited
public class UserHelpComment extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2704745709905323464L;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserHelp userHelp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;
	
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public UserHelp getUserHelp() {
		return userHelp;
	}

	public void setUserHelp(UserHelp userHelp) {
		this.userHelp = userHelp;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
