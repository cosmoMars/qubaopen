package com.qubaopen.survey.entity.comment;

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

/**
 * @author mars
 * 求助评论
 */
@Entity
@Table(name = "help_comment")
@Audited
public class HelpComment extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2704745709905323464L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Help help;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public Help getHelp() {
		return help;
	}

	public void setHelp(Help help) {
		this.help = help;
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
