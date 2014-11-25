package com.qubaopen.survey.controller.user;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.UserHelp
import com.qubaopen.survey.entity.user.UserHelpComment
import com.qubaopen.survey.repository.user.UserHelpCommentRepository

@RestController
@RequestMapping('userHelpComment')
@SessionAttributes('currentUser')
public class UserHelpCommentController extends AbstractBaseController<UserHelpComment, Long> {

	private static Logger logger = LoggerFactory.getLogger(UserHelpCommentController.class)
	
	@Autowired
	UserHelpCommentRepository userHelpCommentRepository
	
	@Override
	protected MyRepository<UserHelpComment, Long> getRepository() {
		return userHelpCommentRepository;
	}
	
	@RequestMapping(value = 'commentHelp', method = RequestMethod.POST)
	commentHelp(@RequestParam(required = false) Long helpId,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) Long doctorId
		) {
		
		logger.trace('-- 医师评论 --')
		if (!helpId || !doctorId) {
			return '{"success" : "0", "message" : "信息不完整"}'
		}
		
		def helpComment = new UserHelpComment(
			userHelp : new UserHelp(id : helpId),
			content : content,
			doctor : new Doctor(id : doctorId),
			time : new Date()
		)
		userHelpCommentRepository.save(helpComment)
		'{"success" : "1"}'
	}
}
