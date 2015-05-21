package com.qubaopen.survey.entity.comment;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mars
 * 求助主题
 */
@Entity
@Table(name = "help")
@Audited
public class Help extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2741484760819353602L;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private User user;

	/**
	 * 发布时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	/**
	 * 内容
	 */
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
