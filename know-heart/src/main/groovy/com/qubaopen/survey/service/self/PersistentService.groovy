package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.self.SelfUserAnswer
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository

@Service
public class PersistentService {

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfUserAnswerRepository selfUserAnswerRepository

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

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
						def choiceId = Long.valueOf(vo.content[0])
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
						vo.content.each { cId ->
							options.find { o ->
								if (o.id == Long.valueOf(cId)) {
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
						vo.content.each {
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestion : q,
								content : it
							)
							userAnswers << answer
						}
					}
					if (type == SelfQuestion.Type.SORT) { // 排序
						vo.content.eachWithIndex { cId, index ->
							options.find { o ->
								if(o.id == Long.valueOf(cId)) {
									option = o
								}
							}

							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : option,
								selfQuestion : q,
								turn : index + 1
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
	void saveMapStatistics(User user, Self self, String result, SelfResultOption selfResultOption, int score, Integer mapMax) {

		def selfType = self.selfType.name
		def type = null
		switch (selfType) {
			case "SDS" :
				type = MapStatistics.Type.SDS
				break
			case "AB" :
				type = MapStatistics.Type.ABCD
				break
			case "C" :
				type = MapStatistics.Type.ABCD
				break
			case "D" :
				type = MapStatistics.Type.ABCD
				break
			case "PDP" :
				type = MapStatistics.Type.PDP
				break
			case "MBTI" :
				type = MapStatistics.Type.MBTI
				break
		}

		def mapStatistics = mapStatisticsRepository.findByUserAndSelf(user, self)
		if (mapStatistics) {
			mapStatistics.result = result
			mapStatistics.selfResultOption = selfResultOption
			mapStatistics.score = score
			mapStatistics.mapMax = mapMax
		} else {
			mapStatistics = new MapStatistics(
				user : user,
				self : self,
				type : type,
				result : result,
				selfResultOption : selfResultOption,
				score : score,
				mapMax : mapMax
			)
		}
		mapStatisticsRepository.save(mapStatistics)
	}
}
