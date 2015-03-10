package com.knowheart3.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.knowheart3.repository.self.SelfQuestionOrderRepository
import com.knowheart3.repository.self.SelfQuestionRepository
import com.knowheart3.repository.self.SelfSpecialInsertRepository
import com.knowheart3.service.self.SelfService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.user.User

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

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		selfService.findBySelf(selfId)
	}

}
