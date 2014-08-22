package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.mindmap.MapStatisticsType
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.self.SelfUserAnswer
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository

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
	void saveQuestionnaireAndUserAnswer(User user, Self self, List<QuestionVo> questionVos, List<SelfQuestion> questions, List<SelfQuestionOption> options, SelfResultOption resultOption) {

		def selfUserQuestionnaire = new SelfUserQuestionnaire(
			user : user,
			self : self,
			selfResultOption : resultOption,
			status : SelfUserQuestionnaire.Status.COMPLETED,
			transmit : SelfUserQuestionnaire.Transmit.NOTRANSMIT,
			time : new Date()
		)
		selfUserQuestionnaire = selfUserQuestionnaireRepository.save(selfUserQuestionnaire)

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
					}
					if (type == SelfQuestion.Type.MULTIPLE) { // 多选
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
					}
					if (type == SelfQuestion.Type.QA) { // 问答
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
					}
					if (type == SelfQuestion.Type.SORT) { // 排序
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
					}
					if (type == SelfQuestion.Type.SCORE) { // 打分
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

	}

	/**
	 * 保存心理地图资料
	 * @param user
	 * @param id
	 * @param result
	 */
	@Transactional
	void saveMapStatistics(User user, Self self, String result, SelfResultOption selfResultOption, int score) {

		def selfType = self.abbreviation
		if (selfType == 'C' || selfType == 'D' || selfType == 'AB') {
			selfType = 'ABCD'
		}
		def mapType = mapStatisticsTypeRepository.findByName(selfType)

		if (!mapType) {
			def mapStatisticsType = new MapStatisticsType(
				name : selfType
			)
			mapType = mapStatisticsTypeRepository.save(mapStatisticsType)
		}

		def mapStatistics = mapStatisticsRepository.findByUserAndSelf(user, self)
		if (mapStatistics) {
			mapStatistics.result = result
			mapStatistics.selfResultOption = selfResultOption
			mapStatistics.score = score
			mapStatistics.mapStatisticsType = mapType
			mapStatistics.mapMax = self.mapMax
			mapStatistics.managementType = self.managementType.toString()
			mapStatistics.recommendedValue = self.recommendedValue
		} else {
			mapStatistics = new MapStatistics(
				user : user,
				self : self,
				mapStatisticsType : mapType,
				result : result,
				selfResultOption : selfResultOption,
				score : score,
				mapMax : self.mapMax,
				managementType : self.managementType.toString(),
				recommendedValue : self.recommendedValue
			)
		}
		mapStatisticsRepository.save(mapStatistics)
	}
}
