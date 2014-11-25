package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserHelpComment
import com.qubaopen.survey.entity.user.UserHelpCommentGood
import com.qubaopen.survey.repository.user.UserHelpCommentGoodRepository

@RestController
@RequestMapping('userHelpCommentGood')
@SessionAttributes('currentUser')
public class UserHelpCommentGoodController extends AbstractBaseController<UserHelpCommentGood, Long> {

	@Autowired
	UserHelpCommentGoodRepository userHelpCommentGoodRepository
	
	@Override
	protected MyRepository<UserHelpCommentGood, Long> getRepository() {
		return userHelpCommentGoodRepository;
	}

	@RequestMapping(value = 'commentGood', method = RequestMethod.POST)
	commentGood(@RequestParam(required = false) Long commentId, @ModelAttribute('currentUser') User user) {
		
		if (commentId) {
			def userHelpComment = new UserHelpComment(id : commentId),
				good = userHelpCommentGoodRepository.findByUserAndUserHelpComment(user, userHelpComment)
			
			if (good) {
				return '{"success" : "0", "message" : "已评价"}'
			}
			def userHelpCommentGood = new UserHelpCommentGood(
				user : user,
				userHelpComment : userHelpComment
			)
			userHelpCommentGoodRepository.save(userHelpCommentGood)
			'{"success" : "1"}'
		}
	}
}
