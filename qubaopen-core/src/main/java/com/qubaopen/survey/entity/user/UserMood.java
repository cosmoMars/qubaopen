package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 用户 每日心情表
 */
@Entity
@Table(name = "user_mood")
@Audited
public class UserMood extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 3241951749351437205L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;

	@Enumerated
	private MoodType moodType;

	/**
	 * 最后修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTime = new Date();
	
	/**
	 * 留言
	 */
	private String message;

	/**
	 * 签到心情类型 得瑟 开心 偷笑 无聊 纠结 怕怕 伤心 滚粗
	 * 
	 */
	public enum MoodType {
		Flaunt, Happy, Giggle, Bored, Kink, Afraid, Sad, Getout
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MoodType getMoodType() {
		return moodType;
	}

	public void setMoodType(MoodType moodType) {
		this.moodType = moodType;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
