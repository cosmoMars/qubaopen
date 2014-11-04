package com.qubaopen.survey.entity.reward;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 兑奖活动 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "REWARD_ACTIVITY")
@Audited
public class RewardActivity extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 3737396417460255332L;

	/**
	 * 奖品类型
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reward_type_id")
	private RewardType rewardType;

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
	 * 兑奖活动状态 0 未上线 1 上线 2 结束 3自动活动
	 */
	@Enumerated
	private Status status;

	/**
	 * OFFLINE 0 未上线, ONLINE 1 上线, END 2 结束  3自动活动
	 */
	private enum Status {
		OFFLINE, ONLINE, END, AUTO
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

	private String picPath;

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
