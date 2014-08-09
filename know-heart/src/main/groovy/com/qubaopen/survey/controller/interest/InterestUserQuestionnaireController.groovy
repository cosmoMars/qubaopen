package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository

@RestController
@RequestMapping('interestUserQuestionnaires')
public class InterestUserQuestionnaireController extends AbstractBaseController<InterestUserQuestionnaire, Long> {

	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Override
	protected MyRepository<InterestUserQuestionnaire, Long> getRepository() {
		interestUserQuestionnaireRepository
	}


	@RequestMapping(value = 'shareInterest', method = RequestMethod.PUT)
	shareInterest(@RequestParam long userId,
		@RequestParam long interestId,
		@RequestParam(required = false) boolean sharedSina,
		@RequestParam(required = false) boolean sharedTencent,
		@RequestParam(required = false) boolean sharedWeChatFriend,
		@RequestParam(required = false) boolean sharedQQSpace,
		@RequestParam(required = false) boolean sharedWeChat,
		@RequestParam(required = false) boolean publicToAll
		) {

		def user = new User(id : userId),
			interest = new Interest(id : interestId)

		def questionnarie = interestUserQuestionnaireRepository.findByUserAndInterest(user, interest)
		if (sharedSina) {
			questionnarie.sharedSina = sharedSina
		}
		if (sharedTencent) {
			questionnarie.sharedTencent = sharedTencent
		}
		if (sharedWeChatFriend) {
			questionnarie.sharedWeChatFriend = sharedWeChatFriend
		}
		if (sharedQQSpace) {
			questionnarie.sharedQQSpace = sharedQQSpace
		}
		if (sharedWeChat) {
			questionnarie.sharedWeChat = sharedWeChat
		}
		if (publicToAll) {
			questionnarie.publicToAll = publicToAll
		}
		interestUserQuestionnaireRepository.save(questionnarie)
		'{"success": "1"}'
	}

}
