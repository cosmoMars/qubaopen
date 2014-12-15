package com.qubaopen.doctor.controller.selfUserQuestionnaire;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.selfUserQuestionnaire.SelfUserQuestionnaireRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('selfUserQuestionnaire')
@SessionAttributes('currentDoctor')
public class SelfUserQuestionnaireController extends AbstractBaseController<SelfUserQuestionnaire, Long> {

	private static Logger logger = LoggerFactory.getLogger(SelfUserQuestionnaireController.class)
	
	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository
	
	@Override
	MyRepository<SelfUserQuestionnaire, Long> getRepository() {
		selfUserQuestionnaireRepository
	}
	
	/**
	 * @param userId
	 * @param doctor
	 * @return
	 * 查询用户问卷结果
	 */
	@RequestMapping(value = 'retrieveSelfResult', method = RequestMethod.POST)
	retrieveSelfResult(@RequestParam Long userId, 
		@RequestParam(required = false) Long typeId,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		
		def questionnaire,
			data = []
		if (typeId != null) {
			questionnaire = selfUserQuestionnaireRepository.findByMaxTime(new User(id : userId), typeId)
		} else {
			questionnaire = selfUserQuestionnaireRepository.findByMaxTime(new User(id : userId))
		}
		questionnaire.each {
			def selfResult = it.selfResultOption
			if (selfResult) {
				data << [
					'id' : selfResult?.id,
					'resultTitle' : selfResult?.selfResult?.title,
					'content' : selfResult?.content,
					'optionTitle' : selfResult?.title,
					'resultRemark' : selfResult?.selfResult?.remark,
					'optionNum' : selfResult?.resultNum
				]
			}
		}
		[
			'success' : '1',
			'data' : data
		]
	}

}
