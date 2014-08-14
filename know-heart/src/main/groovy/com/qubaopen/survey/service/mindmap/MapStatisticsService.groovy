package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	/**
	 * 获取心理地图
	 * @param userId
	 * @param type
	 * @return
	 */
	@Transactional
	retrieveMapStatistics(long userId, String type) {

		def user = new User(id : userId)

		def mapType = null, resultMap = []

		switch (type) {
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

		def map =  mapStatisticsRepository.findByUserAndType(user, mapType)

		if (map.size() > 1) {  // abcd 问卷
			def temp = [:],
				typeName = ''
			map.each {
				typeName = it.self.selfType.name
				temp << [typeName : it.score]
			}
			return resultMap = [
				'success' : '1',
				'message' : '成功',
				'userId' : userId,
				'chart' : temp,
				'name' : '',
				'content' : '',
				'title' : '',
				'score' : ''
			]
		}
		if (!map.empty) {
			resultMap = [
				'success' : '1',
				'message' : '成功',
				'userId' : userId,
				'chart' : map[0].result,
				'name' : map[0].selfResultOption?.name ?: '',
				'content' : map[0].selfResultOption?.content ?: '',
				'title' : map[0].selfResultOption?.title ?: '',
				'score' : map[0].score ?: ''
			]
		}

		resultMap
	}
}
