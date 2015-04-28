package com.knowheart3.service.mindmap

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.knowheart3.repository.EPQBasicRepository
import com.knowheart3.repository.mindmap.MapCoefficientRepository
import com.knowheart3.repository.mindmap.MapRecordRepository
import com.knowheart3.repository.mindmap.MapStatisticsRepository
import com.knowheart3.repository.mindmap.MapStatisticsTypeRepository
import com.knowheart3.repository.self.SelfGroupRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.self.SelfResultOptionRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.knowheart3.repository.user.UserMoodRecordRepository
import com.knowheart3.service.user.UserIDCardBindService
import com.qubaopen.survey.entity.mindmap.MapCoefficient
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfManagementType
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserMoodRecord

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsTypeRepository mapStatisticsTypeRepository

	@Autowired
	SelfGroupRepository selfGroupRepository

	@Autowired
	ObjectMapper objectMapper
	
	@Autowired
	CalculateT calculateT
	
	@Autowired
	UserIDCardBindService userIDCardBindService
	
	@Autowired
	EPQBasicRepository epqBasicRepository
	
	@Autowired
	SelfRepository selfRepository

	@Autowired
	MapRecordRepository mapRecordRepository
	
	@Autowired
	CalculatePoint calculatePoint
	
	@Autowired
	SelfResultOptionRepository selfResultOptionRepository
	
	@Autowired
	MapCoefficientRepository mapCoefficientRepository
	
	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository
	
	@Autowired
	UserMoodRecordRepository userMoodRecordRepository
	
	/**
	 * 获取心理地图
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional
	newRetrieveMapStatistics(User user, Long typeId) {

		def data = [], existMaps = []
			
//		def specialSelf = selfRepository.findSpecialSelf()
		def	specialGroup = selfGroupRepository.findSpecialSelfGroup()
		
		if (typeId != null) {
			
			def selfManagementType = new SelfManagementType(id : typeId)
//			def specialMaps = mapStatisticsRepository.findOneByFilters(
//					[
//						self_equal : specialSelf,
//						user_equal : user,
//						selfManagementType_equal : selfManagementType
//					]
//				)
			
//			if (specialMaps) {
//				existMaps += specialMaps
//			}
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(selfManagementType, user, specialGroup, true)
//			existMaps += existGroupMaps
			
			def singleMaps = []
//			if (existMaps) {
//				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, selfManagementType, user)
//			} else {
//				singleMaps = mapStatisticsRepository.findBySelfManagementTypeAndUser(selfManagementType, user)
//			}
			def groupResultMaps = [:]
			groupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
					
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'groupId' : k?.id,
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else if (v.size() == 1 && k.selfs.size() == 1) {
					singleMaps += v
				} else if (v.size() == k.selfs.size() && k.selfs.size() > 1) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
						}
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						def resultStr = MapContent.epqTitle
						
						if (recordMaps['E'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['N']) {
							resultStr += MapContent.EGreaterThanPN
						} else if (recordMaps['E'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['P']) {
							resultStr += MapContent.EGreaterThanNP
						} else if (recordMaps['P'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['N']) {
							resultStr += MapContent.PGreaterThanEN
						} else if (recordMaps['P'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['E']) {
							resultStr += MapContent.PGreaterThanNE
						} else if (recordMaps['N'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['E']) {
							resultStr += MapContent.NGreaterThanPE
						} else if (recordMaps['N'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['P']) {
							resultStr += MapContent.NGreaterThanEP
						}
						if (recordMaps['L'] > 60) {
							resultStr += MapContent.lieL
						}
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						def resultStr = '', contentMap = [:] as Map
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
									contentMap.put(it.name, it.value)
								}
							}
						}
						if ('ABCD' == k?.name || 'ABCD'.equals(k?.name)) {
							resultStr = MapContent.abcdTitle
							
							// AB
							if (contentMap['AB'] >= 120 && contentMap['AB'] <= 200) {
								resultStr += MapContent.extremeA
							} else if (contentMap['AB'] >= 106 && contentMap['AB'] <= 119) {
								resultStr += MapContent.obviousA
							} else if (contentMap['AB'] >= 100 && contentMap['AB'] <= 105) {
								resultStr += MapContent.tendencyA
							} else if (contentMap['AB'] >= 90 && contentMap['AB'] <= 99) {
								resultStr += MapContent.extremeB
							} else if (contentMap['AB'] >= 0 && contentMap['AB'] <= 89) {
								resultStr += MapContent.tendencyB
							}
							// C
							if (contentMap['C'] >= 14 && contentMap['C'] <= 100) {
								resultStr += MapContent.typicalC
							} else if (contentMap['C'] >= 7 && contentMap['C'] <= 13) {
								resultStr += MapContent.tendencyC
							} else if (contentMap['C'] >= 0 && contentMap['C'] <= 6) {
								resultStr += MapContent.notC
							}
							
							// D
							if (contentMap['D'] >= 0 && contentMap['D'] <= 9) {
								resultStr += MapContent.D1
							} else if (contentMap['D'] >= 10 && contentMap['D'] <= 15) {
								resultStr += MapContent.D2
							} else if (contentMap['D'] >= 16 && contentMap['D'] <= 18) {
								resultStr += MapContent.D3
							} else if (contentMap['D'] >= 19 && contentMap['D'] <= 27) {
								resultStr += MapContent.D4
							} else if (contentMap['D'] >= 28 && contentMap['D'] <= 36) {
								resultStr += MapContent.D5
							} else if (contentMap['D'] >= 37 && contentMap['D'] <= 100) {
								resultStr += MapContent.D6
							}
						}
						
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
					
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = [], resultMaps = [], strResult = ''
//				if (it?.self?.graphicsType) {
//					it.mapRecords.each {
//						chart << [name : it.name, value : it.value]
//					}
//				}
				if (it?.self?.id && (10l == it?.self?.id || 13l == it?.self?.id || 14l == it?.self?.id)) {
					println it?.self?.id
					resultMaps = selfUserQuestionnaireRepository.findBySelfAndUserOrderByTimeAsc(it?.self, user)
					if (resultMaps.size() > 1) {
						resultMaps.each {
							chart << [name : it.time.getTime(), value : it.score]
						}
					}
				} else if (it?.self?.id && (8l == it?.self?.id || 16l == it?.self?.id)) {
					def resultMapRecords = mapRecordRepository.findBySelfAndUser(it?.self, user),
						moodMaps = [:]
					resultMapRecords.each {
						moodMaps.put(it.name, it.value)
					}
					if ('ADULT' == it.self.abbreviation) {
						if (moodMaps['Tenacity']) {
							chart << [name : '坚韧性', value : moodMaps['Tenacity']]
						} else {
							chart << [name : '坚韧性', value : 0]
						}
						if (moodMaps['Optimism']) {
							chart << [name : '乐观性', value : moodMaps['Optimism']]
						} else {
							chart << [name : '乐观性', value : 0]
						}
						if (moodMaps['Poisedness']) {
							chart << [name : '自若性', value : moodMaps['Poisedness']]
						} else {
							chart << [name : '自若性', value : 0]
						}
						if (moodMaps['Persistence']) {
							chart << [name : '执着性', value : moodMaps['Persistence']]
						} else {
							chart << [name : '执着性', value : 0]
						}
						
					}
					if ('MBTI' == it.self.abbreviation) {
						if (moodMaps['J']) {
							chart << [name : '判断（J）', value : moodMaps['J']]
						} else {
							chart << [name : '判断（J）', value : 0]
						}
						if (moodMaps['T']) {
							chart << [name : '思考（T）', value : moodMaps['T']]
						} else {
							chart << [name : '思考（T）', value : 0]
						}
						if (moodMaps['S']) {
							chart << [name : '实感（S）', value : moodMaps['S']]
						} else {
							chart << [name : '实感（S）', value : 0]
						}
						if (moodMaps['E']) {
							chart << [name : '外向（E）', value : moodMaps['E']]
						} else {
							chart << [name : '外向（E）', value : 0]
						}
						if (moodMaps['P']) {
							chart << [name : '（P）感觉', value : moodMaps['P']]
						} else {
							chart << [name : '（P）感觉', value : 0]
						}
						if (moodMaps['F']) {
							chart << [name : '（F）情感', value : moodMaps['F']]
						} else {
							chart << [name : '（F）情感', value : 0]
						}
						if (moodMaps['N']) {
							chart << [name : '（N）直觉', value : moodMaps['N']]
						} else {
							chart << [name : '（N）直觉', value : 0]
						}
						if (moodMaps['I']) {
							chart << [name : '（I）内向', value : moodMaps['I']]
						} else {
							chart << [name : '（I）内向', value : 0]
						}
					}
					
				} else if (it?.self?.selfGroup?.graphicsType){
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'groupId' : '',
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}
				strResult = it?.selfResultOption?.content
				if ('SDS' == it?.self?.selfGroup?.name) {
					strResult += ('\n' + MapContent.sds)
				}
				data << [
					'groupId' : it?.self?.selfGroup?.id,
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.self?.selfGroup?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : strResult,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.selfGroup?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		
		} else {
		
//			def specialMaps = mapStatisticsRepository.findOneByFilters(
//				[
//					self_equal : specialSelf,
//					user_equal : user
//				]
//			)// 4小时题目
//			if (specialMaps) {
//				existMaps += specialMaps
//			}
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(user, specialGroup, true)
			
//			existMaps += groupMaps
			def singleMaps = []
			
//			if (existMaps) {
//				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, user)
//			} else {
//				singleMaps = mapStatisticsRepository.findAll(
//					['user_equal' : user]
//				)
//			}
			def groupResultMaps = [:]
			
			groupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'groupId' : k?.id,
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else if (v.size() == 1 && k.selfs.size() == 1) {
					singleMaps += v
				} else if (v.size() == k.selfs.size() && k.selfs.size() > 1) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
							
						}
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						def resultStr = MapContent.epqTitle
						
						if (recordMaps['E'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['N']) {
							resultStr += MapContent.EGreaterThanPN
						} else if (recordMaps['E'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['P']) {
							resultStr += MapContent.EGreaterThanNP
						} else if (recordMaps['P'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['N']) {
							resultStr += MapContent.PGreaterThanEN
						} else if (recordMaps['P'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['E']) {
							resultStr += MapContent.PGreaterThanNE
						} else if (recordMaps['N'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['E']) {
							resultStr += MapContent.NGreaterThanPE
						} else if (recordMaps['N'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['P']) {
							resultStr += MapContent.NGreaterThanEP
						}
						if (recordMaps['L'] > 60) {
							resultStr += MapContent.lieL
						}
						
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						def resultStr = '', contentMap = [:] as Map
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
									contentMap.put(it.name, it.value)
								}
							}
						}
						if ('ABCD' == k?.name || 'ABCD'.equals(k?.name)) {
							resultStr = MapContent.abcdTitle
							
							// AB
							if (contentMap['AB'] >= 120 && contentMap['AB'] <= 200) {
								resultStr += MapContent.extremeA
							} else if (contentMap['AB'] >= 106 && contentMap['AB'] <= 119) {
								resultStr += MapContent.obviousA
							} else if (contentMap['AB'] >= 100 && contentMap['AB'] <= 105) {
								resultStr += MapContent.tendencyA
							} else if (contentMap['AB'] >= 90 && contentMap['AB'] <= 99) {
								resultStr += MapContent.extremeB
							} else if (contentMap['AB'] >= 0 && contentMap['AB'] <= 89) {
								resultStr += MapContent.tendencyB
							}
							// C
							if (contentMap['C'] >= 14 && contentMap['C'] <= 100) {
								resultStr += MapContent.typicalC
							} else if (contentMap['C'] >= 7 && contentMap['C'] <= 13) {
								resultStr += MapContent.tendencyC
							} else if (contentMap['C'] >= 0 && contentMap['C'] <= 6) {
								resultStr += MapContent.notC
							}
							
							// D
							if (contentMap['D'] >= 0 && contentMap['D'] <= 9) {
								resultStr += MapContent.D1
							} else if (contentMap['D'] >= 10 && contentMap['D'] <= 15) {
								resultStr += MapContent.D2
							} else if (contentMap['D'] >= 16 && contentMap['D'] <= 18) {
								resultStr += MapContent.D3
							} else if (contentMap['D'] >= 19 && contentMap['D'] <= 27) {
								resultStr += MapContent.D4
							} else if (contentMap['D'] >= 28 && contentMap['D'] <= 36) {
								resultStr += MapContent.D5
							} else if (contentMap['D'] >= 37 && contentMap['D'] <= 100) {
								resultStr += MapContent.D6
							}
						}
		
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = [], resultMaps = [], strResult = ''
//				if (it?.self?.graphicsType) {
//					it.mapRecords.each {
//						chart << [name : it.name, value : it.value]
//					}
//				}
				if (it?.self?.id && (10l == it?.self?.id || 13l == it?.self?.id || 14l == it?.self?.id)) {
					println it?.self?.id
					resultMaps = selfUserQuestionnaireRepository.findBySelfAndUserOrderByTimeAsc(it?.self, user)
					if (resultMaps.size() > 1) {
						resultMaps.each {
							chart << [name : it.time.getTime(), value : it.score]
						}
					}
				} else if (it?.self?.id && (8l == it?.self?.id || 16l == it?.self?.id)) {
					def resultMapRecords = mapRecordRepository.findBySelfAndUser(it?.self, user),
						moodMaps = [:]
					resultMapRecords.each {
						moodMaps.put(it.name, it.value)
					}
					if ('ADULT' == it.self.abbreviation) {
						if (moodMaps['Tenacity']) {
							chart << [name : '坚韧性', value : moodMaps['Tenacity']]
						} else {
							chart << [name : '坚韧性', value : 0]
						}
						if (moodMaps['Optimism']) {
							chart << [name : '乐观性', value : moodMaps['Optimism']]
						} else {
							chart << [name : '乐观性', value : 0]
						}
						if (moodMaps['Poisedness']) {
							chart << [name : '自若性', value : moodMaps['Poisedness']]
						} else {
							chart << [name : '自若性', value : 0]
						}
						if (moodMaps['Persistence']) {
							chart << [name : '执着性', value : moodMaps['Persistence']]
						} else {
							chart << [name : '执着性', value : 0]
						}
						
					}
					if ('MBTI' == it.self.abbreviation) {
						if (moodMaps['J']) {
							chart << [name : '判断（J）', value : moodMaps['J']]
						} else {
							chart << [name : '判断（J）', value : 0]
						}
						if (moodMaps['T']) {
							chart << [name : '思考（T）', value : moodMaps['T']]
						} else {
							chart << [name : '思考（T）', value : 0]
						}
						if (moodMaps['S']) {
							chart << [name : '实感（S）', value : moodMaps['S']]
						} else {
							chart << [name : '实感（S）', value : 0]
						}
						if (moodMaps['E']) {
							chart << [name : '外向（E）', value : moodMaps['E']]
						} else {
							chart << [name : '外向（E）', value : 0]
						}
						if (moodMaps['P']) {
							chart << [name : '（P）感觉', value : moodMaps['P']]
						} else {
							chart << [name : '（P）感觉', value : 0]
						}
						if (moodMaps['F']) {
							chart << [name : '（F）情感', value : moodMaps['F']]
						} else {
							chart << [name : '（F）情感', value : 0]
						}
						if (moodMaps['N']) {
							chart << [name : '（N）直觉', value : moodMaps['N']]
						} else {
							chart << [name : '（N）直觉', value : 0]
						}
						if (moodMaps['I']) {
							chart << [name : '（I）内向', value : moodMaps['I']]
						} else {
							chart << [name : '（I）内向', value : 0]
						}
					}
					
				} else if (it?.self?.selfGroup?.graphicsType){
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'groupId' : '',
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}
				strResult = it?.selfResultOption?.content
				if ('SDS' == it?.self?.selfGroup?.name) {
					strResult += ('\n' + MapContent.sds)
				}
				data << [
					'groupId' : it?.self?.selfGroup?.id,
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.self?.selfGroup?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : strResult,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.selfGroup?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		}
		[
			'success' : '1',
			'message' : '成功',
			'userId' : user.id,
			'data' : data
		]
	}
	
	@Transactional
	retrieveSpecialMap(User user) {
		
		def data = [], tip
		def specialSelf = selfRepository.findSpecialSelf()
		def specialMap = mapStatisticsRepository.findOneByFilters(
			[
				self_equal : specialSelf,
				user_equal : user
			]
		)// 4小时题目
		def specialMapRecords
		if (specialMap) {
			specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMap)
		}
		if (!specialMap) {
			tip = "亲，为准确推断您的情绪周期\n    至少需要记录七天数据哦~\n       加油！7天后就有惊喜~ " as String
		} else if (specialMap && specialMapRecords.size() >= 7) { // 特殊题
			def chart, c = []
			def resultContent = "err"
			if (specialMap?.self?.graphicsType) {
				def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
				Collections.sort(specialMapRecords, new MapRecordComparator())
				specialMapRecords.each {
					def thatTime = new Date(it.name as long),
						time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
					timeChart << time.getTime()
					paChart << it.value
					naChart << -it.naValue
					midChart << (it.value - it.naValue)
				}

				def paChartC = [], naChartC = [], midChartC = []
				
				def coefficient = mapCoefficientRepository.findOne(user.id)
				// 计算时间差
				if (coefficient) {
					def chartTime = new Date(timeChart[timeChart.size() - 1] as long) 
					if (chartTime - coefficient.time > 0) {
						paChartC = calculatePoint.getPoint(timeChart, paChart)
						naChartC = calculatePoint.getPoint(timeChart, naChart)
						midChartC = calculatePoint.getPoint(timeChart, midChart)
						coefficient.pa1 = paChartC[0]
						coefficient.pa2 = paChartC[1]
						coefficient.pa3 = paChartC[2]
						coefficient.pa4 = paChartC[3]
						coefficient.na1 = naChartC[0]
						coefficient.na2 = naChartC[1]
						coefficient.na3 = naChartC[2]
						coefficient.na4 = naChartC[3]
						coefficient.mid1 = midChartC[0]
						coefficient.mid2 = midChartC[1]
						coefficient.mid3 = midChartC[2]
						coefficient.mid4 = midChartC[3]
						coefficient.time = chartTime
						mapCoefficientRepository.save(coefficient)
					}
				} else {
					paChartC = calculatePoint.getPoint(timeChart, paChart)
					naChartC = calculatePoint.getPoint(timeChart, naChart)
					midChartC = calculatePoint.getPoint(timeChart, midChart)
					coefficient = new MapCoefficient(
						id : user.id,
						pa1 : paChartC[0],
						pa2 : paChartC[1],
						pa3 : paChartC[2],
						pa4 : paChartC[3],
						na1 : naChartC[0],
						na2 : naChartC[1],
						na3 : naChartC[2],
						na4 : naChartC[3],
						mid1 : midChartC[0],
						mid2 : midChartC[1],
						mid3 : midChartC[2],
						mid4 : midChartC[3],
						time : new Date(timeChart[timeChart.size() - 1] as long)
					)
					mapCoefficientRepository.save(coefficient)
				}
				
				//计算 正负情感趋势 上升 下降
				def time = System.currentTimeMillis(),
					timeBefore = time - 60 * 60 * 24 * 1000 * 2,
					timeAfter = time + 60 * 60 * 24 * 1000 * 2
				
				def resultToday = coefficient.mid1 + coefficient.mid2 * Math.cos(time * coefficient.mid4) + coefficient.mid3 * Math.sin(time * coefficient.mid4)
				def resultBefore = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
				def resultAfter = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
				
				def selfUserQuestionnaire = selfUserQuestionnaireRepository.findByUserAndSelfAndUsed(user, new Self(id : 17l), true),
					moodContent = ''
				if (selfUserQuestionnaire) {
					if (137l == selfUserQuestionnaire.selfResultOption.id || '137'.equals(selfUserQuestionnaire.selfResultOption.id)) {
						moodContent = MapContent.outbound
					}else if (138l == selfUserQuestionnaire.selfResultOption.id || '138'.equals(selfUserQuestionnaire.selfResultOption.id)) {
						moodContent = MapContent.within
					}
				}	
			
				if(resultBefore <= resultToday && resultToday < resultAfter){ // 上升
					resultContent = MapContent.lowToHighTitle + MapContent.lowToHighContent + moodContent + MapContent.lowToHighMethod
				}else if (resultBefore > resultToday && resultToday >= resultAfter){ // 下降
					resultContent = MapContent.highToLowTitle + MapContent.highToLowContent + moodContent + MapContent.highToLowMethod
				}else if (resultBefore <= resultToday && resultToday >= resultAfter){ // 最高处
					resultContent = MapContent.highTideTitle + MapContent.highTideContent + moodContent
				}else if (resultBefore > resultToday && resultToday < resultAfter){ // 最底处
					resultContent = MapContent.lowTideTitle + MapContent.lowTideContent + moodContent + MapContent.lowTideMethod
				}
				
				chart = [
					timeChart : timeChart,
					midChart : midChart,
					paChartC : [coefficient.pa1, coefficient.pa2, coefficient.pa3, coefficient.pa4],
					naChartC : [coefficient.na1, coefficient.na2, coefficient.na3, coefficient.na4],
					midChartC : [coefficient.mid1, coefficient.mid2, coefficient.mid3, coefficient.mid4]
				]
				
				c << chart
			}
			data << [
				'groupId' : specialMap?.self?.selfGroup?.id,
				'chart' : c,
				'mapMax' : specialMap?.mapMax,
				'resultName' : specialMap?.selfResultOption?.name,
				'resultScore' : '',
				'resultContent' : resultContent,
				'managementType' : specialMap?.selfManagementType?.id,
				'recommendedValue' : specialMap?.recommendedValue,
				'graphicsType' : specialMap?.self?.graphicsType?.id,
				'special' : true,
				'lock' : false
			]
		} else if (specialMap && specialMapRecords.size() < 7) {
			def days = 7 - specialMapRecords.size()
			tip = "亲，为准确推断您的情绪周期\n    至少需要记录七天数据哦~\n       加油！${days}天后就有惊喜~ " as String
			data << [
				'groupId' : specialMap?.self?.selfGroup?.id,
				'chart' : '',
				'mapMax' : '',
				'resultName' : '',
				'resultScore' : '',
				'resultContent' : '',
				'managementType' : specialMap?.selfManagementType?.id,
				'recommendedValue' : specialMap?.recommendedValue,
				'graphicsType' : specialMap?.self?.graphicsType?.id,
				'special' : true,
				'lock' : true
//				'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
			]
		}
		def result = [
			'success' : '1',
			'message' : '成功',
			'specialId' : specialSelf.id,
			'mapTitle' : specialMap?.self?.title,
			'userId' : user.id,
			'data' : data
		]
		if (tip) {
			result << ['tips' : tip]
		}
		result
	}
	
	class MapRecordComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			MapRecord m1 = (MapRecord) o1
			MapRecord m2 = (MapRecord) o2
			return m1.createdDate.compareTo(m2.createdDate)
		}
	}
	
	@Transactional
	retrieveMapStatistics(User user, long typeId) {

		def data = [], existMaps = []
			
		def specialSelf = selfRepository.findSpecialSelf()
		if (typeId == 4l) {
			
			def specialMaps = mapStatisticsRepository.findOneByFilters(
				[
					self_equal : specialSelf,
					user_equal : user
				]
			)// 4小时题目
			def specialMapRecords
			if (specialMaps) {
				existMaps += specialMaps
				specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMaps)
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs(user)
			
			existMaps += existGroupMaps
			def singleMaps
			
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, user)
			} else {
				singleMaps = mapStatisticsRepository.findAll(
					['user_equal' : user]
				)
			}
			
			if (specialMaps && specialMapRecords.size() >= 7) { // 特殊题
				def chart
				def c = []
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
					Collections.sort(specialMapRecords, new MapRecordComparator())
					specialMapRecords.each {
						def thatTime = new Date(it.name as long),
							time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
						timeChart << time.getTime()
						paChart << it.value
						naChart << -it.naValue
						midChart << (it.value - it.naValue)
					}
					
					def paChartC = calculatePoint.getPoint(timeChart, paChart)
					def naChartC = calculatePoint.getPoint(timeChart, naChart)
					def midChartC = calculatePoint.getPoint(timeChart, midChart)
					
					chart = [
						timeChart : timeChart,
						midChart : midChart,
						paChartC : paChartC,
						naChartC : naChartC,
						midChartC : midChartC
					]
					
					c << chart
				}
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : c,
					'mapMax' : specialMaps?.mapMax,
					'resultName' : specialMaps?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true,
					'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
				]
			}
			
			def groupResultMaps = [:]
			
			existGroupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else if (v.size() == k.selfs.size()) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
							
						}
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
								}
							}
						}
		
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}
				
				data << [
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		} else {
			def selfManagementType = new SelfManagementType(id : typeId)
			def specialMaps = mapStatisticsRepository.findOneByFilters(
					[
						self_equal : specialSelf,
						user_equal : user,
						selfManagementType_equal : selfManagementType
					]
				)
			
			def specialMapRecords
			if (specialMaps) {
				existMaps += specialMaps
				specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMaps)
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs(selfManagementType, user)
			existMaps += existGroupMaps
			
			def singleMaps
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, selfManagementType, user)
			} else {
				singleMaps = mapStatisticsRepository.findBySelfManagementTypeAndUser(selfManagementType, user)
			}
			if (specialMaps && specialMapRecords.size() >= 7) { // 特殊题
				def chart
				def c = []
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
					Collections.sort(specialMapRecords, new MapRecordComparator())
					specialMapRecords.each {
						def thatTime = new Date(it.name as long),
							time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
						timeChart << time.getTime()
						paChart << it.value
						naChart << -it.naValue
						midChart << (it.value - it.naValue)
					}
						def paChartC = calculatePoint.getPoint(timeChart, paChart)
					def naChartC = calculatePoint.getPoint(timeChart, naChart)
					def midChartC = calculatePoint.getPoint(timeChart, midChart)
					
					chart = [
						timeChart : timeChart,
						midChart : midChart,
						paChartC : paChartC,
						naChartC : naChartC,
						midChartC : midChartC
					]
					c << chart
				}
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : c,
					'mapMax' : specialMaps?.mapMax,
					'resultName' : specialMaps?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps.self.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true,
					'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
				]
			}
			
			def groupResultMaps = [:]
			
			existGroupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
					
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else if (v.size() == k.selfs.size()) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
						}
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
								}
							}
						}
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
					
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}
				
				data << [
					
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		
		}
		[
			'success' : '1',
			'message' : '成功',
			'userId' : user.id,
			'data' : data
		]
	}
	
	@Transactional
	def retrieveMoodRecord(User user, Date date) {
		
		def c = Calendar.getInstance()
		
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		def lastDate
		if (!DateUtils.isSameDay(date, c.getTime()) && date > c.getTime()) {
			date = c.getTime()
			c.add(Calendar.DATE, -1)
			
			lastDate = c.getTime()
		}
		
		// 查找最后一条纪录
		def maxMoodRecord = userMoodRecordRepository.findByMaxTime(user)
		def mr = userMoodRecordRepository.findByUserAndTime(user, lastDate),
			
			timeChart = [], paChart = [], naChart = [], midChart = [], statusChart = []
			
		def coefficient = mapCoefficientRepository.findOne(user.id)
		
		def moodRecords = [], showRecords = []
		
		if (mr != null && mr.size() == 0 && maxMoodRecord == null) {
			def specialSelf = selfRepository.findSpecialSelf()
			def specialMap = mapStatisticsRepository.findOneByFilters(
				[
					self_equal : specialSelf,
					user_equal : user
				]
			)// 4小时题目
			def specialMapRecords
			if (specialMap) {
				specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMap)
			}
			
			if (specialMapRecords && specialMapRecords.size() >= 7) {
				c.setTime new Date()
				
				c.set(Calendar.HOUR_OF_DAY, 12)
				c.set(Calendar.MINUTE, 0)
				c.set(Calendar.SECOND, 0)
				
				if (coefficient) {
				
					for (i in 1..2) {
						if (i > 1)
							c.add(Calendar.DATE, -1)
						def cTime = c.getTimeInMillis(),
							paValue = coefficient.pa1 + coefficient.pa2 * Math.cos(cTime * coefficient.pa4) + coefficient.pa3 * Math.sin(cTime * coefficient.pa4),
							naValue = coefficient.na1 + coefficient.na2 * Math.cos(cTime * coefficient.na4) + coefficient.na3 * Math.sin(cTime * coefficient.na4)
				
						def userMoodRecord = new UserMoodRecord(
							user : user,
							time : c.getTime(),
							pa : paValue,
							na : naValue
						)
						
						def timeBefore = cTime - 60 * 60 * 24 * 1000 * 2,
						timeAfter = cTime + 60 * 60 * 24 * 1000 * 2
					
						def now = coefficient.mid1 + coefficient.mid2 * Math.cos(cTime * coefficient.mid4) + coefficient.mid3 * Math.sin(cTime * coefficient.mid4)
						def before = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
						def after = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
						
						if (before <= now && now < after){ // 上升
							userMoodRecord.status = UserMoodRecord.Status.LowToHigh
						} else if (before > now && now >= after){ // 下降
							userMoodRecord.status = UserMoodRecord.Status.HighToLow
						} else if (before <= now && now >= after){ // 最高处
							userMoodRecord.status = UserMoodRecord.Status.HighTide
						} else if (before > now  && now < after){ // 最底处
							userMoodRecord.status = UserMoodRecord.Status.LowTide
						}
						if (i > 1) {
							moodRecords << userMoodRecord
						}
						if (userMoodRecord.time >= date) {
							showRecords << userMoodRecord
						}
					
					}
					userMoodRecordRepository.save(moodRecords)
				}
			}
		} else {
		
			// 日期后的数据
			def eMoodRecords = userMoodRecordRepository.findByUserAndTime(user, date)
			
			eMoodRecords.each {
				timeChart << it.time.getTime()
				paChart << it.pa
				naChart << -it.na
				midChart << (it.pa - it.na)
				statusChart << it?.status?.ordinal()
			}
			
			def nowTime = new Date()
			if (maxMoodRecord && !DateUtils.isSameDay(maxMoodRecord.time, nowTime)) {
				def days = (nowTime.getTime() - maxMoodRecord.time.getTime()) / 1000 / 60 / 60 / 24 as int
				
				c.setTime maxMoodRecord.time
				// 循环天数设置纪录
				for (i in 1..days) {
					c.add(Calendar.DATE, 1)
					c.set(Calendar.HOUR_OF_DAY, 12)
					c.set(Calendar.MINUTE, 0)
					c.set(Calendar.SECOND, 0)
					
					// 计算pa，na
					def cTime = c.getTimeInMillis(),
						paValue = coefficient.pa1 + coefficient.pa2 * Math.cos(cTime * coefficient.pa4) + coefficient.pa3 * Math.sin(cTime * coefficient.pa4),
						naValue = coefficient.na1 + coefficient.na2 * Math.cos(cTime * coefficient.na4) + coefficient.na3 * Math.sin(cTime * coefficient.na4)
			
					def userMoodRecord = new UserMoodRecord(
						user : user,
						time : c.getTime(),
						pa : paValue,
						na : naValue
					)
					
					def timeBefore = cTime - 60 * 60 * 24 * 1000 * 2,
						timeAfter = cTime + 60 * 60 * 24 * 1000 * 2
				
					def now = coefficient.mid1 + coefficient.mid2 * Math.cos(cTime * coefficient.mid4) + coefficient.mid3 * Math.sin(cTime * coefficient.mid4)
					def before = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
					def after = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
					
					// 计算状态
					if (before <= now && now < after){ // 上升
						userMoodRecord.status = UserMoodRecord.Status.LowToHigh
					} else if (before > now && now >= after){ // 下降
						userMoodRecord.status = UserMoodRecord.Status.HighToLow
					} else if (before <= now && now >= after){ // 最高处
						userMoodRecord.status = UserMoodRecord.Status.HighTide
					} else if (before > now  && now < after){ // 最底处
						userMoodRecord.status = UserMoodRecord.Status.LowTide
					}
					if (userMoodRecord.time >= date) {
						showRecords << userMoodRecord
					}
					
					if (i < days) {
						moodRecords << userMoodRecord
					}
				}
				userMoodRecordRepository.save(moodRecords)
			}
		}
		showRecords.reverse().each {
			timeChart << it.time.getTime()
			paChart << it.pa
			naChart << -it.na
			midChart << (it.pa - it.na)
			statusChart << it.status?.ordinal()
		}
		[
			'success' : '1',
			'timeChart' : timeChart,
			'paChart' : paChart,
			'naChart' : naChart,
			'midChart' : midChart,
			'statusChart' : statusChart
		]
	}

	/**
	 * 通过用户结果答案查找map
	 * @param resultId
	 * @param user
	 * @return
	 */
	def retrieveMapByResultAndUser(long selfId, User user) {


		def data = []

//		def userSelfResult = selfResultOptionRepository.findOne(resultId),
		def self = selfRepository.findOne(selfId),
			selfGroup = self?.selfGroup


		// 查找答案
		def mStatistics = mapStatisticsRepository.findBySelfGroupAndUser(selfGroup, user)
		def groupResultMaps = [:]

		mStatistics.each {
			if (groupResultMaps.get(it.self.selfGroup)) {
				groupResultMaps.get(it.self.selfGroup) << it
			} else {
				def tempMap = []
				tempMap << it
				groupResultMaps.put(it.self.selfGroup, tempMap)
			}
		}

		// 情绪预测题
		def	specialGroup = selfGroupRepository.findSpecialSelfGroup()

		def singleMaps = []



		groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
			if (v.size() < k.selfs.size() && v != null) {

				def allName = []
				def completeName = [] as Set
				k.selfs.each {
					allName << it.abbreviation
				}
				v.each { s ->
					s.mapRecords.each {
						if (allName.contains(it.name)) {
							completeName << it.name
						}
					}
				}
				def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
				data << [
						'groupId' : k?.id,
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
				]
			} else if (v.size() == 1 && k.selfs.size() == 1) {
				singleMaps += v
			} else if (v.size() == k.selfs.size() && k.selfs.size() > 1) {
				def records = []
				def recordMaps = [:]
				def chart = []
				if (k.name == 'EPQ') {
					v.each { s ->
						records += s.mapRecords
					}

					v.each { s ->
						s.mapRecords.each {
							if (recordMaps.get(it.name)) {
								recordMaps.get(it.name) << it
							} else {
								def tempList = []
								tempList << it
								recordMaps.put(it.name, tempList)
							}
						}
					}

					recordMaps.each { rk, rv ->
						def score = 0
						rv.each {
							score += it.value
						}
						def idMap = userIDCardBindService.calculateAgeByIdCard(user)

						def age, sex
						if (idMap) {
							age = idMap.get('age')
							sex = idMap.get('sex')
						} else {
							def c = Calendar.getInstance()
							c.setTime new Date()
							age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
							sex = user.userInfo.sex.ordinal()
						}

						def epqBasic = epqBasicRepository.findOneByFilters(
								[
										'minAge_lessThanOrEqualTo' : age,
										'maxAge_greaterThanOrEqualTo' : age,
										'sex_equal' : sex,
										'name_equal' : rk
								]
						)
						def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
						recordMaps.get(rk).clear()
						recordMaps.put(rk, tScore)
					}

					def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
					def resultStr = MapContent.epqTitle

					if (recordMaps['E'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['N']) {
						resultStr += MapContent.EGreaterThanPN
					} else if (recordMaps['E'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['P']) {
						resultStr += MapContent.EGreaterThanNP
					} else if (recordMaps['P'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['N']) {
						resultStr += MapContent.PGreaterThanEN
					} else if (recordMaps['P'] >= recordMaps['N'] && recordMaps['N'] >= recordMaps['E']) {
						resultStr += MapContent.PGreaterThanNE
					} else if (recordMaps['N'] >= recordMaps['P'] && recordMaps['P'] >= recordMaps['E']) {
						resultStr += MapContent.NGreaterThanPE
					} else if (recordMaps['N'] >= recordMaps['E'] && recordMaps['E'] >= recordMaps['P']) {
						resultStr += MapContent.NGreaterThanEP
					}
					if (recordMaps['L'] > 60) {
						resultStr += MapContent.lieL
					}
					data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
					]
				} else {
					chart = []
					def resultStr = '', contentMap = [:] as Map
					if (k?.graphicsType) {
						v.each { s ->
							s.mapRecords.each {
								chart << [name : it.name, value : it.value]
								contentMap.put(it.name, it.value)
							}
						}
					}
					if ('ABCD' == k?.name || 'ABCD'.equals(k?.name)) {
						resultStr = MapContent.abcdTitle

						// AB
						if (contentMap['AB'] >= 120 && contentMap['AB'] <= 200) {
							resultStr += MapContent.extremeA
						} else if (contentMap['AB'] >= 106 && contentMap['AB'] <= 119) {
							resultStr += MapContent.obviousA
						} else if (contentMap['AB'] >= 100 && contentMap['AB'] <= 105) {
							resultStr += MapContent.tendencyA
						} else if (contentMap['AB'] >= 90 && contentMap['AB'] <= 99) {
							resultStr += MapContent.extremeB
						} else if (contentMap['AB'] >= 0 && contentMap['AB'] <= 89) {
							resultStr += MapContent.tendencyB
						}
						// C
						if (contentMap['C'] >= 14 && contentMap['C'] <= 100) {
							resultStr += MapContent.typicalC
						} else if (contentMap['C'] >= 7 && contentMap['C'] <= 13) {
							resultStr += MapContent.tendencyC
						} else if (contentMap['C'] >= 0 && contentMap['C'] <= 6) {
							resultStr += MapContent.notC
						}

						// D
						if (contentMap['D'] >= 0 && contentMap['D'] <= 9) {
							resultStr += MapContent.D1
						} else if (contentMap['D'] >= 10 && contentMap['D'] <= 15) {
							resultStr += MapContent.D2
						} else if (contentMap['D'] >= 16 && contentMap['D'] <= 18) {
							resultStr += MapContent.D3
						} else if (contentMap['D'] >= 19 && contentMap['D'] <= 27) {
							resultStr += MapContent.D4
						} else if (contentMap['D'] >= 28 && contentMap['D'] <= 36) {
							resultStr += MapContent.D5
						} else if (contentMap['D'] >= 37 && contentMap['D'] <= 100) {
							resultStr += MapContent.D6
						}
					}

					data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
					]
				}
			}

		}
		def selfResultCount = 0
		singleMaps.each { // 单个题目出答案
			def chart = [], resultMaps = [], strResult = ''
			if (it?.self?.id && (10l == it?.self?.id || 13l == it?.self?.id || 14l == it?.self?.id)) {
				println it?.self?.id
				resultMaps = selfUserQuestionnaireRepository.findBySelfAndUserOrderByTimeAsc(it?.self, user)
				if (resultMaps.size() > 1) {
					resultMaps.each {
						chart << [name : it.time.getTime(), value : it.score]
					}
				}
			} else if (it?.self?.id && (8l == it?.self?.id || 16l == it?.self?.id)) {
				def resultMapRecords = mapRecordRepository.findBySelfAndUser(it?.self, user),
					moodMaps = [:]
				resultMapRecords.each {
					moodMaps.put(it.name, it.value)
				}
				if ('ADULT' == it.self.abbreviation) {
					if (moodMaps['Tenacity']) {
						chart << [name : '坚韧性', value : moodMaps['Tenacity']]
					} else {
						chart << [name : '坚韧性', value : 0]
					}
					if (moodMaps['Optimism']) {
						chart << [name : '乐观性', value : moodMaps['Optimism']]
					} else {
						chart << [name : '乐观性', value : 0]
					}
					if (moodMaps['Poisedness']) {
						chart << [name : '自若性', value : moodMaps['Poisedness']]
					} else {
						chart << [name : '自若性', value : 0]
					}
					if (moodMaps['Persistence']) {
						chart << [name : '执着性', value : moodMaps['Persistence']]
					} else {
						chart << [name : '执着性', value : 0]
					}

				}
				if ('MBTI' == it.self.abbreviation) {
					if (moodMaps['J']) {
						chart << [name : '判断（J）', value : moodMaps['J']]
					} else {
						chart << [name : '判断（J）', value : 0]
					}
					if (moodMaps['T']) {
						chart << [name : '思考（T）', value : moodMaps['T']]
					} else {
						chart << [name : '思考（T）', value : 0]
					}
					if (moodMaps['S']) {
						chart << [name : '实感（S）', value : moodMaps['S']]
					} else {
						chart << [name : '实感（S）', value : 0]
					}
					if (moodMaps['E']) {
						chart << [name : '外向（E）', value : moodMaps['E']]
					} else {
						chart << [name : '外向（E）', value : 0]
					}
					if (moodMaps['P']) {
						chart << [name : '（P）感觉', value : moodMaps['P']]
					} else {
						chart << [name : '（P）感觉', value : 0]
					}
					if (moodMaps['F']) {
						chart << [name : '（F）情感', value : moodMaps['F']]
					} else {
						chart << [name : '（F）情感', value : 0]
					}
					if (moodMaps['N']) {
						chart << [name : '（N）直觉', value : moodMaps['N']]
					} else {
						chart << [name : '（N）直觉', value : 0]
					}
					if (moodMaps['I']) {
						chart << [name : '（I）内向', value : moodMaps['I']]
					} else {
						chart << [name : '（I）内向', value : 0]
					}
				}

			} else if (it?.self?.selfGroup?.graphicsType){
				it.mapRecords.each {
					chart << [name : it.name, value : it.value]
				}
			}

			if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
				selfResultCount ++
				def selfResult = selfResultOptionRepository.findOne(145l)
				data << [
						'groupId' : '',
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
				]
			}
			strResult = it?.selfResultOption?.content
			if ('SDS' == it?.self?.selfGroup?.name) {
				strResult += ('\n' + MapContent.sds)
			}
			data << [
					'groupId' : it?.self?.selfGroup?.id,
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.self?.selfGroup?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : strResult,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.selfGroup?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
			]
		}

		[
				'success' : '1',
				'message' : '成功',
				'userId' : user.id,
				'data' : data
		]


	}
}
