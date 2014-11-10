package com.qubaopen.survey.controller.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.service.mindmap.MapStatisticsService

@RestController
@RequestMapping('mapStatistics')
@SessionAttributes('currentUser')
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
	@RequestMapping(value = 'newRetrieveMapStatistics', method = RequestMethod.GET)
	newRetrieveMapStatistics(@RequestParam(required = false) Long typeId, @ModelAttribute('currentUser') User user) {

		logger.trace(' -- 获取心理地图信息 -- ')

		mapStatisticsService.newRetrieveMapStatistics(user, typeId)
	}
	
	@RequestMapping(value = 'retrieveMapStatistics', method = RequestMethod.GET)
	retrieveMapStatistics(@RequestParam(required = false) Long typeId, @ModelAttribute('currentUser') User user) {

		logger.trace(' -- 获取心理地图信息 -- ')

		mapStatisticsService.retrieveMapStatistics(user, typeId)
	}

	/**
	 * 获取心理地图信息
	 * @param userId
	 * @param selfId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSpecialMap', method = RequestMethod.GET)
	retrieveSpecialMap(@ModelAttribute User user) {

		logger.trace(' -- 获取心理地图信息 -- ')
		
		mapStatisticsService.retrieveSpecialMap(user)
	}
	
}
