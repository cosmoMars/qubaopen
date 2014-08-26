package com.qubaopen.survey.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


/**
 * @author mars 用户 每日心情表
 */
@Entity
@Table(name = "user_mood")
@Audited
public class UserMood implements Serializable{


	private static final long serialVersionUID = 3241951749351437205L;

	
	@Id
	private Long id;

	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	@Enumerated
	private MoodType moodType=MoodType.NONE;
	
	
	/**
	 * 最后修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTime=new Date();
	
	
	/**
	 * 签到心情类型  0无心情 1郁闷 2无聊 3得瑟 4丢人 5求安慰 6纠结
	 */
	public enum MoodType {
		NONE,YUMEN, WULIAO, DESE, DIUREN, QIUANWEI, JIUJIE
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
