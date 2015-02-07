package com.qubaopen.survey.controller.examination;

import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.mindmap.MapRecordRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.reward.RewardActivityRecordRepository
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository
import com.qubaopen.survey.service.self.SelfService

@RestController
@RequestMapping('mental')
@SessionAttributes('currentUser')
public class MentalExaminationController {
	
	static Logger logger = LoggerFactory.getLogger(MentalExaminationController.class)
	
	@Autowired
	SelfRepository selfRepository
	
	@Autowired
	SelfService selfService
	
	@Autowired
	MapStatisticsRepository mapStatisticsRepository
	
	@Autowired
	MapRecordRepository mapRecordRepository
	
	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository
	
	@Autowired
	RewardActivityRepository rewardActivityRepository
	
	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

	/**
	 * @param user
	 * @return
	 * 心理体检
	 */
	@RequestMapping(value = 'retrieveMentalExamination', method = RequestMethod.GET)
	retrieveMentalExamination(@ModelAttribute('currentUser') User user) {
		
//		def data = []
		
		def userAnalysis = selfService.calcUserAnalysisRadio(new UserInfo(id : user.id)),
			analysis = userAnalysis['analysis']
			
		def rewardData = []
		def rewardActivitys = rewardActivityRepository.findAll(
			status_equal : RewardActivity.Status.ONLINE
		)
		
		def activityRecords = rewardActivityRecordRepository.findByUserWithActivity(user, rewardActivitys)
		rewardActivitys.each { ra ->
			def isExchange = false
			if (activityRecords) {
				def completeRecord = activityRecords.find { ar ->
					ra.id == ar.rewardActivity.id
				}
				if (completeRecord) {
					isExchange = true
				}
			}
			rewardData << [
				'activityId' : ra?.id,
				'content' : ra?.content,
				'level' : ra?.rewardType?.rewardLevel?.name,
				'isExchange' : isExchange
			]
			
		}
		
		
		def specialSelf = selfRepository.findSpecialSelf(),
			specialData = [:]
		
		def specialMaps = mapStatisticsRepository.findOneByFilters(
			[
				self_equal : specialSelf,
				user_equal : user
			]
		)// 4小时题目
		def specialRecords = mapRecordRepository.findEveryDayMapRecords(specialMaps)
		
		
		def todayWork = false
		// 检索今天做题
		if (specialRecords != null) {
			def todayRecord = specialRecords.find{ sr ->
				def today = sr.createdDate.toString().split('T')
				DateUtils.isSameDay(new Date(), DateUtils.parseDate(today[0], 'yyyy-MM-dd'))
			}
			if (todayRecord != null) {
				todayWork = true
			}
		}
		
		specialData << [
			'specialId' : specialSelf.id,
			'completeCount' : specialRecords ? specialRecords.size() : 0,
			'done' : todayWork
		]
		
		// 上线比做题
		def allSelf = selfRepository.findAll(
			status_equal : Self.Status.ONLINE,
			analysis_equal : true,
			id_notEqual : specialSelf.id
		)
		
		// 查找做过的题目
		def questionnaires = selfUserQuestionnaireRepository.findAll(
			used_equal : true,
			user_equal : user
		)
		
		def selfMap = [:]
		
		allSelf.each { single ->// 分组
			def done = false,
				doneSelf = questionnaires.find {
						it.self.id == single.id
					}
				if (doneSelf) {
					done = true
				}
				
			if (selfMap.get(single.selfManagementType)) {
				selfMap.get(single.selfManagementType) << [
					'selfId' : single?.id,
					'selfName' : single?.title,
					'version' : single?.version,
					'done' : done
				]
			} else {
				def list = []
				list << [
					'selfId' : single?.id,
					'selfName' : single?.title,
					'version' : single?.version,
					'done' : done
				]
				selfMap.put(single.selfManagementType, list)
			}
		}
		def mapResult = []
		selfMap.each { k, v ->
			mapResult << [
				'managementId' : k?.id,
				'manageMentName' : k?.name,
				'manageData' : v
			]
		}
		
		def otherQuestionnaires = selfUserQuestionnaireRepository.findAll(
			user_equal : user,
			'self.analysis_equal' : 0,
			used_equal : true
		)
		// 计算其他完成率
		def otherRadio
		if (otherQuestionnaires) {
			otherRadio = otherQuestionnaires.size() / 8 * 100
		}
		[
			'success' : 1,
			'reward' : rewardData,
			'analysis' : analysis,
			'specialData' : specialData,
			'selfMap' : mapResult,
			'otherRadio' : otherRadio ?: 0
		]
	}
	
	@RequestMapping(value = 'retrieveAnotherQuestionnaire', method = RequestMethod.POST)
	retrieveAnotherQuestionnaire(@RequestParam(required = false) Long id,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 获取额外问卷 --'
		
		def self
		if (id == null) {
			self = selfRepository.findRandomSelf()
		} else {
			self = selfRepository.findRandomSelfWithoutExist(id)
		}
		
		[
			'success' : "1",
			'selfId' : self?.id,
			'managementType' : self?.selfManagementType?.id,
			'title' : self?.title,
			'version' : self?.version
		]
		
	}
	
}
