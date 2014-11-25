package com.qubaopen.survey.controller.user;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserHelp
import com.qubaopen.survey.repository.user.UserHelpCommentGoodRepository;
import com.qubaopen.survey.repository.user.UserHelpCommentRepository
import com.qubaopen.survey.repository.user.UserHelpRepository

@RestController
@RequestMapping('userHelp')
@SessionAttributes('currentUser')
public class UserHelpController extends AbstractBaseController<UserHelp, Long> {

	private static Logger logger = LoggerFactory.getLogger(UserHelpController.class)
	@Autowired
	UserHelpRepository userHelpRepository
	
	@Autowired
	UserHelpCommentRepository userHelpCommentRepository
	
	@Autowired
	UserHelpCommentGoodRepository userHelpCommentGoodRepository
	
	@Override
	protected MyRepository<UserHelp, Long> getRepository() {
		return userHelpRepository;
	}
	
	
	@RequestMapping(value = 'addHelp', method = RequestMethod.POST)
	addHelp(@RequestParam(required = false) String content, @ModelAttribute('currentUser') User user) {
		
		if (content) {
			def userHelp = new UserHelp (
				user : user,
				time : new Date(),
				content : content
			)
			userHelpRepository.save(userHelp)
			'{"success" : "1"}'
		} else {
			'{"success" : "0", "message" : "没有内容"}'
		}
	}
	
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.GET)
	retrieveHelpComment(@ModelAttribute('currentUser') User user,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {

		def helps = userHelpRepository.findAll(
			[
				user_equal : user	
			], pageable
		)
		
		def data = []
		helps.each {
			def comments = userHelpCommentRepository.findByUserHelp(it)
			def commentData = []
			comments.each { cit ->
				def goods = userHelpCommentGoodRepository.countByUserHelpComment(cit)
				commentData << [
					'doctorId' : cit.doctor?.id,
					'doctorName' : cit.doctor?.doctorInfo?.name,
					'doctorContent' : cit.content,
					'doctorTime' : cit.time,
					'goods' : goods
				]
					
			}
			data << [
				'helpId' : it.id,
				'helpContent' : it.content,
				'helpTime' : it.time,
				'commentData' : commentData
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}

}
