package com.qubaopen.doctor.controller.map;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.map.MapStatisticsRepository
import com.qubaopen.doctor.repository.user.UserRepository;
import com.qubaopen.doctor.service.MapStatisticsService
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('map')
@SessionAttributes('currentDoctor')
public class MapController extends AbstractBaseController<MapStatistics, Long> {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository
	
	@Autowired
	MapStatisticsService mapStatisticsService
	
	@Autowired
	UserRepository userRepository

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
	@RequestMapping(value = 'retrieveSelfResult', method = RequestMethod.POST)
	retrieveSelfResult(@RequestParam(required = false) Long userId, @RequestParam(required = false) Long typeId, @ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace(' -- 获取心理地图信息 -- ')
		def user = userRepository.findOne(userId)
		mapStatisticsService.newRetrieveMapStatistics(user, typeId)
	}
}
