package com.knowheart3.controller.user

import com.knowheart3.repository.mindmap.MapRecordRepository
import com.knowheart3.repository.mindmap.MapStatisticsRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.user.UserMoodPicRepository
import com.qubaopen.survey.entity.user.UserMoodPic
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.mindmap.MapCoefficientRepository
import com.knowheart3.repository.user.UserMoodRepository
import com.knowheart3.service.mindmap.MapContent
import com.knowheart3.service.user.UserMoodService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserMood
import com.qubaopen.survey.entity.user.UserMood.MoodType

@RestController
@RequestMapping('userMood')
@SessionAttributes('currentUser')
public class UserMoodController extends AbstractBaseController<UserMood, Long>{
	
	static Logger logger = LoggerFactory.getLogger(UserMoodController.class)
	
	@Autowired
	UserMoodRepository userMoodRepository
	
	@Autowired
	MapCoefficientRepository mapCoefficientRepository

	@Autowired
	UserMoodService userMoodService
	
	@Autowired
	SelfRepository selfRepository

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapRecordRepository mapRecordRepository

	@Autowired
	UserMoodPicRepository userMoodPicRepository

	@Override
	protected MyRepository<UserMood, Long> getRepository() {
		userMoodRepository
	}

	/**
	 * 用户提交心情
	 * @param type
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'setMood', method = RequestMethod.POST)
	userMood(@RequestParam int type,
			@RequestParam(required = false) String message,
			@ModelAttribute('currentUser') User user) {

		if(type<0 || type>=MoodType.values().length){
			return '{"success": "0", "message": "err800"}' // type参数错误
		}
		if (message && message.length() > 50) {
			return '{"success" : "0", "message" : "心情短语过长"}'
		}

		userMoodService.saveUserMood(user, MoodType.values()[type], message);
	}
			
	/**
	 * 获取用户心情 停用并 已并入登录接口
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'getMood', method = RequestMethod.GET)
	getMood(@ModelAttribute('currentUser') User user) {

		userMoodService.getUserMood(user);
	}
	
	/**
	 * 获取用户指定月份的心情数据 每日的最后一次
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'getMoodList', method = RequestMethod.POST)
	getMoodList(@RequestParam int month,
		@ModelAttribute('currentUser') User user) {
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		userMoodService.getUserMood(user,month);
	}
	
	/**
	 * @param type
	 * @param time
	 * @param message
	 * @param user
	 * @return
	 * 添加心情
	 */
	@RequestMapping(value = 'addMood', method = RequestMethod.POST)
	addMood(@RequestParam int type,
		@RequestParam(required = false) String time,
		@RequestParam(required = false) String message,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 添加心情 --'
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		def date = null
		// 判断时间空否
		if (time != null) {
			date = DateUtils.parseDate(time, 'yyyy-MM-dd HH:mm')
		}
		def userMood = new UserMood(
			user : user,
			moodType : UserMood.MoodType.values()[type],
			message : message
		)
		if (date) {
			userMood.lastTime = date
		} else {
			userMood.lastTime = new Date()
		}
		
		userMoodRepository.save(userMood);
		
		[
			'success' : '1',
			'moodId' : userMood?.id,
			'moodType' : userMood?.moodType?.ordinal(),
			'lastTime' : userMood?.lastTime,
			'message' : message
		]
		
	}
		
	@RequestMapping(value = 'modifyMood', method = RequestMethod.POST)
	modifyMood(@RequestParam long id,
		@RequestParam(required = false) Integer type,
		@RequestParam(required = false) String message,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 修改心情 --'

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		if (message == null) {
			return '{"success" : "0", "message" : "没有填写心情"}'
		}
		
		def mood = userMoodRepository.findOne(id)
		mood.message = message
		if (type != null) 
			mood.moodType = UserMood.MoodType.values()[type]
		userMoodRepository.save(mood)

		'{"success" : "1"}'		
		
	}
	
