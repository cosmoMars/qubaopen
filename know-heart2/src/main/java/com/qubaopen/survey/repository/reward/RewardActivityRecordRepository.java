package com.qubaopen.survey.repository.reward;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.RewardActivityRecord;
import com.qubaopen.survey.entity.user.User;

public interface RewardActivityRecordRepository extends MyRepository<RewardActivityRecord, Long> {

	long countByUser(User user);

	List<RewardActivityRecord> findAllByUser(User user);

}
