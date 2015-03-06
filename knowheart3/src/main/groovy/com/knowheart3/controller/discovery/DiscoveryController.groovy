package com.knowheart3.controller.discovery;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.knowheart3.repository.exercise.ExerciseInfoRepository;
import com.knowheart3.repository.exercise.ExerciseRepository;
import com.knowheart3.repository.exercise.UserExerciseRepository;
import com.knowheart3.repository.favorite.UserFavoriteRepository;
import com.knowheart3.repository.topic.DailyDiscoveryRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.DailyDiscovery
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('discovery')
@SessionAttributes('currentUser')
public class DiscoveryController extends AbstractBaseController<DailyDiscovery, Long> {

	@Autowired
	DailyDiscoveryRepository dailyDiscoveryRepository
	
	@Autowired
	UserFavoriteRepository userFavoriteRepository
	
	@Autowired
	ExerciseRepository exerciseRepository
	
	@Autowired
	ExerciseInfoRepository exerciseInfoRepository
	
	@Autowired
	UserExerciseRepository userExerciseRepository
	
	@Override
	MyRepository<DailyDiscovery, Long> getRepository() {
		dailyDiscoveryRepository
	}
	
	/**
	 * @param id
	 * @param time
	 * @param user
	 * @return
	 * 获取发现内容
	 */
	@RequestMapping(value = 'retrieveDiscoveryContent', method = RequestMethod.GET)
	retrieveDiscoveryContent(@RequestParam(required = false) String time,
		@ModelAttribute('currentUser') User user) {
		
		def exercise, number, exerciseCount, userExercise, breakTime, mession = false
		if (null == user.id) {
			exercise = exerciseRepository.findRandomExercise()
			number = 1
			exerciseCount = exerciseInfoRepository.countByExercise(exercise)
		} else {
			userExercise = userExerciseRepository.findOneByFilters([
				user_equal : user,
				complete_isFalse : null
			])
            if (userExercise == null) {
                exercise = exerciseRepository.findRandomExercise()
                number = 1
            } else {
                exercise = userExercise.exercise
                number = userExercise.number as int
                breakTime = ((new Date()).getTime() - userExercise.time.getTime()) / 1000 / 60 / 60 / 24 as int
                if (breakTime <= 1) {
                    userExercise.time = new Date()
                    userExercise.completeCount += 1
                    mession = true
                } else if (breakTime > 1) {
                    userExercise.completeCount = 0
                }
                userExerciseRepository.save(userExercise)
            }
			exerciseCount = exerciseInfoRepository.countByExercise(exercise)
		}
		def dailyDiscovery
		if (time != null) {
			dailyDiscovery = dailyDiscoveryRepository.findByTime(DateUtils.parseDate(time, 'yyyy-MM-dd'))
		} else {
			dailyDiscovery = dailyDiscoveryRepository.findByTime(new Date())
		}
		
		[
			'success' : '1',
			'exerciseId' : exercise?.id,
			'exerciseName' : exercise?.name,
			'exerciseContent' : exercise?.remark,
			'exerciseNumber' : number,
			'exerciseCount' : exerciseCount,
			'exerciseComplete' : userExercise?.completeCount,
            'messionComplete' : mession,
            'exercisePic' : exercise.url,
			'selfId' : dailyDiscovery?.self?.id,
			'selfName' : dailyDiscovery?.self?.title,
			'selfContent' : dailyDiscovery?.self?.remark,
            'selfPic' : dailyDiscovery.self.picPath,
			'topicId' : dailyDiscovery?.topic?.id,
			'topicName' : dailyDiscovery?.topic?.name,
			'topicContent' : dailyDiscovery?.topic?.content,
            'topicPic' : dailyDiscovery.topic.picUrl
		]
		
	}
		
}
