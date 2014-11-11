package com.qubaopen.survey.service.user

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserMood
import com.qubaopen.survey.entity.user.UserMood.MoodType
import com.qubaopen.survey.repository.user.UserMoodRepository
import com.qubaopen.survey.repository.user.UserRepository;

@Service
class UserMoodService {
	@Autowired
	UserRepository userRepository

	@Autowired
	UserMoodRepository userMoodRepository

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
//		def userMood=userMoodRepository.findOne(user.id);
//		if(userMood){
//			userMood.setMoodType(moodType);
//			userMood.setLastTime(new Date());
//			
//		}else{
//			userMood =new UserMood(
//				id : user.id,
//				moodType : moodType
//			);
//		}
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
			
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT max(id),convert(subString(last_time,1,10),char) last_date,mood_type, message ");
		sql.append("FROM user_mood where ");
		sql.append("user_id="+user.id+" and month(last_time)="+month+" ");
		sql.append("group by last_date ");

		Query query = entityManager.createNativeQuery(sql.toString());

		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();

		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", objects[1]);
			map.put("mood", objects[2]);
			map.put('message', objects[3])
			datalist.add(map);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", '1');
		result.put("moodList", datalist);
		
		return result;
		
	}
}
