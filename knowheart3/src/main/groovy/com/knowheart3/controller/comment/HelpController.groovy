package com.knowheart3.controller.comment;

import org.apache.commons.lang3.time.DateFormatUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

import com.knowheart3.repository.comment.HelpCommentGoodRepository
import com.knowheart3.repository.comment.HelpCommentRepository
import com.knowheart3.repository.comment.HelpRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.user.User

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

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
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
//				def comments = helpCommentRepository.findLimitComment(it)
//				def commentData = []
//				comments.each { cit ->
//					def goods = helpCommentGoodRepository.findByHelpComment(cit),
//						gSize = 0
//					if (goods) {
//						gSize = goods.size()
//					}
//					def isGood = goods.any { g ->
//						g.user == user
//					}
//					commentData << [
//						'commentId' : cit?.id,
//						'doctorId' : cit?.doctor?.id,
//						'doctorName' : cit?.doctor?.doctorInfo?.name,
//						'hospitalName' : cit?.hospital?.hospitalInfo?.name,
//						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
//						'content' : cit?.content,
//						'time' : DateFormatUtils.format(cit?.time, 'yyyy-MM-dd'),
//						'goods' : gSize,
//						'isGood' : isGood
//					]
//
//				}
				def comments = helpCommentRepository.findLimitCommentByGood(it)
				def commentData = []
				comments.each { cit ->
					def isGood = false
					if (cit.userId == user.id) {
						isGood = true
					}
					commentData << [
						'commentId' : cit.commentId,
						'doctorId' : cit.doctorId,
						'doctorName' : cit.doctorName,
						'hospitalId' : cit.hospitalId,
						'hospitalName' : cit.hospitalName,
						'doctorAvatar' : cit.doctorPath,
						'hospitalAvatar' : cit.hospitalPath,
						'content' : cit.commentContent,
						'time' : cit.commentTime ? DateFormatUtils.format(cit.commentTime, 'yyyy-MM-dd') : "",
						'goods' : cit.gSize,
						'isGood' : isGood
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
			def goods = helpCommentGoodRepository.findByHelpComment(it),
				gSize = 0
			if (goods) {
				gSize = goods.size()
			}
			def isGood = goods.any { g ->
				g.user == user
			}
			commentData << [
				'commentId' : it?.id,
				'doctorId' : it?.doctor?.id,
				'doctorName' : it?.doctor?.doctorInfo?.name,
				'doctorContent' : it?.content,
				'doctorAvatar' : it?.doctor?.doctorInfo?.avatarPath,
				'doctorTime' : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
				'goods' : gSize,
				'isGood' : isGood
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
				'more' : more,
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
			'more' : more,
			'data' : commentData
		]
	}

}
