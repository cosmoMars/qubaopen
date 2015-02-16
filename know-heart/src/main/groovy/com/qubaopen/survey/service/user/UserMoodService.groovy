package com.qubaopen.survey.service.user

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.mindmap.MapCoefficient;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserMood
import com.qubaopen.survey.entity.user.UserMood.MoodType
import com.qubaopen.survey.repository.mindmap.MapCoefficientRepository
import com.qubaopen.survey.repository.user.UserMoodRepository
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.service.mindmap.MapContent

@Service
class UserMoodService {
	
	@Autowired
	UserRepository userRepository

	@Autowired
	UserMoodRepository userMoodRepository
	
	@Autowired
	MapCoefficientRepository mapCoefficientRepository

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 用户提交心情
	 * @param user
	 * @param moodType
	 * @return
	 */
	@Transactional
	saveUserMood(User user, MoodType moodType, String message) {
		def userMood = new UserMood(
			user : user,
			moodType : moodType,
			lastTime : new Date(),
			message : message
		)
		
		userMoodRepository.save(userMood);
		
		return ['success' : '1','moodType':userMood.moodType.ordinal(),'lastTime':userMood.lastTime, 'message' : message]		
	}
	
	
	/**
	 * 获取最后一次心情 停用并 已并入登录接口
	 * @param user
	 * @return
	 */
	@Transactional
	getUserMood(User user) {
		def userMood=userMoodRepository.findOne(user.id);
		if(userMood==null){
			userMood =new UserMood(
				id : user.id
			);
		}
		userMoodRepository.save(userMood);
		
		return ['success' : '1','moodType':userMood.moodType.ordinal(),'lastTime':userMood.lastTime]
	}
	
	/**
	 * 获取用户指定月份的心情数据 （取每日的最后一次）
	 * @param user
	 * @param month
	 * @return
	 */
	@Transactional
	getUserMood(User user,int month) {
		
		def userMoods = userMoodRepository.retrieveUserMoodByMonth(user, month)
		
		def datalist = []
		userMoods.each {
			datalist << [
				'date' : DateFormatUtils.format(it.lastTime, 'yyyy-MM-dd'),
				'mood' : it.moodType.ordinal(),
				'message' : it.message
			]
		}
		
		return ["success" : "1", "moodList" : datalist]
	}
	
	
	/**
	 * @param month
	 * @param user
	 * @return
	 * 获取当月信息
	 */
	@Transactional
	def retrieveMoodList(String month, User user, MapCoefficient coefficient) {
		
		// 获取月数据
		def nowMonth
		if (month == null) {
			nowMonth = new Date()
		}
		nowMonth = DateUtils.parseDate(month, 'yyyy-MM')
		def moods = userMoodRepository.findMonthMood(nowMonth, user)
		
		def dayMoods = [:]
		moods.each {
			def moodTime = DateFormatUtils.format(it.lastTime, 'yyyy-MM-dd')
			if (dayMoods.get(moodTime)) {
				dayMoods.get(moodTime) << it
			} else {
				def list = []
				list << it
				dayMoods.put(moodTime, list)
			}
		}
		
		def strTime = month.split('-'),
			y = strTime[0] as int,
			m = strTime[1] as int,
			day = getMonthLastDay(y, m)
			
		
		def strHourTime = DateFormatUtils.format(new Date(), 'HH:mm').split(':'),
			h = strHourTime[0] as int,
			min = strHourTime[1] as int
		
		
		def c = Calendar.getInstance()
		c.set(Calendar.YEAR, y)
		c.set(Calendar.MONTH, m - 1)
		c.set(Calendar.DAY_OF_MONTH, 1)
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		
		println c.getTime()
		
		def monthData = []
		for(i in 1..day) {
			def dayData = []
			if (i > 1)
				c.add(Calendar.DATE, 1)
			def todayTime = DateFormatUtils.format(c.getTime(), 'yyyy-MM-dd')
			if (dayMoods.get(todayTime)) {
				def ms = dayMoods.get(todayTime)
				ms.each {
					dayData << [
						'id' : it?.id,
						'moodDay' : it?.lastTime,
						'message' : it?.message,
						'moodType' : it?.moodType?.ordinal()
					]
				}
			}
			def status = ''
			if (coefficient) {
				//计算 正负情感趋势 上升 下降
				def time = c.getTimeInMillis(),
					timeBefore = time - 60 * 60 * 24 * 1000 * 2,
					timeAfter = time + 60 * 60 * 24 * 1000 * 2
				
				def resultToday = coefficient.mid1 + coefficient.mid2 * Math.cos(time * coefficient.mid4) + coefficient.mid3 * Math.sin(time * coefficient.mid4)
				def resultBefore = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
				def resultAfter = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
			
				if (resultBefore <= resultToday && resultToday >= resultAfter){ // 最高处
					status = '1' as int
				} else if (resultBefore > resultToday && resultToday < resultAfter){ // 最底处
					status = '2' as int
				}
			}
			
			monthData << [
				'day' : todayTime,
				'dayData' : dayData,
				'status' : status
			]
		}
		
		monthData
	}
	
	int getMonthLastDay(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, 1);//把日期设置为当月第一天
		c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = c.get(Calendar.DATE);
		return maxDate;
	}
}
