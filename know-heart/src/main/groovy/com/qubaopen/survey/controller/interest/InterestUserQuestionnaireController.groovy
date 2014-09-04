package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository

@RestController
@RequestMapping('interestUserQuestionnaires')
@SessionAttributes('currentUser')
public class InterestUserQuestionnaireController extends AbstractBaseController<InterestUserQuestionnaire, Long> {

	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Override
	protected MyRepository<InterestUserQuestionnaire, Long> getRepository() {
		interestUserQuestionnaireRepository
	}


	@RequestMapping(value = 'shareInterest', method = RequestMethod.PUT)
	shareInterest(@RequestParam long interestId,
		@RequestParam(required = false) Boolean sharedSina,
		@RequestParam(required = false) Boolean sharedTencent,
		@RequestParam(required = false) Boolean sharedWeChatFriend,
		@RequestParam(required = false) Boolean sharedQQSpace,
		@RequestParam(required = false) Boolean sharedWeChat,
		@RequestParam(required = false) Boolean publicToAll,
		@ModelAttribute('currentUser') User user
		) {

		def interest = new Interest(id : interestId)

		def questionnarie = interestUserQuestionnaireRepository.findByUserAndInterest(user, interest)
		if (sharedSina != null) {
			questionnarie.sharedSina = sharedSina
		}
		if (sharedTencent != null) {
			questionnarie.sharedTencent = sharedTencent
		}
		if (sharedWeChatFriend != null) {
			questionnarie.sharedWeChatFriend = sharedWeChatFriend
		}
		if (sharedQQSpace != null) {
			questionnarie.sharedQQSpace = sharedQQSpace
		}
		if (sharedWeChat != null) {
			questionnarie.sharedWeChat = sharedWeChat
		}
		if (publicToAll != null) {
			questionnarie.publicToAll = publicToAll
		}
		interestUserQuestionnaireRepository.save(questionnarie)
		'{"success": "1"}'
	}

}
