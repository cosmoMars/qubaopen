package com.qubaopen.survey.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository
import com.qubaopen.survey.service.self.SelfService

@RestController
@RequestMapping('selfs')
@SessionAttributes('currentUser')
public class SelfController extends AbstractBaseController<Self, Long> {

	@Autowired
	SelfRepository selfRepository

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfService selfService

	@Override
	protected MyRepository<Self, Long> getRepository() {
		selfRepository
	}

	/**
	 * 获取用户自测问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelf', method = RequestMethod.GET)
	retrieveSelf(@ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取用户自测问卷 -- '

		def data = [],
			selfs = selfService.retrieveSelf()
			/*selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTime(user)

		def selfs = [], now = new Date(), justSelf = null
		def justUserQuestionnaire = selfUserQuestionnaires.find { // 找到小于循环时间的问卷， 得到问卷类型
			(now.time - it.time.time) < 20 * 60 * 1000
		}
		if (justUserQuestionnaire) {
			selfUserQuestionnaires.remove(justUserQuestionnaire)
			justSelf = justUserQuestionnaire.self
		}

		if (justSelf) { // 找到刚做好的题目
			selfUserQuestionnaires.findAll { suq ->
				justSelf.managementType == suq.self.managementType
			}.each {
				selfs << it.self
			}
			selfUserQuestionnaires.findAll { suq ->
				justSelf.managementType != suq.self.managementType
			}.each {
				selfs << it.self
			}
		} else {
			selfUserQuestionnaires.each {
				selfs << it.self
			}
		}


//		selfUserQuestionnaires.findAll { // 找到所有符合时间的问卷
//			(now.time - it.time.time) > 1 * 60 * 1000
//		}.each { e ->
//			selfs << e.self
//		}
//		selfUserQuestionnaires.each {
//			selfs << it.self
//		}
*/
		selfs.each {
			def self = [
				'selfId' : it.id,
				'managementType' : it.managementType,
				'title' : it.title,
			]
			data << self
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}

	/**
	 * 计算自测结果选项
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @return
	 */
	@RequestMapping(value = 'calculateSelfResult', method = RequestMethod.POST)
	calculateSelfReslut(@RequestParam long selfId,
		@RequestParam String questionJson,
		@RequestParam(required = false) Boolean refresh,
		@ModelAttribute('currentUser') User user
		) {

		logger.trace ' -- 计算自测结果选项 -- '

		def result = selfService.calculateSelfReslut(user.id, selfId, questionJson, refresh)
		[
			'success' : '1',
			'message' : '成功',
			'id' : result?.id,
			'resultTitle' : result?.selfResult?.title,
			'content' : result?.content,
			'optionTitle' : result?.title
//			'resultNum' : result?.resultNum
		]

	}

}
