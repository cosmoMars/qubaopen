package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.SelfGroup;
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
	retrieveMapStatistics(long userId, String type) {

		def user = new User(id : userId),
			data = [], existMaps = []
			
		def om = new ObjectMapper()
		if (type == 'ALL' || type == null) {
			
			def specialMaps = mapStatisticsRepository.findByMaxRecommendedValue() // 4小时题目
			existMaps += specialMaps
			
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs()
			
			existMaps += existGroupMaps
			
			def singleMaps
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps)
			} else {
				singleMaps = mapStatisticsRepository.findAll()
			}
			if (specialMaps && specialMaps.size() >= 7) {
				def results = []
				specialMaps.each {
					results << it.result
				}
				data << [
			        'mapTitle' : specialMaps[0].self.title,
					'chart' : "[{${results.join(",")}}]",
					'mapMax' : specialMaps[0].mapMax,
					'resultName' : specialMaps[0].selfResultOption.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0].managementType,
					'recommendedValue' : specialMaps[0].recommendedValue,
					'graphicsType' : specialMaps[0].self.graphicsType.name,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMaps.size() < 7) {
				data << [
					'mapTitle' : specialMaps[0].self.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps[0].managementType,
					'recommendedValue' : specialMaps[0].recommendedValue,
					'graphicsType' : specialMaps[0].self.graphicsType.id,
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
						'managementType' : k.managementType,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
						'special' : false,
						'lock' : true
					]
				} else if (v.size() == k.selfs.size()) {
					def charts = []
					v.each { s ->
						charts << [name : s.self.abbreviation, value : s.score]
					}
				
					data << [
						'mapTitle' : k.name,
						'chart' : charts,
						'mapMax' : k.mapMax,
						'resultName' : k.name,
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k.managementType,
						'recommendedValue' : k.recommendedValue,
						'graphicsType' : k.graphicsType.id,
						'special' : false,
						'lock' : false
					]
				}
			}
			
			
			singleMaps.each { // 单个题目出答案
				data << [
					'mapTitle' : it?.self?.title,
					'chart' : it?.result,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : it?.score,
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.managementType,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false
				]
			}
			
		} else {
//			def mapType = mapStatisticsTypeRepository.findByName(type),
//				maps = mapStatisticsRepository.findByUserAndMapStatisticsType(user, mapType)
//				if (maps.size() > 1) {  // abcd 问卷
//					def temp = [],
//						typeName = ''
//					maps.each {
//						typeName = it.self.abbreviation
//						temp << [ name : typeName, value : it.score]
//					}
//					data << [
//						'chart' : om.writeValueAsString(temp),
//						'name' : 'ABCD测试',
//						'content' : 'ABCD测试',
//						'title' : mapType.name,
//						'score' : '',
//						'mapMax' : maps[0].mapMax,
//						'managementType' : maps[0].managementType,
//						'recommendedValue' : maps[0].recommendedValue
//					]
//				}
//				if (!maps.empty && maps.size() == 1) {
//					data << [
//						'chart' : maps[0].result,
//						'name' : maps[0].selfResultOption?.name,
//						'content' : maps[0].selfResultOption?.content,
//						'title' : mapType.name,
//						'score' : maps[0].score,
//						'mapMax' : maps[0].mapMax,
//						'managementType' : maps[0].managementType,
//						'recommendedValue' : maps[0].recommendedValue
//					]
//				}
		}

		[
			'success' : '1',
			'message' : '成功',
			'userId' : userId,
			'data' : data
		]
	}
}
