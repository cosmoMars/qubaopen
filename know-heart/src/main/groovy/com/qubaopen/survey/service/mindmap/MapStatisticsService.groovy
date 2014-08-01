package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Transactional
	calculateMapStatistics(long userId, long questionnaireId, String type) {

//		def maps = mapStatisticsRepository

	}
}
