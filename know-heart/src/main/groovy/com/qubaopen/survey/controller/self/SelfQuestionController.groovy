package com.qubaopen.survey.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository
import com.qubaopen.survey.service.self.SelfService

@RestController
@RequestMapping('selfQuestions')
@SessionAttributes('currentUser')
public class SelfQuestionController extends AbstractBaseController<SelfQuestion, Long> {

	@Autowired
	SelfQuestionRepository selfQuestionRepository

	@Autowired
	SelfQuestionOrderRepository selfQuestionOrderRepository

	@Autowired
	SelfSpecialInsertRepository selfSpecialInsertRepository

	@Autowired
	SelfService selfService

	@Override
	protected MyRepository<SelfQuestion, Long> getRepository() {
		selfQuestionRepository
	}

	/**
	 * 获取自测问卷
	 * @param selfId
	 * @return
	 */
	@RequestMapping(value =  'findBySelf/{selfId}', method = RequestMethod.GET)
	findBySelf(@PathVariable long selfId, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取自测问卷 -- '
		
		selfService.findBySelf(selfId)
	}

}
