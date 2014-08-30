package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository
import com.qubaopen.survey.repository.self.SelfGroupRepository;

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
				return '暂没有心理地图，请做题'
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
				if (v.size() < k.selfs.size()) {
					data << [
						'mapTitle' : k.name,
						'chart' : '',
						'mapMax' : '',
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
						'special' : false,
						'lock' : true
					]
				} else if (v.size() == k.selfs.size()) {
					def chart = []
					
					v.each { s ->
						s.mapRecords.each {  
							chart << [name : it.name, value : it.value]
						}
					}
				
					data << [
						'mapTitle' : k.name,
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
				return '该类型暂没有心理题图，请做题'
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
				if (v.size() < k.selfs.size()) {
					data << [
						'mapTitle' : k.name,
						'chart' : '',
						'mapMax' : '',
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
						'special' : false,
						'lock' : true
					]
				} else if (v.size() == k.selfs.size()) {
					def chart = []
					
					v.each { s ->
						s.mapRecords.each {
							chart << [name : it.name, value : it.value]
						}
					}
				
					data << [
						'mapTitle' : k.name,
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
		data << [
			
			'mapTitle' : '测试坐标轴',
			'chart' : [name : 5, value : 2],
			'mapMax' : 10,
			'resultName' : '测试结果',
			'resultScore' : 5,
			'resultContent' : '坐标轴测试用结果',
			'managementType' : 1,
			'recommendedValue' : 10,
			'graphicsType' : 5,
			'special' : false,
			'lock' : false
		]
		[
			'success' : '1',
			'message' : '成功',
			'userId' : userId,
			'data' : data
		]
	}
}
