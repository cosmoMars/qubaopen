package com.qubaopen.survey.service.user

import javax.servlet.http.HttpServletRequest;

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

	@Transactional
	saveUserMood(User user, MoodType moodType) {
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
			lastTime : new Date()	
		)
		
		userMoodRepository.save(userMood);
		
		return ['success' : '1','moodType':userMood.moodType.ordinal(),'lastTime':userMood.lastTime]		
	}
	
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
}
