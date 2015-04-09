package com.knowheart3.controller.discovery
import com.knowheart3.repository.discovery.DailyDiscoveryRepository
import com.knowheart3.repository.exercise.ExerciseInfoRepository
import com.knowheart3.repository.exercise.ExerciseRepository
import com.knowheart3.repository.exercise.UserExerciseInfoLogRepository
import com.knowheart3.repository.exercise.UserExerciseRepository
import com.knowheart3.repository.favorite.UserFavoriteRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.DailyDiscovery
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

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
    UserExerciseInfoLogRepository userExerciseInfoLogRepository
	
	@Autowired
	UserExerciseRepository userExerciseRepository

    @Autowired
    SelfUserQuestionnaireRepository selfUserQuestionnaireRepository
	
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
    @Transactional(readOnly = true)
	@RequestMapping(value = 'retrieveDiscoveryContent', method = RequestMethod.GET)
	retrieveDiscoveryContent(@PageableDefault(page = 0, size = 1, sort = 'time', direction = Sort.Direction.DESC) Pageable pageable,
		@ModelAttribute('currentUser') User user) {
		
		def exercise, number, userExercise, mession = false, isLogin = false
        def now = new Date(),
            c = Calendar.getInstance()
        c.setTime new Date()
        def diff = -pageable.pageNumber
        c.add(Calendar.DATE, diff)
        def today = c.getTime()

		if (null == user.id) {
			exercise = exerciseRepository.findRandomExercise()
            // 查找练习第一题
			number = 1
		} else {
            isLogin = true
            userExercise = userExerciseRepository.findOneByFilters([
                    user_equal : user,
                    complete_isFalse : null
            ])

            // 完成练习中最后一个纪录
            if (!DateUtils.isSameDay(now, today) && today.compareTo(now) == -1) {
                def eLog = userExerciseInfoLogRepository.findLastLogByUserAndTime(user, today)

                def exerciseInfo = eLog.exerciseInfo
                exercise = exerciseInfo.exercise
                number = exerciseInfo.number as int
                mession = true
            } else {
                // 查找未完成的用户练习纪录
                if (userExercise == null) {
                    // 查找刚完成的用户练习纪录
                    userExercise = userExerciseRepository.findCompleteExerciseByUser(user)
                    mession = true
                } else {
                    // 存在用户练习，得到练习大题
                    if (DateUtils.isSameDay(userExercise.time, now)) {
                        mession = true
                        number = userExercise.number as int
                    } else {
                        number = (userExercise.number as int) + 1
                    }
                }
                exercise = userExercise.exercise
            }
		}
        def exerciseCount = exerciseInfoRepository.countByExercise(exercise)
        if (userExercise.complete == true) {
            number = exerciseCount
        }
        def dailyDiscovery = dailyDiscoveryRepository.findByTime(today),
            size = dailyDiscoveryRepository.countByTime(today)


        def more = false
        if (size > 0) {
            more = true
        }


        def haveDone = false
        if (dailyDiscovery && dailyDiscovery?.self != null && null != user.id) {
            def count = selfUserQuestionnaireRepository.countBySelfAndUser(dailyDiscovery.self, user)
            if (count > 0) {
                haveDone = true
            }
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
            'exercisePic' : exercise?.url,
			'selfId' : dailyDiscovery?.self?.id,
			'selfName' : dailyDiscovery?.self?.title,
			'selfContent' : dailyDiscovery?.self?.remark,
            'selfPic' : dailyDiscovery?.self?.picPath,
            'selfDone' : haveDone,
			'topicId' : dailyDiscovery?.topic?.id,
			'topicName' : dailyDiscovery?.topic?.name,
			'topicContent' : dailyDiscovery?.topic?.content,
            'topicPic' : dailyDiscovery?.topic?.picUrl,
            'isLogin' : isLogin,
            'time' : dailyDiscovery?.time,
            'more' : more
		]
		
	}

}
