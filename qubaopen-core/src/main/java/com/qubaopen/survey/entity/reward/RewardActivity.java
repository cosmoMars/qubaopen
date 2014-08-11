package com.qubaopen.survey.entity.reward;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 兑奖活动 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "REWARD_ACTIVITY")
@Audited
public class RewardActivity extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -9097829089069118323L;

	/**
	 * 奖品
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reward_id")
	private Reward reward;

	/**
	 * 活动标题
	 */
	private String title;

	/**
	 * 活动详细介绍
	 */
	private String content;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 消耗金币
	 */
	private int requireGold;

	/**
	 * 兑奖活动状态 0 未上线 1 上线 2 结束
	 */
	@Enumerated
	private Status status;

	/**
	 * OFFLINE 0 未上线, ONLINE 1 上线, END 2 结束
	 */
	private enum Status {
		OFFLINE, ONLINE, END
	}

	/**
	 * 活动总共可被参与的次数限制
	 */
	private int totalCountLimit;

	/**
	 * 活动目前参与次数
	 */
	private int currentCount;

	/**
	 * 每人可以参与的次数限制 0为不限制
	 */
	private int eachCountLimit;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getRequireGold() {
		return requireGold;
	}

	public void setRequireGold(int requireGold) {
		this.requireGold = requireGold;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getTotalCountLimit() {
		return totalCountLimit;
	}

	public void setTotalCountLimit(int totalCountLimit) {
		this.totalCountLimit = totalCountLimit;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public int getEachCountLimit() {
		return eachCountLimit;
	}

	public void setEachCountLimit(int eachCountLimit) {
		this.eachCountLimit = eachCountLimit;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

}
