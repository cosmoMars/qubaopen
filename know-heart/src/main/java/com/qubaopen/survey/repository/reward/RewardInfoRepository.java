package com.qubaopen.survey.repository.reward;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.Reward;
import com.qubaopen.survey.entity.reward.RewardInfo;

public interface RewardInfoRepository extends MyRepository<RewardInfo, Long> {

	RewardInfo findByReward(Reward reward);
}
