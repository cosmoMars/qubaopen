package com.knowheart3.controller.mindmap

import com.knowheart3.repository.mindmap.MapCoefficientRepository
import com.knowheart3.repository.mindmap.MapRecordRepository
import com.knowheart3.repository.mindmap.MapStatisticsRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.user.UserMoodRecordRepository
import com.knowheart3.service.mindmap.MapStatisticsService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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

	@Autowired
	MapRecordRepository mapRecordRepository

	@Autowired
	MapCoefficientRepository mapCoefficientRepository

	@Autowired
	SelfRepository selfRepository

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
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		mapStatisticsService.newRetrieveMapStatistics(user, typeId)
	}
	
	@RequestMapping(value = 'retrieveMapStatistics', method = RequestMethod.GET)
	retrieveMapStatistics(@RequestParam(required = false) Long typeId, @ModelAttribute('currentUser') User user) {

		logger.trace(' -- 获取心理地图信息 -- ')
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

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
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
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
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
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

	@RequestMapping(value = 'retrieveMapBySelf', method = RequestMethod.POST)
	retrieveMapBySelf(@RequestParam long selfId,
					  @ModelAttribute('currentUser') User user) {

		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}

		mapStatisticsService.retrieveMapByResultAndUser(selfId, user)
	}

	/**
	 * 情绪准确
	 * @param timeStr
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'confirmMapRecord')
	confirmMapRecord(@RequestParam String timeStr,
					 @RequestParam boolean accurary,
					 @ModelAttribute('currentUser') User user) {

		def time = DateUtils.parseDate(timeStr, "yyyyy-MM-dd")

		def specialSelf = selfRepository.findSpecialSelf(),
			mapStatistics = mapStatisticsRepository.findBySelfAndUser(specialSelf, user),
			localRecord = mapRecordRepository.findByMapStatisticsAndCreateDate(mapStatistics, DateFormatUtils.format(time, "yyyy-MM-dd"))
		def mapRecord
		if (localRecord) {
			mapRecord = localRecord.get(0)
			mapRecord.accurary = accurary
			mapRecordRepository.save(mapRecord)
			return '{"success" : "1"}'
		}
		def c = Calendar.getInstance()
		c.time = time
		c.set(Calendar.HOUR, 12)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)

		def longTime = c.getTimeInMillis()

		def coefficient = mapCoefficientRepository.findOne(user.id)

		def pa = coefficient.pa1 + coefficient.pa2 * Math.cos(longTime * coefficient.pa4) + coefficient.pa3 * Math.sin(longTime * coefficient.pa4),
			na = coefficient.na1 + coefficient.na2 * Math.cos(longTime * coefficient.na4) + coefficient.na3 * Math.sin(longTime * coefficient.na4)

		mapRecord = new MapRecord(
				name: c.getTime().getTime(),
				value: Math.abs(pa as int),
				naValue: Math.abs(na as int),
				mapStatistics: mapStatistics,
				accurary: accurary
		)
		mapRecordRepository.save(mapRecord)

		'{"success" : "1"}'
	}
}
