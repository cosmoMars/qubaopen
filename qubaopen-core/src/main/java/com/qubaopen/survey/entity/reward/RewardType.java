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
@Table(name = "reward_type")
@Audited
public class RewardType extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6627275265710152659L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 奖品等级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reward_level_id")
	private RewardLevel rewardLevel;

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

	public RewardLevel getRewardLevel() {
		return rewardLevel;
	}

	public void setRewardLevel(RewardLevel rewardLevel) {
		this.rewardLevel = rewardLevel;
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
