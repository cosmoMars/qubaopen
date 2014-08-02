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

	/**
	 * 获取心理地图信息
	 * @param userId
	 * @param selfId
	 * @return
	 */
	@RequestMapping(value = 'retrieveMapStatistics', method = RequestMethod.GET)
	retrieveMapStatistics(@RequestParam long userId, @RequestParam String type) {

		logger.trace(' -- 获取心理地图信息 -- ')

		mapStatisticsService.retrieveMapStatistics(userId, type)
	}

}
