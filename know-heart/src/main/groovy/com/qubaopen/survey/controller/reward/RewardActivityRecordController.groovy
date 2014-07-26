package com.qubaopen.survey.controller.reward

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.entity.reward.RewardActivityRecord
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.reward.RewardActivityRecordRepository
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.repository.reward.RewardAssignRecordRepository
import com.qubaopen.survey.repository.reward.RewardInfoRepository
import com.qubaopen.survey.repository.user.UserGoldRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.service.reward.RewardActivityRecordService

@RestController
@RequestMapping("rewardActivityRecords")
public class RewardActivityRecordController extends AbstractBaseController<RewardActivityRecord, Long> {

	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

	@Autowired
	RewardActivityRepository rewardActivityRepository

	@Autowired
	UserGoldRepository userGoldRepository

	@Autowired
	RewardActivityRecordService rewardActivityRecordService

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	RewardAssignRecordRepository rewardAssignRecordRepository

	@Autowired
	RewardInfoRepository rewardInfoRepository

	@Override
	protected MyRepository<RewardActivityRecord, Long> getRepository() {
		rewardActivityRecordRepository
	}

	/**
	 * 兑换奖品，创建兑奖信息记录
	 * @param userId
	 * @param activityId
	 * @param addressId
	 * @return
	 */
	@RequestMapping(value = 'exchangeReward', method = RequestMethod.POST)
	exchangeReward(@RequestParam long userId, @RequestParam long activityId, @RequestParam long addressId) {

		logger.trace ' -- 创建兑奖信息记录 -- '

		def rewardActivity = rewardActivityRepository.findOne(activityId)

		if (rewardActivity != null && rewardActivity.status != RewardActivity.Status.ONLINE) {
			return '{"success": 0, "message": "活动未上线或已结束"}'
		}

		if (rewardActivity.currentCount >= rewardActivity.totalCountLimit) {
			return '{"success": 0, "message": "该活动已售完"}'
		}

		def user = new User(id : userId),
			activityCount = rewardActivityRecordRepository.countByUser(user)

		if (rewardActivity.eachCountLimit != 0 && activityCount > rewardActivity.eachCountLimit) {
			return '{"success": 0, "message": "兑奖次数已用完"}'
		}

		def userGold = userGoldRepository.findOne(userId)

		if (userGold.currentGold < rewardActivity.requireGold) {
			return '{"success": 0, "message": "当前金币不足"}'
		}

		def address = userReceiveAddressRepository.findOne(addressId)

		rewardActivityRecordService.saveRecordAndGold(rewardActivity, userGold, address)
	}

	/**
	 * 修改用户参与活动记录，改变状态为confirming时，创建奖品分发记录
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody @Valid RewardActivityRecord rewardActivityRecord, BindingResult result) {

		logger.trace ' -- 修改用户参与活动记录，改变状态为confirming时，创建奖品分发记录 -- '

		def existRecord = rewardActivityRecordRepository.findOne(rewardActivityRecord.id)

		if (existRecord.status != rewardActivityRecord.status && rewardActivityRecord.status == RewardActivityRecord.Status.CONFIRMING) {

			rewardActivityRecordService.modifyRecord(rewardActivityRecord)

		}
	}

	/**
	 * 根据状态位查找用户活动记录
	 * @param userId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = 'findByStatus', method = RequestMethod.GET)
	findByStatus(@RequestParam long userId, @RequestParam String status) {

		logger.trace ' -- 根据状态位查找用户活动记录 -- '

		def user = new User(id : userId)
		if (status.isEmpty() || ''.is(status)) {
			return rewardActivityRecordRepository.findAllByUser(user)
		}

		// DELIVERING 发货中, CONFIRMING 待确认, CONFIRMED 已确认, PROCESSING 处理中
		def rewardStatus
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
			def activityRequireds = rewardActivityRecordRepository.findAllByUser(user),
			 	rewardInfos
			activityRequireds.each {
				if (it.rewardInfo) {
					rewardInfos << it.rewardInfo
				}
			}
			return rewardInfos
		}

		return rewardActivityRecordRepository.findAll(
			[
				user_equal : user,
				status_equal : rewardStatus
			]
		)
	}
}
