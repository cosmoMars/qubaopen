package com.knowheart3.controller.comment

import com.knowheart3.repository.comment.HelpCommentGoodRepository
import com.knowheart3.repository.comment.HelpCommentRepository
import com.knowheart3.repository.comment.HelpRepository
import com.knowheart3.repository.user.UserBookingDataRepository
import com.knowheart3.repository.user.UserHelpDataRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateFormatUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

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

	@Autowired
	UserHelpDataRepository userHelpDataRepository

	@Autowired
	UserBookingDataRepository userBookingDataRepository
	
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
		help = helpRepository.save(help)

		[
		    'success' : '1',
			'helpId' : help.id,
			'helpContent' : help.content,
			'helpTime' : DateFormatUtils.format(help.time, 'yyyy-MM-dd'),
			'userName' : user.userInfo.name,
			'userAvatar' : user.userInfo.avatarPath
		]
	}
	
	/**
	 * @param user
	 * @param self
	 * @param pageable
	 * @return
	 * 获取求助信息列表
	 */
	@Transactional
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

		// 当前最新求助
		def commentIds = []
		if (user.id != null) {
			def currentHelp = helpRepository.findCurrentHelp()
			// 查询有新的评论的求助
			def userHelpDatas = userHelpDataRepository.findAll(
					[
							user_equal   : user,
							refresh_equal: true
					]
			)

			userHelpDatas.each {
				it.refresh = false
				it.currentHelpId = currentHelp.id
				commentIds << it.helpComment.id
			}

			userHelpDataRepository.save(userHelpDatas)
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
						'helpId'     : it?.id,
						'userName'   : it?.user?.userInfo?.nickName,
						'helpContent': it?.content,
						'userAvatar' : it?.user?.userInfo?.avatarPath,
						'helpTime'   : DateFormatUtils.format(it?.time, 'yyyy-MM-dd')
				]
			}
		} else {
			helps = helpRepository.findAll(pageable)
			helps.each {
				def comments = helpCommentRepository.findLimitCommentByGood(it)
				def commentData = []
				comments.each { cit ->
					def isGood = false
					if (cit.userId == user.id) {
						isGood = true
					}
					def id, name, avatar, type = 0
					if (cit.doctorId) {
						id = cit.doctorId
						name = cit.doctorName
						avatar = cit.doctorPath
					} else {
						id = cit.hospitalId
						name = cit.hospitalName
						avatar = cit.hospitalPath
						type = 1
					}
					commentData << [
							'commentId'     : cit.commentId,
							'doctorId'      : id,
							'doctorName'    : name,
							'hospitalId'    : id,
							'hospitalName'  : name,
							'doctorAvatar'  : avatar,
							'hospitalAvatar': avatar,
							'content'       : cit.commentContent,
							'time'          : cit.commentTime ? DateFormatUtils.format(cit.commentTime, 'yyyy-MM-dd') : "",
							'goods'         : cit.gSize,
							'isGood'        : isGood,
							'type'          : type
					]
				}
				data << [
						'helpId'     : it?.id,
						'helpContent': it?.content,
						'helpTime'   : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
						'userName'   : it?.user?.userInfo?.nickName,
						'userAvatar' : it?.user?.userInfo?.avatarPath,
						'commentData': commentData
				]
			}
		}
		def more = true
		if (data.size() < pageable.pageSize) {
			more = false
		}
		[
				'success'    : '1',
				'commentIds' : commentIds,
				'commentSize': commentIds.size(),
				'more'       : more,
				'data'       : data
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
			commentData = [], comments,
			commentCount = helpCommentRepository.countByHelp(help)
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
			def id, name, avatar, type = 0

			if (it.doctor) {
				id = it.doctor?.id
				name = it.doctor?.doctorInfo?.name
				avatar = it.doctor?.doctorInfo?.avatarPath
			} else {
				id = it.hospital?.id
				name = it.hospital?.hospitalInfo?.name
				avatar = it.hospital?.hospitalInfo?.hospitalAvatar
				type = 1
			}
			commentData << [
					'commentId'     : it?.id,
					'doctorId'      : id,
					'doctorName'    : name,
					'doctorContent' : it?.content,
					'doctorAvatar'  : avatar,
					'doctorTime'    : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
					'hospitalId'    : id,
					'hospitalName'  : name,
					'hospitalAvatar': avatar,
					'goods'         : gSize,
					'isGood'        : isGood,
					'type'          : type
			]
		}
		if (pageable.pageNumber > 0) {
			return [
					'success'     : '1',
					'helpId'      : '',
					'helpContent' : '',
					'helpTime'    : '',
					'userName'    : '',
					'userAvatar'  : '',
					'more'        : more,
					'data'        : commentData,
					'commentCount': commentCount
			]
		}
		[
				'success'     : '1',
				'helpId'      : help?.id,
				'helpContent' : help?.content,
				'helpTime'    : DateFormatUtils.format(help.time, 'yyyy-MM-dd'),
				'userName'    : help?.user?.userInfo?.nickName,
				'userAvatar'  : help?.user?.userInfo?.avatarPath,
				'more'        : more,
				'data'        : commentData,
				'commentCount': commentCount
		]
	}

	/**
	 * 删除求助
	 * @param id
	 * @param user
	 * @return
	 */
	@Transactional
	@RequestMapping(value = 'deleteHelpById', method = RequestMethod.POST)
	deleteHelpById(@RequestParam long id,
				   @ModelAttribute('currentUser') User user) {

		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}

		def help = new Help(id: id)
		def helpComments = helpCommentRepository.findAll(
				[
						help_equal: help
				]
		)

		if (helpComments) {
			def ids = []
			helpComments.each {
				ids << it.id
			}

			helpCommentRepository.deleteByHelpComentId(ids)
		}
		helpComments.each {
			helpCommentRepository.delete(it)
		}

		helpRepository.delete(help)

		'{"success" : "1"}'

	}

	/**
	 * 获取评论数
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'retrieveCommentCount')
	retrieveCommentCount(@ModelAttribute('currentUser') User user) {

		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}

		// 查询有新的评论的求助
		def userHelpDatas = userHelpDataRepository.findAll(
				[
						user_equal   : user,
						refresh_equal: true
				]
		)
		// 当前最新求助
		def currentHelp = helpRepository.findCurrentHelp()
		def haveNewHelp = false

		userHelpDatas.each {
			// 比对最新的求助
			if (haveNewHelp) {
				if (it.currentHelpId != currentHelp.id) {
					haveNewHelp = true
				}
			}
		}

		def bookingRefresh = false
		def ubd = userBookingDataRepository.findAll(
				[
						user_equal        : user,
						thirdRefresh_equal: true
				]
		)

		if (ubd.size() > 0) {
			bookingRefresh = true
		}

		[
				success       : "1",
				commentCount  : userHelpDatas.size(),
				haveNewHelp   : haveNewHelp,
				bookingRefresh: bookingRefresh
		]

	}


	/**
	 * 获取评论详情
	 * @param ids
	 * @param user
	 */
	@RequestMapping(value = 'retrieveCommentDetial')
	retrieveCommentDetial(@RequestParam(required = false) String ids,
						  @ModelAttribute('currentUser') User user) {

		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}

		def idsList = [], data = []
		if (ids == null) {
			return [
					success: '1',
					data   : data
			]
		}
		def strIds = ids.split(',')
		strIds.each {
			idsList << Long.valueOf(it.trim())
		}

		def helpComments = helpCommentRepository.findByIds(idsList)

		helpComments.each {

			def id, name, avatar, type = 0 // 0医师，1诊所

			if (it.doctor.id) {
				id = it.doctor.id
				name = it.doctor.doctorInfo.name
				avatar = it.doctor.doctorInfo.avatarPath
			} else {
				id = it.hospital.id
				name = it.hospital.hospitalInfo.name
				avatar = it.hospital.hospitalInfo.hospitalAvatar
				type = 1
			}
			data << [
					helpId       : it.help.id,
					helpContent  : it.help.content,
					helpTime     : it.help.time ? DateFormatUtils.format(it.help.time, 'yyyy-MM-dd') : "",
					userName     : it.help.user.userInfo.name,
					userAvatar   : it.help.user.userInfo.avatarPath,
					helpCommentId: it.id,
					content      : it.content,
					id           : id,
					name         : name,
					avatar       : avatar,
					time         : it.time,
					type         : type
			]


		}
		[
				success: '1',
				data   : data
		]
	}

}
