package com.knowheart3.service.user
import com.knowheart3.repository.user.UserRepository
import com.knowheart3.repository.user.UserShareRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserShare
import com.qubaopen.survey.entity.user.UserShare.ShareOrigin
import com.qubaopen.survey.entity.user.UserShare.ShareTarget
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserShareService {
	@Autowired
	UserRepository userRepository
	
	@Autowired
	UserShareRepository userShareRepository
	
	@Transactional
	saveUserShare(User user,ShareTarget target, ShareOrigin origin, String remark) {
	
		def userShare = new UserShare(
			user:user,
			shareTarget : target,
			shareOrigin : origin,
			remark : remark
			)
		userShareRepository.save(userShare);
		
		def result = ['success' : "1"]
		result
	}
}
