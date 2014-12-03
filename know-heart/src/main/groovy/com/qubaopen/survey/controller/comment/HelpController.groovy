package com.qubaopen.survey.controller.comment;

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
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.comment.HelpCommentGoodRepository;
import com.qubaopen.survey.repository.comment.HelpCommentRepository;
import com.qubaopen.survey.repository.comment.HelpRepository;

@RestController
@RequestMapping('help')
@SessionAttributes('currentUser')
public class HelpController extends AbstractBaseController<Help, Long> {

	private static Logger logger = LoggerFactory.getLogger(HelpController.class)
	@Autowired
	HelpRepository helpRepository
	
	@Autowired
	HelpCommentRepository helpCommentRepository
	
	@Autowired
	HelpCommentGoodRepository helpCommentGoodRepository
	
	@Override
	protected MyRepository<Help, Long> getRepository() {
		return helpRepository;
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
		def help = new Help (
			user : user,
			time : new Date(),
			content : content.trim()
		)
		helpRepository.save(help)
		'{"success" : "1"}'
	}
	
	/**
	 * @param user
	 * @param self
	 * @param pageable
	 * @return
	 * 获取求助信息列表
	 */
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.GET)
	retrieveHelpComment(@RequestParam(required = false) Boolean self,
		@ModelAttribute('currentUser') User user,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {
		
		logger.trace('-- 获取求助信息 --')
		
		def helps
		if (self) {
			helps = helpRepository.findAll(
				[
					user_equal : user
				], pageable
			)
		} else {
			helps = helpRepository.findAll(pageable)
		}
		def data = []
		helps.each {
			def comments = helpCommentRepository.findByHelp(it)
			def commentData = []
			comments.each { cit ->
				def goods = helpCommentGoodRepository.countByHelpComment(cit)
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
