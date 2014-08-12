package com.qubaopen.survey.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.service.self.SelfService

@RestController
@RequestMapping('selfs')
public class SelfController extends AbstractBaseController<Self, Long> {

	@Autowired
	SelfRepository selfRepository

	@Autowired
	SelfService selfService

	@Override
	protected MyRepository<Self, Long> getRepository() {
		selfRepository
	}

	/**
	 * 计算自测结果选项
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @return
	 */
	@RequestMapping(value = 'calculateSelfResult', method = RequestMethod.GET)
	calculateSelfReslut(@RequestParam long userId, @RequestParam long selfId, @RequestParam String questionJson, @RequestParam(required = false) boolean refresh) {

		logger.trace ' -- 计算自测结果选项 -- '

		selfService.calculateSelfReslut(userId, selfId, questionJson, refresh)

	}

}
