package com.qubaopen.survey.controller.self

import org.apache.commons.lang3.StringUtils
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
import com.qubaopen.survey.entity.self.Self.ManagementType
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
	retrieveSelf(@RequestParam(required = false) Boolean refresh, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取用户自测问卷 -- '

		def data = [], result = [], now = new Date(),
			selfs = selfService.retrieveSelf()
			
		def index = dayForWeek()
		if (index in 1..5) {
			if (result) {
				def singleSelf = selfRepository.findByManagementTypeAndIntervalTime(ManagementType.Character, 4) // 必做的题目
				def epqSelfs = selfRepository.findAll( // epq 4套题目
						[
							'selfType.name_equal' : 'EPQ'
						]
					)
				
				
			}
		} else if (index in 6..7) {
		
		}
		
		if (refresh) {
			
		}
		

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
				'guidanceSentence' :it.guidanceSentence
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
	calculateSelfReslut(@RequestParam(required = false) Long selfId,
		@RequestParam(required = false) String questionJson,
		@RequestParam(required = false) Boolean refresh,
		@ModelAttribute('currentUser') User user
		) {

		logger.trace ' -- 计算自测结果选项 -- '

		if (StringUtils.isEmpty(questionJson)) {
			return '{"success" : "0", "message" : "err1234"}'
		}

		if (!refresh) {
			refresh = false
		}

		def result = selfService.calculateSelfReslut(user.id, selfId, questionJson, refresh)

		if (result instanceof String) {
			return result
		}

		if (!result) {
			return '{"success" : "0", "message" : "err123123"}'
		}
		[
			'success' : '1',
			'message' : '成功',
			'id' : result?.id,
			'resultTitle' : result?.selfResult?.title,
			'content' : result?.content,
			'optionTitle' : result?.title,
			'optionNum' : result?.resultNum
		]

	}

	def dayForWeek() {
		def c = Calendar.getInstance()
		c.setTime new Date()
		def idx
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			idx = 7
		} else {
			idx = c.get(Calendar.DAY_OF_WEEK) - 1
		}
		idx
	}

}
