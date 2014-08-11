package com.qubaopen.survey.entity.reward;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 奖品分发记录 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "reward_assign_record")
@Audited
public class RewardAssignRecord extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -2180641333847011629L;

	@ManyToOne(fetch = FetchType.LAZY)
	private RewardActivityRecord rewardActivityRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	private Reward reward;

	public RewardActivityRecord getRewardActivityRecord() {
		return rewardActivityRecord;
	}

	public void setRewardActivityRecord(RewardActivityRecord rewardActivityRecord) {
		this.rewardActivityRecord = rewardActivityRecord;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
