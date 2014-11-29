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
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "help")
@Audited
public class Help extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2741484760819353602L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	private String content;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
