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

	@Transactional
	retrieveMapStatistics(long userId, String type) {

		def user = new User(id : userId)

		def mapType = null, resultMap = []

		switch (type) {
			case 'SDS' :
				mapType = MapStatistics.Type.SDS
			case 'ABCD' :
				mapType = MapStatistics.Type.ABCD
			case 'PDP' :
				mapType = MapStatistics.Type.PDP
			case 'MBTI' :
				mapType = MapStatistics.Type.MBTI

		}

		def map =  mapStatisticsRepository.findByUserAndType(user, mapType)

		if (map.size() > 1) {
			def temp = [:]
			map.each {
				if (temp.get(it.score)) {
					temp.get(it.score) << it.self.selfType.name
				} else {
					def list = []
					list << it.self.selfType.name
					temp.put(it.score, list)
				}
			}
			resultMap = [
				'userId' : userId,
				'chart' : temp,
				'resultOption' : '',
				'score' : ''
			]
		}
		if (!map.empty) {
			resultMap = [
				'userId' : userId,
				'chart' : map[0].result,
				'resultOption' : map[0].selfResultOption ?: '',
				'score' : map[0].score ?: ''
			]
		}

		resultMap
	}
}
