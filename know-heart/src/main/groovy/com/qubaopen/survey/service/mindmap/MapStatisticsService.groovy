package com.qubaopen.survey.service.mindmap

import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.SelfManagementType
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.EPQBasicRepository
import com.qubaopen.survey.repository.mindmap.MapRecordRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository
import com.qubaopen.survey.repository.self.SelfGroupRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.service.user.UserIDCardBindService

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
	/**
	 * 获取心理地图
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional
	retrieveMapStatistics(User user, long typeId) {

		def data = [], existMaps = []
			
		def om = new ObjectMapper()
		def specialSelf = selfRepository.findSpecialSelf()
		if (typeId == 4l) {
			
			def specialMaps = mapStatisticsRepository.findOneByFilters(
				[
					self_equal : specialSelf,
					user_equal : user
				]	
			)// 4小时题目
//			if (!specialMaps) {
//				return '{"success" : "0", "message" : "err700"}' // 暂没有心理地图，请做题
//			}
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
				def chart = []
				
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
					specialMaps.mapRecords.each {
						timeChart << it.name
						paChart << it.value
						naChart << it.naValue
						midChart << (it.value - it.naValue)
						
					}
					chart << [timeChart : timeChart, paChart : paChart, naChart : naChart, midChart : midChart]
				}
				data << [
			        'mapTitle' : specialMaps?.self?.title,
					'chart' : chart,
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
//							if (k?.graphicsType) {
//								chart << [name : rk, value : tScore]
//							}
							
						}
						def resultStr
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.name,
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
							'resultName' : k?.name,
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
			
			
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				data << [
					
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : it?.score,
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false/*,
					'picPath' : it?.selfResultOption?.picPath*/
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
		
//			if (!typeMaps) {
//				return '{"success" : "0", "message" : "err701"}' // 该类型暂没有心理题图，请做题
//			}
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
				def chart = []
//				if (specialMaps?.self?.graphicsType) {
//					specialMaps.mapRecords.each {
//						chart << [name : it.name, value : it.value]
//					}
//				}
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
					specialMaps.mapRecords.each {
						timeChart << it.name
						paChart << it.value
						naChart << it.naValue
						midChart << (it.value - it.naValue)
					}
					chart << [timeChart : timeChart, paChart : paChart, naChart : naChart, midChart : midChart]
				}
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : chart,
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
//							def idMap = userIDCardBindService.calculateAgeByIdCard(user),
//								age = idMap.get('age'), sex = idMap.get('sex')
//							
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
//							if (k.graphicsType) {
//								chart << [name : rk, value : tScore]
//							}
						}
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						def resultStr
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						data << [
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.name,
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
							'resultName' : k?.name,
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
			
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				data << [
					
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : it?.score,
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false/*,
					'picPath' : it?.selfResultOption?.picPath*/
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
}