	/**
	 * @param month
	 * @param user
	 * @return
	 * 获取每月用户心里列表
	 */
	@RequestMapping(value = 'retrieveMoodList', method = RequestMethod.GET)
	retrieveMoodList(@RequestParam String month, @ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 获取每月用户心里列表 --'

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		def coefficient = mapCoefficientRepository.findOne(user.id)
		
		def moodContent = ''
		if (coefficient) {
			//计算 正负情感趋势 上升 下降
			def c = Calendar.getInstance()
			def time = System.currentTimeMillis(),
				timeBefore = time - 60 * 60 * 24 * 1000 * 2,
				timeAfter = time + 60 * 60 * 24 * 1000 * 2
			
			def resultToday = coefficient.mid1 + coefficient.mid2 * Math.cos(time * coefficient.mid4) + coefficient.mid3 * Math.sin(time * coefficient.mid4)
			def resultBefore = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
			def resultAfter = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
			
			if (resultBefore <= resultToday && resultToday < resultAfter){ // 上升
				moodContent = MapContent.lowToHighTitle + MapContent.lowToHighContent + moodContent + MapContent.lowToHighMethod
			} else if (resultBefore > resultToday && resultToday >= resultAfter){ // 下降
				moodContent = MapContent.highToLowTitle + MapContent.highToLowContent + moodContent + MapContent.highToLowMethod
			} else if (resultBefore <= resultToday && resultToday >= resultAfter){ // 最高处
				moodContent = MapContent.highTideTitle + MapContent.highTideContent + moodContent
			} else if (resultBefore > resultToday  && resultToday < resultAfter){ // 最底处
				moodContent = MapContent.lowTideTitle + MapContent.lowTideContent + moodContent + MapContent.lowTideMethod
			}
		}
		
		def result = userMoodService.retrieveMoodList(month, user, coefficient)
		
		[
			'success' : '1',
			'moodContent' : moodContent,
			'monthData' : result
		]
	}

    /**
     *
     * @param month
     * @param user
     * @return
     * 获取每月心情概况
     */
    @RequestMapping(value = 'retrieveMonthMoodList', method = RequestMethod.POST)
    retrieveMonthMood(@RequestParam(required = false) String month, @ModelAttribute('currentUser') User user) {

        logger.trace '-- 获取每月心情概况 --'

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

        def coefficient = mapCoefficientRepository.findOne(user.id)

        def result = userMoodService.retrieveMonthMood(month, user, coefficient)

        [
            'success' : '1',
            'monthData' : result
        ]
    }

    /**
     * @param time
     * @param user
     * @return
     * 获取当日心情详细
     */
    @RequestMapping(value = 'retrieveTimeMood', method = RequestMethod.POST)
    retrieveTimeMood(@RequestParam(required = false) String time, @ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		def specialSelf = selfRepository.findSpecialSelf()
		def specialMap = mapStatisticsRepository.findOneByFilters(
				[
						self_equal : specialSelf,
						user_equal : user
				]
		)// 4小时题目
		def specialMapRecords
		if (specialMap) {
			specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMap)
		}

		def moreThan7 = specialMapRecords?.size(),
			coefficient = mapCoefficientRepository.findOne(user.id),
			moodContent = '', pic

		def moodPics = userMoodPicRepository.findAll()

        if (coefficient) {
            //计算 正负情感趋势 上升 下降
            def c = Calendar.getInstance()
            c.setTime(new Date())
            c.set(Calendar.HOUR_OF_DAY, 12)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
			c.set(Calendar.MILLISECOND, 0)
            def todayTime = c.getTime().getTime(),
                timeBefore = todayTime - 60 * 60 * 24 * 1000 * 2,
                timeAfter = todayTime + 60 * 60 * 24 * 1000 * 2

            def resultToday = coefficient.mid1 + coefficient.mid2 * Math.cos(todayTime * coefficient.mid4) + coefficient.mid3 * Math.sin(todayTime * coefficient.mid4)
            def resultBefore = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
            def resultAfter = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)

            if (resultBefore <= resultToday && resultToday < resultAfter){ // 上升
				pic = moodPics.find {
					it.type == UserMoodPic.Type.Up
				}.path
                moodContent = MapContent.lowToHighTitle + MapContent.lowToHighContent + moodContent + MapContent.lowToHighMethod
            } else if (resultBefore > resultToday && resultToday >= resultAfter){ // 下降
				pic = moodPics.find {
					it.type == UserMoodPic.Type.Down
				}.path
                moodContent = MapContent.highToLowTitle + MapContent.highToLowContent + moodContent + MapContent.highToLowMethod
            } else if (resultBefore <= resultToday && resultToday >= resultAfter){ // 最高处
				pic = moodPics.find {
					it.type == UserMoodPic.Type.Top
				}.path
                moodContent = MapContent.highTideTitle + MapContent.highTideContent + moodContent
            } else if (resultBefore > resultToday  && resultToday < resultAfter){ // 最底处
				pic = moodPics.find {
					it.type == UserMoodPic.Type.Bottom
				}.path
                moodContent = MapContent.lowTideTitle + MapContent.lowTideContent + moodContent + MapContent.lowTideMethod
            }
        }

        def date = DateUtils.parseDate(time, 'yyyy-MM-dd')

        def moods = userMoodRepository.findTimeMood(date, user)

        def data = []
        moods.each {
            data << [
                'id' : it?.id,
                'moodDay' : it?.lastTime,
                'message' : it?.message,
                'moodType' : it?.moodType?.ordinal()
            ]
        }
        [
            'success' : '1',
            'data' : data,
			'picPath' : pic,
			'moreThan7' : moreThan7,
            'moodContent' : moodContent
        ]

    }

}
