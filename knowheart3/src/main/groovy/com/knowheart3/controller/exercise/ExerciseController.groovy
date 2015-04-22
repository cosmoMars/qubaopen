package com.knowheart3.controller.exercise

import com.knowheart3.repository.exercise.ExerciseInfoRepository
import com.knowheart3.repository.exercise.ExerciseRepository
import com.knowheart3.repository.exercise.UserExerciseInfoLogRepository
import com.knowheart3.repository.exercise.UserExerciseRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.Exercise
import com.qubaopen.survey.entity.topic.UserExercise
import com.qubaopen.survey.entity.topic.UserExerciseInfoLog
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

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
	def retrieveExerciseList(@PageableDefault(page = 0, size = 20) Pageable pageable,
							 @ModelAttribute('currentUser') User user) {

		def list = exerciseRepository.findAll(pageable)

		def uExerciseCount = 0
		def data = []
		if (user.id != null) {

			list.each {
				def infoSize = exerciseInfoRepository.countByExercise(it)
				uExerciseCount = userExerciseRepository.countByUserAndExerciseAndComplete(user, it, true)
				data << [
						'id'          : it?.id,
						'name'        : it?.name,
						'pic'         : it?.url,
						'size'        : infoSize,
						uExerciseCount: uExerciseCount,
				]
			}
		} else {
			list.each {
				def infoSize = exerciseInfoRepository.countByExercise(it)
				data << [
						'id'          : it?.id,
						'name'        : it?.name,
						'pic'         : it?.url,
						'size'        : infoSize,
						uExerciseCount: 0,
				]
			}
		}
		[
				success: '1',
				data   : data
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
        // 找练习 总数
		def eInfo = exerciseInfoRepository.findOne(id),
			infoSize = exerciseInfoRepository.countByExercise(eInfo.exercise)

        // 用户现在练习纪录
		def uExercise = userExerciseRepository.findOneByFilters([
			user_equal : user,
			complete_isFalse : null
		])
        // 存在练习，发现练习专栏不同删除原先练习
		if (uExercise && uExercise?.exercise != eInfo?.exercise) {
			userExerciseRepository.delete(uExercise)
			uExercise = null
		}
		if (uExercise) {
            def now = new Date()
            // 今天已经完成练习
            if (DateUtils.isSameDay(now, uExercise.time) && uExercise.exerciseInfo.id == id) {
                return '{"success" : "0", "message" : "今天练习已经完成"}'
            }
            // 判断连续次数
            Calendar c = Calendar.getInstance()
            c.setTime(uExercise.time)
            c.add(Calendar.DATE, 1)
            if (DateUtils.isSameDay(c.getTime(), now)) {
                uExercise.completeCount++
            } else {
                uExercise.completeCount = 1
            }
            uExercise.number = eInfo.number
			uExercise.time = now
            uExercise.exerciseInfo = eInfo

			if (eInfo.number as int == infoSize) {
				uExercise.complete = true
			}
		} else {
			uExercise = new UserExercise(
				user : user,
				exercise : eInfo.exercise,
				number : eInfo.number,
                time: new Date(),
                completeCount: 1,
                exerciseInfo: eInfo
			)
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
