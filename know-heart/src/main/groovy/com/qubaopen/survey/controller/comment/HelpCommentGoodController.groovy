package com.qubaopen.survey.controller.comment;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.comment.HelpCommentGood;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.comment.HelpCommentGoodRepository;

@RestController
@RequestMapping('helpCommentGood')
@SessionAttributes('currentUser')
public class HelpCommentGoodController extends AbstractBaseController<HelpCommentGood, Long> {

	private static Logger logger = LoggerFactory.getLogger(HelpCommentGoodController.class)
	
	@Autowired
	HelpCommentGoodRepository userHelpCommentGoodRepository
	
	@Override
	protected MyRepository<HelpCommentGood, Long> getRepository() {
		return userHelpCommentGoodRepository;
	}

	/**
	 * @param commentId
	 * @param user
	 * @return
	 * 点赞
	 */
	@RequestMapping(value = 'commentGood', method = RequestMethod.POST)
	commentGood(@RequestParam(required = false) Long commentId, @ModelAttribute('currentUser') User user) {
		
		logger.trace('-- 点赞 --')
		
		if (commentId) {
			def helpComment = new HelpComment(id : commentId),
				good = userHelpCommentGoodRepository.findByUserAndHelpComment(user, helpComment)
			
			if (good) {
				userHelpCommentGoodRepository.delete(good)
				return '{"success" : "1"}'
			}
			def helpCommentGood = new HelpCommentGood(
				user : user,
				helpComment : helpComment
			)
			userHelpCommentGoodRepository.save(helpCommentGood)
			'{"success" : "1"}'
		}
	}
}
