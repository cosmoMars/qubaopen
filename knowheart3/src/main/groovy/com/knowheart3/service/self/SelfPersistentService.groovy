package com.knowheart3.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.knowheart3.repository.mindmap.MapRecordRepository
import com.knowheart3.repository.mindmap.MapStatisticsRepository
import com.knowheart3.repository.mindmap.MapStatisticsTypeRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.self.SelfUserAnswerRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.repository.user.UserRepository
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.self.SelfUserAnswer
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo

@Service
public class SelfPersistentService {

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfUserAnswerRepository selfUserAnswerRepository

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsTypeRepository mapStatisticsTypeRepository
	
	@Autowired
	MapRecordRepository mapRecordRepository
	
	@Autowired
	SelfRepository selfRepository
	
	@Autowired
	SelfService selfService
	
	@Autowired
	UserRepository userRepository
	
	@Autowired
	UserInfoRepository userInfoRepository

	/**
	 * 保存用户答卷信息，用户答题内容
	 * @param user
	 * @param self
	 * @param questionVos
	 * @param questions
	 * @param options
	 * @param resultOption
	 */
	@Transactional
	def saveQuestionnaireAndUserAnswer(User user, Self self, List<QuestionVo> questionVos, List<SelfQuestion> questions, List<SelfQuestionOption> options, SelfResultOption resultOption, boolean refresh, int score) {
		def selfUserQuestionnaire, questionnaire 
		if (refresh) { // 重做
			questionnaire = selfUserQuestionnaireRepository.findRecentQuestionnarie(user, self)
			def	now = new Date()
			if (questionnaire) {
				def intervalTime = questionnaire.self.intervalTime as Long
				if (now.getTime() - questionnaire.time.getTime() < intervalTime * 60 * 60 * 1000) {
					questionnaire.used = false
					selfUserQuestionnaireRepository.save(questionnaire)
				}
			}
			selfUserQuestionnaire = new SelfUserQuestionnaire(
				user : user,
				self : self,
				selfResultOption : resultOption,
				status : SelfUserQuestionnaire.Status.COMPLETED,
				transmit : SelfUserQuestionnaire.Transmit.NOTRANSMIT,
				time : new Date(),
				used : true,
				score : score
			)
		} else {
			selfUserQuestionnaire = new SelfUserQuestionnaire(
				user : user,
				self : self,
				selfResultOption : resultOption,
				status : SelfUserQuestionnaire.Status.COMPLETED,
				transmit : SelfUserQuestionnaire.Transmit.NOTRANSMIT,
				time : new Date(),
				used : false,
				score : score
			)
		}
	
		selfUserQuestionnaire = selfUserQuestionnaireRepository.save(selfUserQuestionnaire)

		selfService.calcUserAnalysisRadio(userInfoRepository.findOne(user.id))
		
		def userAnswers = []

		questions.each { q ->
			def type = q.type
			def answer = null, option = null
			questionVos.find { vo ->
				if (q.id == vo.questionId) {
					if (type == SelfQuestion.Type.SINGLE) { // 单选
						def choiceId = vo.contents[0].id
						options.find { o ->
							if (o.id == choiceId) {
								option = o
							}
						}
						answer = new SelfUserAnswer(
							user : user,
							selfUserQuestionnaire : selfUserQuestionnaire,
							selfQuestionOption : option,
							selfQuestion : q,
							score : option.score
						)
						userAnswers << answer
					} else if (type == SelfQuestion.Type.MULTIPLE) { // 多选
						vo.contents.each { voc ->
							options.find { o ->
								if (o.id == voc.id) {
									option = o
								}
							}
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : option,
								selfQuestion : q,
								score : option.score
							)
							userAnswers << answer
						}
					} else if (type == SelfQuestion.Type.QA) { // 问答
						vo.contents.each { voc ->
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestion : q,
								selfQuestionOption : new SelfQuestionOption(id : voc.id),
								content : voc.cnt
							)
							userAnswers << answer
						}
					} else if (type == SelfQuestion.Type.SORT) { // 排序
						vo.contents.each { voc ->
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : new SelfQuestionOption(id : voc.id),
								selfQuestion : q,
								turn : voc.order
							)
							userAnswers << answer
						}
					} else if (type == SelfQuestion.Type.SCORE) { // 打分
						vo.contents.each {
							options.find { o ->
								if (o.id == it.id) {
									option = o
								}
							}
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : option,
								selfQuestion : q,
								content : option.content

							)
							userAnswers << answer
						}
					}
				}
			}
		}
		selfUserAnswerRepository.save(userAnswers)
//		self.totalRespondentsCount ++
//		selfRepository.save(self)
		selfUserQuestionnaire
	}

	/**
	 * 保存心理地图资料
	 * @param user
	 * @param id
	 * @param result
	 */
	@Transactional
	void saveMapStatistics(User user, SelfUserQuestionnaire selfUserQuestionnaire, List<MapRecord> result, SelfResultOption selfResultOption, Integer score) {

		def special = false
		def specialSelf = selfRepository.findSpecialSelf()
		
//		def questionnaire = selfUserQuestionnaireRepository.findRecentQuestionnarie(user, self),
		def questionnaire = selfUserQuestionnaireRepository.findRecentQuestionnarie(user, selfUserQuestionnaire.id)
		def	now = new Date()
		if (questionnaire) {
			def intervalTime = questionnaire.self.intervalTime as Long

			println (now.getTime() - questionnaire.time.getTime())
			println (now.getTime() - questionnaire.time.getTime() < intervalTime * 60 * 60 * 1000)

			if (now.getTime() - questionnaire.time.getTime() < intervalTime * 60 * 60 * 1000) {
				if (questionnaire.self.id == specialSelf.id) {
					def record = mapRecordRepository.findMaxRecordBySpecialSelf(questionnaire.self, user)
					if (record)
						mapRecordRepository.delete(record)
				}
			}
		}
		def nowSelf = selfUserQuestionnaire.self
		if (nowSelf.id == specialSelf.id) {
			special = true
		}
		
		def mapStatistics = mapStatisticsRepository.findByUserAndSelf(user, nowSelf)
		
		if (mapStatistics) {
			result.each {
				it.mapStatistics = mapStatistics
			}
			mapStatistics.selfResultOption = selfResultOption
			if (score != null) {
				mapStatistics.score = score
			}
			mapStatistics.mapMax = nowSelf.mapMax
			mapStatistics.selfManagementType = nowSelf.selfManagementType
			mapStatistics.recommendedValue = nowSelf.recommendedValue
			if (!special) {
				mapRecordRepository.deleteByMapStatistics(mapStatistics)
			}
		} else {
			mapStatistics = new MapStatistics(
				user : user,
				self : nowSelf,
				selfResultOption : selfResultOption,
				score : score,
				mapMax : nowSelf.mapMax,
				selfManagementType : nowSelf.selfManagementType,
				recommendedValue : nowSelf.recommendedValue
			)
			result.each {
				it.mapStatistics = mapStatistics
			}
		}
		mapStatisticsRepository.save(mapStatistics)
		mapRecordRepository.save(result)
	}	
}
