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

//			selfs = selfService.retrieveSelf(),
		def data = [], now = new Date(), resultSelfs = [], allSelfs = [],
			selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTime(user) // 所有用户答过的题目记录，按照时间倒序排序
			
		def index = dayForWeek()
		
		def singleSelf = selfRepository.findByManagementTypeAndIntervalTime(ManagementType.Character, 4) // 必做的题目 4小时
		def epqSelfs = selfRepository.findAll( // epq 4套题目
			[
				'selfType.name_equal' : 'EPQ'
			]
		)
		
		allSelfs << singleSelf
		allSelfs += epqSelfs
		
		def userQuestionnaire = selfUserQuestionnaires.find { // 4小时题 记录
			it.self.id = singleSelf.id
		}
		if (userQuestionnaire) { // 判断4小时是否符合时间，符合添加，没有也添加
			if (now.getTime() - userQuestionnaire.time.getTime() > singleSelf.intervalTime * 60 * 60 * 1000) {
				resultSelfs << singleSelf
				selfUserQuestionnaires.remove(userQuestionnaire)
			}
		} else {
			resultSelfs << singleSelf
		}
		
		epqSelfs.each { epq ->
			def questionnaire = selfUserQuestionnaires.find { suq ->
				epq.id == suq.self.id
			}
			selfUserQuestionnaires.remove(questionnaire)
			if (questionnaire) {
				if (now.getTime() - questionnaire.time.getTime() > epq.intervalTime * 60 * 60 * 1000) {
					resultSelfs << epq
				}
			} else {
				resultSelfs << epq
			}
		}
		def existQuestionnaires = selfUserQuestionnaires.findAll {
			now.getTime() - it.time.getTime() < it.self.intervalTime * 60 * 60 * 1000
		}
		existQuestionnaires.each {
			allSelfs << it.self
		}
		def todayUserQuestionnaires = selfUserQuestionnaires.findAll { // 每天额外题目
			DateUtils.isSameDay(now, it.time)
		}
		if (index in 1..5) {
			if (!todayUserQuestionnaires) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
				} else {
					def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.managementType, allSelfs)
					if (relationSelfs) {
						resultSelfs << relationSelfs[new Random().nextInt(relationSelfs.size())]
					} else {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
					}
				}
			}
		} else if (index in 6..7) {
			if (!todayUserQuestionnaires) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
				} else {
					def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.managementType, allSelfs)
					if (relationSelfs) {
						for (i in 1..2) {
							def randSelf = relationSelfs[new Random().nextInt(relationSelfs.size())]
							resultSelfs << randSelf
							resultSelfs.remove(randSelf)
						}
					} else {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
					}
				}
			} else if (todayUserQuestionnaires.size() == 1) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
				} else {
					def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.managementType, allSelfs)
					if (relationSelfs) {
						resultSelfs << relationSelfs[new Random().nextInt(relationSelfs.size())]
					} else {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
					}
				}
			}
		
		}
		resultSelfs.each {
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
