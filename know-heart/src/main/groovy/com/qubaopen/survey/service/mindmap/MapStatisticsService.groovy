package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsTypeRepository mapStatisticsTypeRepository

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
			data = []
		if (type == 'ALL' || type == null) {
			def mapTypes = mapStatisticsTypeRepository.findAll()

			mapTypes.each {
				def maps =  mapStatisticsRepository.findByUserAndMapStatisticsType(user, it)
				if (maps.size() > 1) {  // abcd 问卷
					def temp = [:],
						typeName = ''
					maps.each {
						typeName = it.self.abbreviation
						temp << ["$typeName" : it.score]
					}
					data << [
						'chart' : objectMapper.writeValueAsString(temp),
						'name' : 'ABCD测试',
						'content' : 'ABCD测试',
						'title' : it.name,
						'score' : '',
						'mapMax' : maps[0].mapMax,
						'managementType' : maps[0].managementType,
						'recommendedValue' : maps[0].recommendedValue
					]
				}
				if (!maps.empty && maps.size() == 1) {
					data << [
						'chart' : maps[0].result,
						'name' : maps[0].selfResultOption?.name,
						'content' : maps[0].selfResultOption?.content,
						'title' : it.name,
						'score' : maps[0].score,
						'mapMax' : maps[0].mapMax,
						'managementType' : maps[0].managementType,
						'recommendedValue' : maps[0].recommendedValue
					]
				}
			}
		} else {
			def mapType = mapStatisticsTypeRepository.findByName(type),
				maps = mapStatisticsRepository.findByUserAndMapStatisticsType(user, mapType)
				if (maps.size() > 1) {  // abcd 问卷
					def temp = [:],
						typeName = ''
					maps.each {
						typeName = it.self.abbreviation
						temp << ["$typeName" : it.score]
					}
					data << [
						'chart' : objectMapper.writeValueAsString(temp),
						'name' : 'ABCD测试',
						'content' : 'ABCD测试',
						'title' : mapType.name,
						'score' : '',
						'mapMax' : maps[0].mapMax,
						'managementType' : maps[0].managementType,
						'recommendedValue' : maps[0].recommendedValue
					]
				}
				if (!maps.empty && maps.size() == 1) {
					data << [
						'chart' : maps[0].result,
						'name' : maps[0].selfResultOption?.name,
						'content' : maps[0].selfResultOption?.content,
						'title' : mapType.name,
						'score' : maps[0].score,
						'mapMax' : maps[0].mapMax,
						'managementType' : maps[0].managementType,
						'recommendedValue' : maps[0].recommendedValue
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
