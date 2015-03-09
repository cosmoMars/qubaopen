package com.knowheart3.controller.exercise;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.exercise.ExerciseInfoRepository
import com.knowheart3.repository.exercise.ExerciseRepository
import com.knowheart3.repository.exercise.UserExerciseInfoLogRepository;
import com.knowheart3.repository.exercise.UserExerciseRepository
import com.knowheart3.utils.DateCommons;
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.Exercise
import com.qubaopen.survey.entity.topic.UserExercise
import com.qubaopen.survey.entity.topic.UserExerciseInfoLog
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('exercise')
@SessionAttributes('currentUser')
public class ExerciseController extends AbstractBaseController<Exercise, Long> {

	@Autowired
	ExerciseRepository exerciseRepository
	
	@Autowired
	ExerciseInfoRepository exerciseInfoRepository
	
	@Autowired
	UserExerciseRepository userExerciseRepository
	
	@Autowired
	UserExerciseInfoLogRepository userExerciseInfoLogRepository
	
	@Override
	MyRepository<Exercise, Long> getRepository() {
		exerciseRepository
	}
	
	/**
	 * @param pageable
	 * @return
	 * 获取列表
	 */
	@RequestMapping(value = 'retrieveExerciseList', method = RequestMethod.GET)
	def retrieveExerciseList(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		
		def list = exerciseRepository.findAll(pageable)
		
		def data = []
		list.each {
			def	infoSize = exerciseInfoRepository.countByExercise(it)
			data << [
				'id' : it?.id,
				'name' : it?.name,
				'pic' : it?.url,
				'size' : infoSize
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
	
	
	/**
	 * @param id
	 * @param user
	 * @return
	 * 确认练习
	 */
	@RequestMapping(value = 'confirmExerciseInfo', method = RequestMethod.POST)
	confirmExerciseInfo(@RequestParam(required = false) Long id,
		@ModelAttribute('currentUser') User user) {
		
		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}
		def eInfo = exerciseInfoRepository.findOne(id),
			infoSize = exerciseInfoRepository.countByExercise(eInfo.exercise)
		
		def uExercise = userExerciseRepository.findOneByFilters([
			user_equal : user,
			complete_isFalse : null
		])
		if (uExercise && uExercise?.exercise != eInfo?.exercise) {
			userExerciseRepository.delete(uExercise)
			uExercise = null
		}
		if (uExercise) {
			uExercise.number = eInfo.number
			uExercise.time = new Date()
			if (eInfo.number as int == infoSize) {
				uExercise.complete = true
			}
		} else {
			uExercise = new UserExercise(
				user : user,
				exercise : eInfo.exercise,
				number : eInfo.number,
				time : new Date()
			)
			if (eInfo.number as int == infoSize) {
				uExercise.complete = true
			}
		}
		def exerciseInfoLog = new UserExerciseInfoLog(
			user : user,
			exerciseInfo : eInfo
		)
		userExerciseRepository.save(uExercise)
		userExerciseInfoLogRepository.save(exerciseInfoLog)
		[
			'success' : '1',
			'uExerciseId' : uExercise.id	
		]
	}
}
