package com.qubaopen.survey.controller.reward

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.reward.Reward
import com.qubaopen.survey.repository.reward.RewardRepository

@RestController
@RequestMapping('rewards')
public class RewardController extends AbstractBaseController<Reward, Long> {

	@Autowired
	RewardRepository rewardRepository

	@Override
	protected MyRepository<Reward, Long> getRepository() {
		rewardRepository
	}

	/**
	 * 点击查看奖品
	 * @param rewardId
	 * @return
	 */
	@RequestMapping(value = 'retrieveReward/{rewardId}', method = RequestMethod.GET)
	retrieveReward(@PathVariable long rewardId) {
		def reward = rewardRepository.findOne(rewardId)

		[
			'success' : '1',
			'message' : '成功',
			'content' : reward.content ?: '',
			'title' : reward.title ?: '',
			'secretCode' : reward.secretCode ?: '',
			'remark' : reward.remark ?: '',
			'expirationDate' : reward.expirationDate ?: ''
		]
	}
}
