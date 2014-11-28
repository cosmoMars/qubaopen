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
	
	
	/**
	 * @param content
	 * @param user
	 * @return
	 * 添加求助言论
	 */
	@RequestMapping(value = 'addHelp', method = RequestMethod.POST)
	addHelp(@RequestParam(required = false) String content, @ModelAttribute('currentUser') User user) {
		
		logger.trace('-- 添加求助 --')
		
		if (!content) {
			return '{"success" : "0", "message" : "没有内容"}'
		}
		def userHelp = new UserHelp (
			user : user,
			time : new Date(),
			content : content.trim()
		)
		userHelpRepository.save(userHelp)
		'{"success" : "1"}'
	}
	
	/**
	 * @param user
	 * @param self
	 * @param pageable
	 * @return
	 * 获取求助信息
	 */
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.GET)
	retrieveHelpComment(@ModelAttribute('currentUser') User user,
		@RequestParam(required = false) Boolean self,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {
		
		logger.trace('-- 获取求助信息 --')
		
		def helps
		if (self) {
			helps = userHelpRepository.findAll(
				[
					user_equal : user
				], pageable
			)
		} else {
			helps = userHelpRepository.findAll(pageable)
		}
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
