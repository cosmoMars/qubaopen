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
		def lastUserMood=userMoodRepository.getLastUserMood(user.id);
		lastUserMood.getCreatedDate();
		//DateUtils.isSameDay(null, null);
		def userMood = new UserMood(
			user : user,
			moodType : moodType
		)
		userMoodRepository.save(userMood);

		def result = ['success' : "1"]
		lastUserMood
	}
}
