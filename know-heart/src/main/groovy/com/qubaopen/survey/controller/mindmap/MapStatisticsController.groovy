package com.qubaopen.survey.controller.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.service.mindmap.MapStatisticsService

@RestController
@RequestMapping("mapStatistics")
public class MapStatisticsController extends AbstractBaseController<MapStatistics, Long> {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsService mapStatisticsService

	@Override
	protected MyRepository<MapStatistics, Long> getRepository() {
		mapStatisticsRepository
	}

	@RequestMapping(value = 'calculateMapStatistcs', method = RequestMethod.GET)
	calculateMapStatistics(@RequestParam long userId, @RequestParam long questionnaireId, @RequestParam String type) {

		mapStatisticsService.calculateMapStatistics(userId, questionnaireId, type)
	}

}
