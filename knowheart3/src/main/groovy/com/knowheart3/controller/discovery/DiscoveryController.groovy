package com.knowheart3.controller.discovery
import com.knowheart3.repository.discovery.DailyDiscoveryRepository
import com.knowheart3.repository.exercise.ExerciseInfoRepository
import com.knowheart3.repository.exercise.ExerciseRepository
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
		
		def exercise, number, exerciseCount, userExercise, mession = false, isLogin = false
        def now = new Date()
		if (null == user.id) {
			exercise = exerciseRepository.findRandomExercise()
            // 查找练习第一题
			number = 1
		} else {
            isLogin = true
            // 查找未完成的用户练习纪录
			userExercise = userExerciseRepository.findOneByFilters([
				user_equal : user,
				complete_isFalse : null
			])

            if (userExercise == null) {
                // 查找刚完成的用户练习纪录
                def page = userExerciseRepository.findCompleteExerciseByUser(user, pageable)
                def existExercise
                if (page.getContent() && page.getContent().size() > 0) {
                    existExercise = page.getContent()?.get(0)
                }

                if (existExercise) {
                    mession = true
                    exerciseCount = exerciseInfoRepository.countByExercise(existExercise)
                    number = exerciseCount
                } else {
                    exercise = exerciseRepository.findRandomExercise()
                    // 查找练习第一题
                    number = 1
                }
            } else {
                // 存在用户练习，得到练习大题
                exercise = userExercise.exercise

                if (DateUtils.isSameDay(userExercise.time, now)) {
                    mession = true
                    number = userExercise.number as int
                } else {
                    number = (userExercise.number as int) + 1
                }

//                breakTime = ((new Date()).getTime() - userExercise.time.getTime()) / 1000 / 60 / 60 / 24 as int
//                if (breakTime == 1) {
//                    userExercise.time = new Date()
//                    userExercise.completeCount += 1
//                    mession = true
//                } else if (breakTime > 1) {
//                    userExercise.completeCount = 0
//                }
//                userExerciseRepository.save(userExercise)
            }
		}
        if (exercise) {
		    exerciseCount = exerciseInfoRepository.countByExercise(exercise)
        } else {
            exerciseCount = 0
        }
        def c = Calendar.getInstance()
        c.setTime new Date()
        def diff = -pageable.pageNumber
        c.add(Calendar.DATE, diff)
        def today = c.getTime()
//        def pable = new PageRequest(0, 1)
//		def page = dailyDiscoveryRepository.findAll([
//		        'time_equal' : DateFormatUtils.format(today, 'yyyy-MM-dd')
//		], pable)
//
////        def page = dailyDiscoveryRepository.findByTimeAndPageable(c.getTime(), pageable)
//        def dailyDiscovery = page.getContent().get(0)
        def dailyDiscovery = dailyDiscoveryRepository.findByTime(c.getTime()),
            size = dailyDiscoveryRepository.countByTime(c.getTime())


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
