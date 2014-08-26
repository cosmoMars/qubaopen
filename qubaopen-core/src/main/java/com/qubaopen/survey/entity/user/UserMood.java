package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户 每日心情表
 */
@Entity
@Table(name = "user_mood")
@Audited
public class UserMood extends AbstractBaseEntity<Long> {


	private static final long serialVersionUID = 3241951749351437205L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated
	private MoodType moodType;

	/**
	 * 签到心情类型 0郁闷 1无聊 2得瑟 3丢人 4求安慰 5纠结
	 */
	public enum MoodType {
		YUMEN, WULIAO, DESE, DIUREN, QIUANWEI, JIUJIE
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

}
