package com.qubaopen.survey.controller.mindmap

import javax.xml.ws.RequestWrapper;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.qubaopen.survey.repository.user.UserMoodRecordRepository;
import com.qubaopen.survey.service.mindmap.MapStatisticsService

@RestController
@RequestMapping('mapStatistics')
@SessionAttributes('currentUser')
public class MapStatisticsController extends AbstractBaseController<MapStatistics, Long> {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsService mapStatisticsService
	
	@Autowired
	UserMoodRecordRepository userMoodRecordRepository

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
	retrieveSpecialMap(@ModelAttribute('currentUser') User user) {

		logger.trace(' -- 获取心理地图信息 -- ')
		
		mapStatisticsService.retrieveSpecialMap(user)
	}
	
	/**
	 * @param time
	 * @param user
	 * @return
	 * 获取心情列表
	 */
	@RequestMapping(value = 'retrieveMoodRecord', method = RequestMethod.POST)
	retrieveMoodRecord(@RequestParam(required = false) String time,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 获取心情列表 --'
		
//		if (time != null && time.matches('/^\\d{4}\\-\\d{2}-\\d{2}\\s\\d{2}\\:\\d{2}$')) {
//			
//		}
		def date
		if (time == null) {
			date = new Date()
		} else {
			date = DateUtils.parseDate(time, 'yyyy-MM-dd')
		}
		
		def result = mapStatisticsService.retrieveMoodRecord(user, date)
		
		result
	}
}
