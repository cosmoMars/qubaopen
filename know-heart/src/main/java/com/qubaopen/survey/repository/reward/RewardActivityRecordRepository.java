package com.qubaopen.survey.repository.reward;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.RewardActivity;
import com.qubaopen.survey.entity.reward.RewardActivityRecord;
import com.qubaopen.survey.entity.user.User;

public interface RewardActivityRecordRepository extends MyRepository<RewardActivityRecord, Long> {

	long countByUser(User user);

	List<RewardActivityRecord> findAllByUser(User user);
	
	@Query("from RewardActivityRecord rar where rar.user = :user and rar.rewardActivity in (:activitys)")
	List<RewardActivityRecord> findByUserWithActivity(@Param("user") User user, @Param("activitys") List<RewardActivity> activitys);

}
