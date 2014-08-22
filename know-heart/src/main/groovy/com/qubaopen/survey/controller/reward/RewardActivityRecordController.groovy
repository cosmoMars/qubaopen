package com.qubaopen.survey.controller.reward

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.reward.RewardActivityRecord
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.reward.RewardActivityRecordRepository
import com.qubaopen.survey.service.reward.RewardActivityRecordService

@RestController
@RequestMapping('rewardActivityRecords')
@SessionAttributes('currentUser')
public class RewardActivityRecordController extends AbstractBaseController<RewardActivityRecord, Long> {

	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

	@Autowired
	RewardActivityRecordService rewardActivityRecordService

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
	exchangeReward(@RequestParam(required = false) long activityId, @RequestParam(required = false) long addressId, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 创建兑奖信息记录 -- '

		if (!activityId) {
			return '{"success": "0", "message": "err305"}'
		}

		if (!addressId) {
			return '{"success": "0", "message": "err306"}'
		}

		rewardActivityRecordService.exchangeReward(user.id, activityId, addressId)
	}

	/**
	 * 修改用户参与活动记录，改变状态为confirming时，创建奖品分发记录
	 */
	@RequestMapping(value = 'modifyRecordStatus', method = RequestMethod.PUT)
	modifyRecordStatus(@RequestParam long recordId, @RequestParam String status) {

		logger.trace ' -- 修改用户参与活动记录，改变状态为confirming时，创建奖品分发记录 -- '

//		if (!rewardActivityRecord) {
//			return '{"success": "0", "message": "err307"}'
//		}

		rewardActivityRecordService.modifyRecordStatus(recordId, status)

	}

	/**
	 * 根据状态位查找用户活动记录
	 * @param userId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = 'findByStatus', method = RequestMethod.GET)
	findByStatus(@RequestParam(required = false) String status, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 根据状态位查找用户活动记录 -- '

		rewardActivityRecordService.findByStatus(user.id, status)
	}
}
