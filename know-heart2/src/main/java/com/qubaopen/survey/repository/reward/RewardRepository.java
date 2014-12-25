package com.qubaopen.survey.repository.reward;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.Reward;
import com.qubaopen.survey.entity.reward.RewardType;

public interface RewardRepository extends MyRepository<Reward, Long> {

//	Reward findByRewardType(RewardType rewardType);

	List<Reward> findByRewardTypeAndUsed(RewardType rewardType, boolean used);
}
