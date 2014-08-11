package com.qubaopen.survey.service.reward

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.entity.reward.RewardActivityRecord
import com.qubaopen.survey.entity.reward.RewardAssignRecord
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserGold
import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.reward.RewardActivityRecordRepository
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.repository.reward.RewardAssignRecordRepository
import com.qubaopen.survey.repository.reward.RewardRepository
import com.qubaopen.survey.repository.user.UserGoldRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.utils.DateCommons

@Service
public class RewardActivityRecordService {

	@Autowired
	UserGoldRepository userGoldRepository

	@Autowired
	RewardRepository rewardRepository

	@Autowired
	RewardAssignRecordRepository rewardAssignRecordRepository

	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

	@Autowired
	RewardActivityRepository rewardActivityRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository


	/**
	 * 兑换奖品
	 * @param userId
	 * @param activityId
	 * @param addressId
	 * @return
	 */
	@Transactional
	exchangeReward(long userId, long activityId, long addressId) {

		def rewardActivity = rewardActivityRepository.findOne(activityId)

		if (rewardActivity != null && rewardActivity.status != RewardActivity.Status.ONLINE) {
			return '{"success": 0, "message": "err301"}'
		}

		if (rewardActivity.currentCount >= rewardActivity.totalCountLimit) {
			return '{"success": 0, "message": "err302"}'
		}

		def user = new User(id : userId),
			activityCount = rewardActivityRecordRepository.countByUser(user)

		if (rewardActivity.eachCountLimit != 0 && activityCount > rewardActivity.eachCountLimit) {
			return '{"success": 0, "message": "err303"}'
		}

		def userGold = userGoldRepository.findOne(userId)

		if (userGold.currentGold < rewardActivity.requireGold) {
			return '{"success": 0, "message": "err304"}'
		}

		def address = userReceiveAddressRepository.findOne(addressId)

		saveRecordAndGold(rewardActivity, userGold, address)
		'{"success": "1"}'
	}

	/**
	 * 修改奖品记录
	 * @param rewardActivityRecord
	 * @return
	 */
	@Transactional
	modify(RewardActivityRecord rewardActivityRecord) {
		def existRecord = rewardActivityRecordRepository.findOne(rewardActivityRecord.id)

		if (existRecord.status != rewardActivityRecord.status && rewardActivityRecord.status == RewardActivityRecord.Status.CONFIRMING) {

			modifyRecord(rewardActivityRecord)

		}

		'{"success": 1}'
	}

	/**
	 * 根据状态查找奖品记录
	 * @param userId
	 * @param status
	 * @return
	 */
	@Transactional
	findByStatus(long userId, String status) {

		def user = new User(id : userId),
			activityRequireds = []
		if (StringUtils.isEmpty(status)) {
			activityRequireds = rewardActivityRecordRepository.findByUserAndReal(user, true)
		} else {
			// DELIVERING 发货中, CONFIRMING 待确认, CONFIRMED 已确认, PROCESSING 处理中
			def rewardStatus = null
			switch(status) {
				case 'DELIVERING' :
					rewardStatus = RewardActivityRecord.Status.DELIVERING
					break
				case 'CONFIRMING' :
					rewardStatus = RewardActivityRecord.Status.CONFIRMING
					break
				case 'CONFIRMED' :
					rewardStatus = RewardActivityRecord.Status.CONFIRMED
					break
				case 'PROCESSING' :
					rewardStatus = RewardActivityRecord.Status.PROCESSING
					break
				case 'REWARD' :
					rewardStatus = RewardActivityRecord.Status.REWARD
					break
			}

			if (RewardActivityRecord.Status.REWARD == rewardStatus) {
				activityRequireds = rewardActivityRecordRepository.findAll(
					[
						user_equal : user,
						real_equal : reward.real
					]
				)
			} else {
				activityRequireds = rewardActivityRecordRepository.findAll(
					[
						user_equal : user,
						status_equal : rewardStatus
					]
				)
			}
		}
		def records = ['success' : '1', 'message' : '成功'],
			infos = []
		activityRequireds.each {

			def info = [
				'name' : it.reward?.rewardType?.name ?: '',
				'awardTime' : DateCommons.Date2String(it.awardTime, 'yyyy-MM-dd') ?: '',
				'coins' : it.rewardActivity?.requireGold ?: '',
				'code' : it.reward?.QRCode ?: '',
				'status' : it.status ?: ''
			]
			infos << info
		}

		records << ['records' : infos]
		records
	}

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
			rewards = rewardRepository.findByRewardTypeAndUsed(rewardActivity.rewardType, false)
			if (!rewards) {
				return '{"success" : "0", "message" : "err404"}' //没有奖品
			}
//			reward = rewardRepository.findByRewardType(rewardActivity.rewardType),
			def reward = rewards[0]
			reward.used = true
			def rewardActivityRecord = new RewardActivityRecord(
				user : user,
				rewardActivity : rewardActivity,
				userReceiveAddress : userReceiveAddress,
				reward : reward,
				status : RewardActivityRecord.Status.DELIVERING,
				awardTime : new Date()
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

		def rewardType = activityRecord.rewardActivity.rewardType,
			reward = activityRecord.reward,
			rewardAssignRecord = new RewardAssignRecord(
				rewardActivityRecord : activityRecord,
				reward : reward
			)

		rewardActivityRecordRepository.save(activityRecord)
		rewardAssignRecordRepository.save(rewardAssignRecord)
		'{"success" : "1"}'
	}
}
