package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

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

		def user = new User(id : userId)

		if (type == 'ALL') {

		}

		def data = []
		def types = type.split(',')
		types.each {
			def mapType = null
			println "$it  ================================"

			switch (it.trim()) {
				case 'SDS' :
					mapType = MapStatistics.Type.SDS
					break
				case 'ABCD' :
					mapType = MapStatistics.Type.ABCD
					break
				case 'PDP' :
					mapType = MapStatistics.Type.PDP
					break
				case 'MBTI' :
					mapType = MapStatistics.Type.MBTI
					break
			}

			if (mapType) {
				def map =  mapStatisticsRepository.findByUserAndType(user, mapType)

				if (map.size() > 1) {  // abcd 问卷
					def temp = [:],
						typeName = ''
					map.each {
						typeName = it.self.abbreviation
						temp << ["$typeName" : it.score]
					}
					data << [
						'chart' : objectMapper.writeValueAsString(temp),
						'name' : 'ABCD测试',
						'content' : 'ABCD测试',
						'title' : 'ABCD测试结果',
						'score' : '',
						'mapMax' : map[0].mapMax
					]
				}
				if (!map.empty && map.size() == 1) {
					data << [
						'chart' : map[0].result,
						'name' : map[0].selfResultOption?.name,
						'content' : map[0].selfResultOption?.content,
						'title' : map[0].selfResultOption?.title,
						'score' : map[0].score,
						'mapMax' : map[0].mapMax
					]
				}
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
