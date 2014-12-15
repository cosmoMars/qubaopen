package com.qubaopen.survey.controller.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.RewardLevel;
import com.qubaopen.survey.repository.reward.RewardLevelRepository;

@RestController
@RequestMapping("rewardLevels")
public class RewardLevelController extends AbstractBaseController<RewardLevel, Long> {

	@Autowired
	private RewardLevelRepository rewardLevelRepository;

	@Override
	protected MyRepository<RewardLevel, Long> getRepository() {
		return rewardLevelRepository;
	}

}
