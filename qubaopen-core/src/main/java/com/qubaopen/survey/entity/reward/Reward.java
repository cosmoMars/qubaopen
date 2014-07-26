package com.qubaopen.survey.entity.reward;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 奖品表 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "REWARD")
@Audited
public class Reward extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1786460720258232346L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 奖品类型 例：兑奖券类 充值卡类 现金红包等
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reward_type_id")
	private RewardType rewardType;

	/**
	 * 库存剩余数量
	 */
	private int remainAmount;

	/**
	 * 描述
	 */
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
	}

	public int getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(int remainAmount) {
		this.remainAmount = remainAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
