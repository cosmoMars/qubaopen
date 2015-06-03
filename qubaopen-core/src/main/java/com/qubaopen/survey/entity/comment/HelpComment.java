package com.qubaopen.survey.entity.comment;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

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

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	/**
	 * 诊所
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	@Column(columnDefinition = "TEXT")
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

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
