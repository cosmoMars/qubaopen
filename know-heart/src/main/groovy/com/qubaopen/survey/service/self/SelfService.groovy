package com.qubaopen.survey.service.self

import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.Self.ManagementType
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository;

@Service
public class SelfService {

	@Autowired
	SelfQuestionRepository selfQuestionRepository

	@Autowired
	SelfSpecialInsertRepository selfSpecialInsertRepository

	@Autowired
	SelfQuestionOptionRepository selfQuestionOptionRepository

	@Autowired
	SelfQuestionOrderRepository selfQuestionOrderRepository

	@Autowired
	SelfRepository selfRepository

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	SelfResultService selfResultService
	
	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository
	
	/**
	 * 获取自测问卷
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	retrieveSelf(User user, boolean refresh) {
		
		def now = new Date(), resultSelfs = [], allSelfs = [],
		selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTime(user) // 所有用户答过的题目记录，按照时间倒序排序
		
		def index = dayForWeek()
		
//		def singleSelf = selfRepository.findByManagementTypeAndIntervalTime(ManagementType.Character, 4) // 必做的题目 4小时
		def singleSelf = selfRepository.findSpecialSelf()
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
			}
			selfUserQuestionnaires.remove(userQuestionnaire)
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
					if (!selfUserQuestionnaires) {
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
		} else if (index in 6..7) {
			if (!todayUserQuestionnaires) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
				} else {
					if (!selfUserQuestionnaires) {
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
	}
	
	/**
	 * 查找自测问卷问题
	 * @param selfId
	 * @return
	 */
	@Transactional(readOnly = true)
	findBySelf(long selfId) {
		def self = new Self(id : selfId)

		def questions = selfQuestionRepository.findBySelf(self),
			specialInserts = selfSpecialInsertRepository.findAllBySelf(self),
			questionOrders = selfQuestionOrderRepository.findBySelf(self)

		def questionResult = [],
			questionNo = 1
		questions.each { q ->

			def resultOptions = [],
				selfQuestionOptions = q.selfQuestionOptions as List
			Collections.sort(selfQuestionOptions, new OptionComparator())
			selfQuestionOptions.each { qo -> // 选项
				resultOptions << [
					'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
			}

			def resultOrder = []
			if (q.special) { // 是特殊题
				specialInserts.each { si ->
					if (q.id == si.specialQuestionId) { // 找到特殊题 取得上一题id 完成order
						questionOrders.findAll { // 找到所有特殊题答题顺序
							it.questionId == si.questionId
						}.each {
							resultOrder << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
						}
					}
				}
			}

			specialInserts.findAll { si -> // 直接判断是否是特殊题的上一题，取得特殊题上一题
				q.id == si.questionId
			}.each {
				resultOrder << "${it.questionId}:${it.questionOptionId}:${it.specialQuestionId}"
			}

			if (!q.children) { // 判断不是矩阵题
				def order = ''
				if (!resultOrder.isEmpty()) {
					order = resultOrder.join('|')
				} else {
					def oResult = []
					questionOrders.findAll {
						q.id == it.questionId
					}.each {
						oResult << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
					}
					order = oResult.join('|')
				}
				questionResult << [
					'questionId' : q.id,
					'questionContent' : q.content,
					'questionType' : q.type,
					'optionCount' : q.optionCount,
					'limitTime' : q.answerTimeLimit,
					'special' : q.special,
					'questionNum' : questionNo,
					'matrix' : false,
					'order' : order,
					'options' : resultOptions
				]
			} else if (q.children) { // 判断是矩阵题
				def children = q.children as List
				Collections.sort(children, new QuestionComparator())
				children.each { c->
					def childResult = [],
						childOptions = c.selfQuestionOptions as List
					Collections.sort(childOptions, new OptionComparator())
					childOptions.each { qo -> // 选项
						childResult << [
							'optionId' : qo.id,
							'optionNum' : qo.optionNum,
							'optionContent' : qo.content
						]
					}
					def orderResult = []
					questionOrders.findAll { qo->
						c.id == qo.questionId
					}.each {
						orderResult << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
					}

					questionResult << [
						'questionId' : c.id,
						'questionContent' : c.content,
						'questionType' : c.type,
						'optionCount' : c.optionCount,
						'limitTime' : c.answerTimeLimit,
						'special' : c.special,
						'questionNum' : c.questionNum,
						'matrix' : true,
						'matrixTitle' : q.content,
						'matrixNo' : questionNo,
						'order' : orderResult.join('|'),
						'options' : childResult
					]
				}
			}
			questionNo ++
		}

		[
			'success' : '1',
			'message' : '成功',
			'questions' : questionResult
		]
	}

	/**
	 * 计算保存问卷信息
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateSelfReslut(long userId, long selfId, String questionJson, boolean refresh) {

		def user = new User(id : userId),
			self = selfRepository.findOne(selfId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class),
			questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []
		questionVos.each { // 获得所有问题id和答案选项id
			questionIds << it.questionId
			it.contents.each { c ->
				optionIds += c.id
			}
		}

		def questions = selfQuestionRepository.findAll(questionIds),
			questionOptions = selfQuestionOptionRepository.findAll(optionIds)

		def typeName = self.questionnaireType.name, // 获得问卷类型名称
			result = null

		switch (typeName) {
			case 'score' : // 得分
				result = selfResultService.calculateScore(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'qtype' : // 问题种类
				result = selfResultService.calculateQType(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'otype' : // 问题选项种类
				result = selfResultService.calculateOType(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'turn' : // 跳转
				result = selfResultService.calculateTurn(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'save' : // 保存
				result = selfResultService.calculateSave(user, self, questionOptions, questionVos, questions, refresh)
				break
		}

		result

	}

	class OptionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			SelfQuestionOption so1 = (SelfQuestionOption) o1
			SelfQuestionOption so2 = (SelfQuestionOption) o2
			return so1.optionNum.compareTo(so2.optionNum)
		}
	}
	class QuestionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			SelfQuestion so1 = (SelfQuestion) o1
			SelfQuestion so2 = (SelfQuestion) o2
			return so1.questionNum.compareTo(so2.questionNum)
		}
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
