package com.qubaopen.survey.controller.self

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils;
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

		def data = [], result = [], now = new Date(), selfs = [],
//			selfs = selfService.retrieveSelf(),
			selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTime(user)
			
		def index = dayForWeek()
		
		def singleSelf = selfRepository.findByManagementTypeAndIntervalTime(ManagementType.Character, 4) // 必做的题目
		def epqSelfs = selfRepository.findAll( // epq 4套题目
			[
				'selfType.name_equal' : 'EPQ'
			]
		)
		def userQuestionnaire = selfUserQuestionnaires.find { // 4小时题 记录
			it.self.id = singleSelf.id
		}
		epqSelfs.each { epq ->
			def questionnaire = selfUserQuestionnaires.find { suq ->
				epq.id == suq.self.id
			}
			if (questionnaire) {
				if (now.getTime() - questionnaire.time.getTime() > epq.intervalTime * 60 * 60 * 1000) {
					selfs << epq
				}
			} else {
				selfs << epq
			}
		}
		if (index in 1..5) {
			if (userQuestionnaire) {
				if (now.getTime() - userQuestionnaire.time.getTime() > singleSelf.intervalTime * 60 * 60 * 1000) {
					selfs << singleSelf
				}
			} 
			def otherSelfs = selfRepository.findWithoutExists(selfs)
			def todayUserQuestionnaires = selfUserQuestionnaires.findAll {
				DateUtils.isSameDay(now, it.time) && it.self.id != singleSelf.id
			}
			if (!todayUserQuestionnaires) {
				selfs << otherSelfs[new Random().nextInt(otherSelfs.size())]
			}
		} else if (index in 6..7) {
			if (userQuestionnaire) {
				if (now.getTime() - userQuestionnaire.time.getTime() > singleSelf.intervalTime * 60 * 60 * 1000) {
					selfs << singleSelf
				}
			}
			def otherSelfs = selfRepository.findWithoutExists(selfs)
			def todayUserQuestionnaires = selfUserQuestionnaires.findAll {
				DateUtils.isSameDay(now, it.time) && it.self.id != singleSelf.id
			}
			if (!todayUserQuestionnaires) {
				for (i in 0..<2) {
					def idx = new Random().nextInt(otherSelfs.size())
					selfs << otherSelfs[idx]
					otherSelfs.remove(otherSelfs[idx])
				}
			} else if (todayUserQuestionnaires.size() == 1) {
				selfs << otherSelfs[new Random().nextInt(otherSelfs.size())]
			}
			
		}
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
