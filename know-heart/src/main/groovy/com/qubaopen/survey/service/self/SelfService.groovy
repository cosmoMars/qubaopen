package com.qubaopen.survey.service.self

import java.text.DecimalFormat

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.entity.user.UserSelfTitle
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserSelfTitleRepository
import com.qubaopen.survey.service.user.UserIDCardBindService
import com.qubaopen.survey.utils.DateCommons

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
	
	@Autowired
	UserIDCardBindService userIDCardBindService
	
	@Autowired
	UserIDCardBindRepository userIDCardBindRepository
	
	@Autowired
	UserSelfTitleRepository userSelfTitleRepository
	
	@Autowired
	UserInfoRepository userInfoRepository
		
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 获取自测问卷
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	retrieveSelf(User user, boolean refresh) {
		
		def now = new Date(), resultSelfs = [], allSelfs = [],
			selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTime(user) // 所有用户答过的题目记录，按照时间倒序排序
		
		def index = dayForWeek(),
			singleSelf = selfRepository.findSpecialSelf(),
			idMap = userIDCardBindService.calculateAgeByIdCard(user)
			
		allSelfs << singleSelf
			
		def epqSelfs = selfRepository.findAll( // epq 4套题目
			[
				'selfGroup.name_equal' : 'EPQ'
			]
		)
		allSelfs += epqSelfs
		
		if (idMap) {
			def age = idMap.get('age')
			if (age > 15 && age < 70) {
				epqSelfs.each { epq ->
					def questionnaire = selfUserQuestionnaires.find { suq ->
						epq.id == suq.self.id
					}
					selfUserQuestionnaires.remove(questionnaire)
					if (questionnaire) {
						
						def intervalTime = epq.intervalTime as Long
						if ((now.getTime() - questionnaire.time.getTime()) > intervalTime * 60 * 60 * 1000) {
//						if ((now.getTime() - questionnaire.time.getTime()) > 60 * 1000) {
							resultSelfs << epq
						}
					} else {
						resultSelfs << epq
					}
				}
			}
		} else if (user.userInfo.birthday && user.userInfo.sex){
			def c = Calendar.getInstance()
			c.setTime new Date()
			def age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
			if (age > 15 && age < 70) {
				epqSelfs.each { epq ->
					def questionnaire = selfUserQuestionnaires.find { suq ->
						epq.id == suq.self.id
					}
					selfUserQuestionnaires.remove(questionnaire)
					if (questionnaire) {
						def intervalTime = epq.intervalTime as Long
						if ((now.getTime() - questionnaire.time.getTime()) > intervalTime * 60 * 60 * 1000) {
//						if ((now.getTime() - questionnaire.time.getTime()) > 60 * 1000) {
							resultSelfs << epq
						}
					} else {
						resultSelfs << epq
					}
				}
			}
		}
		
		def userQuestionnaire = selfUserQuestionnaires.find { // 4小时题 记录
			it.self.id == singleSelf.id
		}
		if (userQuestionnaire) { // 判断4小时是否符合时间，符合添加，没有也添加
			
			def intervalTime = singleSelf.intervalTime as Long
			if ((now.getTime() - userQuestionnaire.time.getTime()) > intervalTime * 60 * 60 * 1000) {
//			if ((now.getTime() - userQuestionnaire.time.getTime()) > 60 * 1000) {
				resultSelfs << singleSelf
			}
			selfUserQuestionnaires.remove(userQuestionnaire)
		} else {
			resultSelfs << singleSelf
		}
		
		def existQuestionnaires = selfUserQuestionnaires.findAll {
			def intervalTime = it.self.intervalTime as Long
			(now.getTime() - it.time.getTime()) < intervalTime * 60 * 60 * 1000
//			(now.getTime() - it.time.getTime()) < 60 * 1000
		}
		existQuestionnaires.each {
			allSelfs << it.self
		}
		def todayUserQuestionnaires = selfUserQuestionnaires.findAll { // 每天额外题目
			DateUtils.isSameDay(now, it.time)
		}
		if (index in 1..5) {
//			resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
			
			if (!todayUserQuestionnaires) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
				} else {
					if (!selfUserQuestionnaires) {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
					} else {
						def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.selfManagementType, allSelfs)
						if (relationSelfs) {
							resultSelfs << relationSelfs[new Random().nextInt(relationSelfs.size())]
						} else {
							resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
						}
					}
				}
			}
		} else if (index in 6..7) {
//		resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
		
			if (!todayUserQuestionnaires) {
				if (refresh) {
					resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
				} else {
					if (!selfUserQuestionnaires) {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 2)
					} else {
						def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.selfManagementType, allSelfs)
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
					def relationSelfs = selfRepository.findByTypeWithoutExists(selfUserQuestionnaires[0].self.selfManagementType, allSelfs)
					if (relationSelfs) {
						resultSelfs << relationSelfs[new Random().nextInt(relationSelfs.size())]
					} else {
						resultSelfs += selfRepository.findRandomSelfs(allSelfs, 1)
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = true)
	retrieveSelfByTypeId(User user, long typeId, Self specialSelf) {
		
		def usedSelfs = [], ids = []
		
		def selfUserQuestionnaires = selfUserQuestionnaireRepository.findByMaxTimeAndType(user, typeId),
			existQuestionnaires = selfUserQuestionnaires.findAll {
				def intervalTime = it.self.intervalTime as Long
				((new Date()).getTime() - it.time.getTime()) < intervalTime * 60 * 60 * 1000
			}
		
		ids << specialSelf.id
		if (existQuestionnaires) {
			existQuestionnaires.each {
				ids << it.self.id
			}
		}
		
		def selfs = selfRepository.findByTypeWithoutExistSelfId(typeId, ids)
		
		selfs
		
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
	
	
	/**
	 * 计算 更新用户性格解析度
	 * @param user
	 */
	@Transactional
	calcUserAnalysisRadio(UserInfo userInfo) {
		
		//用户已经做了的必做 选做的数量
		def user = new User(id : userInfo.id),
			userDoMust = selfUserQuestionnaireRepository.findByAnalysis(user, true).size(),
			userDoOther = selfUserQuestionnaireRepository.findByAnalysis(user, false).size()
		
		def userDone 
		if (userDoOther <= 8) {
			userDone = userDoMust + userDoOther
		} else {
			userDone = userDoMust + 8
		}
		
		//必做题总数
		def allMust= selfRepository.findAll(
			['analysis_equal' : true]
		).size()
	
		
		def all= allMust + 8,
			f = userDone / all * 100,
			userSelfTitle=userSelfTitleRepository.findOneByFilters(
				[
					minScore_lessThanOrEqualTo : f,
					maxScore_greaterThan : f
				]			
			)
		userInfo.resolution=f;
		userInfo.userSelfTitle = userSelfTitle;
		
		userInfoRepository.save(userInfo);

		[
			'success' : '1',
			'analysis' : f,
			'name' : userSelfTitle.name
		]
	}

	
	/**
	 * 计算 更新 用户心理健康指数
	 * @param user
	 */
	@Transactional
	calcUserMentalStatus(UserInfo userInfo) {
		
		List<SelfUserQuestionnaire> list1=selfUserQuestionnaireRepository.findByMentalStatus(new User(id : userInfo.id));
		
		StringBuilder sql
		Query query
		List<Long> list
		def minusScore=0;
		
		list1.each {
			
			sql = new StringBuilder();
			sql.append("SELECT id FROM self_result_option ");
			sql.append("where self_result_id=(select id from self_result where self_id="+it.self.id+" ) ");
	
			query = entityManager.createNativeQuery(sql.toString());
	
			list = query.getResultList()
			
			def sro=it.selfResultOption.id
			def index=0;
			for(def i=0;i<list.size();i++){
				if(list.get(i)==sro || list.get(i).equals(sro)){
					index=i;
					break;
				}
			}
			
			
			def score= index/(list.size()-1)*20;
			
			minusScore+=score
		}
		
		def lastSuo=selfUserQuestionnaireRepository.findLastByTimeAndSelfManagementType(new User(id : userInfo.id))
		
		if(lastSuo!=null){
			def differHour=DateCommons.getDifferHour(lastSuo.time,new Date()) / 24;
				
			if(differHour >=14){
				minusScore+=20;
			}else if(differHour >=7 ){
				minusScore+=10;
			}
		} else {
			minusScore = 20
		}
		
		
		DecimalFormat df = new DecimalFormat("0")
		String s = df.format(minusScore)
		
		userInfo.deduction=s;
		
		userInfoRepository.save(userInfo);
		
		[
			'success' : '1',
			'minusScore' : minusScore
		]
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
