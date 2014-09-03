package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.EPQBasicRepository;
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository
import com.qubaopen.survey.repository.self.SelfGroupRepository;
import com.qubaopen.survey.repository.user.UserIDCardBindRepository;
import com.qubaopen.survey.service.user.UserIDCardBindService;

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

	/**
	 * 获取心理地图
	 * @param userId
	 * @param type
	 * @return
	 */
	@Transactional
	retrieveMapStatistics(long userId, long typeId) {

		def user = new User(id : userId),
			data = [], existMaps = []
			
		def om = new ObjectMapper()
		if (typeId == 4l) {
			
			def specialMaps = mapStatisticsRepository.findByMaxRecommendedValue() // 4小时题目
			if (!specialMaps) {
				return '{"success" : "0", "message" : "暂没有心理地图，请做题"}'
			}
			
			if (specialMaps && specialMaps.size() == 1) {
				existMaps += specialMaps
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs()
			
			existMaps += existGroupMaps
			
			def singleMaps
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps)
			} else {
				singleMaps = mapStatisticsRepository.findAll()
			}
			if (specialMaps && specialMaps.size() == 1 && specialMaps[0].mapRecords.size() >= 7) { // 特殊题
				def chart = []
				
				specialMaps[0].mapRecords.each {
					chart << [name : it.name, value : it.value]
				}
				data << [
			        'mapTitle' : specialMaps[0].self.title,
					'chart' : chart,
					'mapMax' : specialMaps[0].mapMax,
					'resultName' : specialMaps[0].selfResultOption.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0]?.selfManagementType?.id,
					'recommendedValue' : specialMaps[0]?.recommendedValue,
					'graphicsType' : specialMaps[0]?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMaps.size() == 1 && specialMaps[0].mapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps[0].self.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0]?.selfManagementType?.id,
					'recommendedValue' : specialMaps[0]?.recommendedValue,
					'graphicsType' : specialMaps[0]?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true
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
					def completeName = []
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each { 
							completeName << it.name
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'mapTitle' : k.title,
						'chart' : '',
						'mapMax' : k.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
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
							def idMap = userIDCardBindService.calculateAgeByIdCard(user),
								age = idMap.get('age'), sex = idMap.get('sex')
							
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
							chart << [name : rk, value : tScore]
						}
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						
						data << [
							'mapTitle' : k.title,
							'chart' : chart,
							'mapMax' : k.mapMax,
							'resultName' : k.name,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k.recommendedValue,
							'graphicsType' : k.graphicsType.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						v.each { s ->
							s.mapRecords.each {
								chart << [name : it.name, value : it.value]
							}
						}
						data << [
							'mapTitle' : k.title,
							'chart' : chart,
							'mapMax' : k.mapMax,
							'resultName' : k.name,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k.recommendedValue,
							'graphicsType' : k.graphicsType.id,
							'special' : false,
							'lock' : false
						]
					}
				}
			}
			
			
			singleMaps.each { // 单个题目出答案
				def chart = []
				it.mapRecords.each {
					chart << [name : it.name, value : it.value]
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
					'lock' : false
				]
			}
		} else {
			def selfManagementType = new SelfManagementType(id : typeId),
				typeMaps = mapStatisticsRepository.findBySelfManagementType(selfManagementType)
				
			if (!typeMaps) {
				return '{"success" : "0", "message" : "该类型暂没有心理题图，请做题"}'
			}
		
			def specialMaps = mapStatisticsRepository.findByMaxRecommendedValue(typeMaps) // 4小时题目
			
			if (specialMaps && specialMaps.size() == 1) {
				existMaps += specialMaps
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs(selfManagementType)
			
			existMaps += existGroupMaps
			
			def singleMaps
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, selfManagementType)
			} else {
				singleMaps = mapStatisticsRepository.findBySelfManagementType(selfManagementType)
			}
			if (specialMaps && specialMaps.size() == 1 && specialMaps[0].mapRecords.size() >= 7) { // 特殊题
				def chart = []
				
				specialMaps[0].mapRecords.each {
					chart << [name : it.name, value : it.value]
				}
				data << [
					'mapTitle' : specialMaps[0].self.title,
					'chart' : chart,
					'mapMax' : specialMaps[0].mapMax,
					'resultName' : specialMaps[0].selfResultOption.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0]?.selfManagementType?.id,
					'recommendedValue' : specialMaps[0]?.recommendedValue,
					'graphicsType' : specialMaps[0]?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMaps.size() == 1 && specialMaps[0].mapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps[0].self.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0]?.selfManagementType?.id,
					'recommendedValue' : specialMaps[0]?.recommendedValue,
					'graphicsType' : specialMaps[0]?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true
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
					def completeName = []
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each { 
							completeName << it.name
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'mapTitle' : k.title,
						'chart' : '',
						'mapMax' : k.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
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
							def idMap = userIDCardBindService.calculateAgeByIdCard(user),
								age = idMap.get('age'), sex = idMap.get('sex')
							
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
							chart << [name : rk, value : tScore]
						}
						
						recordMaps
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						
						data << [
							'mapTitle' : k.title,
							'chart' : chart,
							'mapMax' : k.mapMax,
							'resultName' : k.name,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k.recommendedValue,
							'graphicsType' : k.graphicsType.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						
						v.each { s ->
							s.mapRecords.each {
								chart << [name : it.name, value : it.value]
							}
						}
					
						data << [
							'mapTitle' : k.title,
							'chart' : chart,
							'mapMax' : k.mapMax,
							'resultName' : k.name,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k.recommendedValue,
							'graphicsType' : k.graphicsType.id,
							'special' : false,
							'lock' : false
						]
					}
				}
					
			}
			
			singleMaps.each { // 单个题目出答案
				def chart = []
				it.mapRecords.each {
					chart << [name : it.name, value : it.value]
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
					'lock' : false
				]
			}
		}
		[
			'success' : '1',
			'message' : '成功',
			'userId' : userId,
			'data' : data
		]
	}
}
