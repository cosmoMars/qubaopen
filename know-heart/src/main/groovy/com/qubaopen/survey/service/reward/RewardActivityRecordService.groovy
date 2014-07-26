package com.qubaopen.survey.service.reward

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.entity.reward.RewardActivityRecord
import com.qubaopen.survey.entity.reward.RewardAssignRecord
import com.qubaopen.survey.entity.user.UserGold
import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.reward.RewardActivityRecordRepository
import com.qubaopen.survey.repository.reward.RewardAssignRecordRepository
import com.qubaopen.survey.repository.reward.RewardInfoRepository
import com.qubaopen.survey.repository.user.UserGoldRepository

@Service
public class RewardActivityRecordService {

	@Autowired
	UserGoldRepository userGoldRepository

	@Autowired
	RewardInfoRepository rewardInfoRepository

	@Autowired
	RewardAssignRecordRepository rewardAssignRecordRepository

	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

	/**
	 * 扣除参与活动金币，活动＋1，并保存用户金币，以及活动记录
	 * @param rewardActivity 奖品活动
	 * @param userGold 用户金币
	 * @param userReceiveAddress 收货地址
	 * @return
	 */
	@Transactional
	saveRecordAndGold(RewardActivity rewardActivity, UserGold userGold, UserReceiveAddress userReceiveAddress ) {

		userGold.currentGold = userGold.currentGold - rewardActivity.requireGold
		userGoldRepository.save(userGold)

		rewardActivity.currentCount++

		def user = userGold.user,
			rewardInfo = rewardInfoRepository.findByReward(rewardActivity.reward),
			rewardActivityRecord = new RewardActivityRecord(
				user : user,
				rewardActivity : rewardActivity,
				userReceiveAddress : userReceiveAddress,
				rewardInfo : rewardInfo,
				status : RewardActivityRecord.Status.DELIVERING
			)

		rewardActivityRecordRepository.save(rewardActivityRecord)
	}

	/**
	 * 修改奖品记录状态，保存分发记录
	 * @param activityRecord 奖品记录
	 * @return
	 */
	@Transactional
	modifyRecord(RewardActivityRecord activityRecord) {

		def reward = activityRecord.rewardActivity.reward,
			rewardInfo = rewardInfoRepository.findByReward(reward),
			rewardAssignRecord = new RewardAssignRecord(
				rewardActivityRecord : activityRecord,
				rewardInfo : rewardInfo
			)

		def record = rewardActivityRecordRepository.save(activityRecord)
		rewardAssignRecordRepository.save(rewardAssignRecord)
		record
	}
}
