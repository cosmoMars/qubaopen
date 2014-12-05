package com.qubaopen.survey.controller.comment;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.POST)
	retrieveHelpComment(@RequestParam(required = false) Boolean self,
		@RequestParam(required = false) String ids,
		@ModelAttribute('currentUser') User user,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {
		
		logger.trace('-- 获取求助信息 --')
		
		def helps, data = [], list = []
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
		}
		if (self) {
			if (ids) {
				helps = helpRepository.findByUser(user, list, pageable)
			} else {
				helps = helpRepository.findAll(
					[
						user_equal : user
					], pageable
				)
			}
			
			helps.each {
				data << [
					'helpId' : it?.id,
					'userName' : it?.user?.userInfo?.nickName,
					'helpContent' : it?.content,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'helpTime' : DateFormatUtils.format(it?.time, 'yyyy-MM-dd')
				]
			}
		} else {
			helps = helpRepository.findAll(pageable)
			helps.each {
				def comments = helpCommentRepository.findLimitComment(it)
				def commentData = []
				comments.each { cit ->
					commentData << [
						'doctorId' : cit?.doctor?.id,
						'doctorName' : cit?.doctor?.doctorInfo?.name,
						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
						'doctorContent' : cit?.content,
						'doctorTime' : DateFormatUtils.format(cit.time, 'yyyy-MM-dd')
					]
						
				}
				data << [
					'helpId' : it?.id,
					'helpContent' : it?.content,
					'helpTime' : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
					'userName' : it?.user?.userInfo?.nickName,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'commentData' : commentData
				]
			}
		}
		[
			'success' : '1',
			'data' : data
		]
	}
		
	/**
	 * @param id
	 * @param user
	 * @return
	 * 详细评论
	 */
	@RequestMapping(value = 'retrieveDetailHelp', method = RequestMethod.POST)
	retrieveDetailHelp(@RequestParam long helpId,
		@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 20,  sort = 'createdDate', direction = Direction.ASC)
		Pageable pageable,
		@ModelAttribute('currentUser') User user) {
	
		logger.trace '-- 获取详细评论 --'
		
		
		def help = helpRepository.findOne(helpId),
			commentData = [], comments
		if (ids) {
			def list = [],
				strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
			comments = helpCommentRepository.findByHelp(help, list, pageable)
		} else {
			comments = helpCommentRepository.findByHelp(help, pageable)
		}
		def more = true
		if (comments.size() < pageable.pageSize) {
			more = false
		}
			
		comments.each {
			def goods = helpCommentGoodRepository.countByHelpComment(it)
			commentData << [
				'doctorId' : it?.doctor?.id,
				'doctorName' : it?.doctor?.doctorInfo?.name,
				'doctorContent' : it?.content,
				'doctorAvatar' : it?.doctor?.doctorInfo?.avatarPath,
				'doctorTime' : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
				'goods' : goods,
				'more' : more
			]
		}
		if (pageable.pageNumber > 0) {
			return [
				'success' : '1',
				'helpId' : '',
				'helpContent' : '',
				'helpTime' : '',
				'userName' : '',
				'userAvatar' : '',
				'data' : commentData
			]
		}
		[
			'success' : '1',
			'helpId' : help?.id,
			'helpContent' : help?.content,
			'helpTime' : DateFormatUtils.format(help.time, 'yyyy-MM-dd'),
			'userName' : help?.user?.userInfo?.nickName,
			'userAvatar' : help?.user?.userInfo?.avatarPath,
			'data' : commentData
		]
	}

}
