package com.qubaopen.doctor.controller.comment;

import org.apache.commons.lang3.time.DateFormatUtils
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
import com.qubaopen.doctor.repository.comment.HelpCommentGoodRepository
import com.qubaopen.doctor.repository.comment.HelpCommentRepository
import com.qubaopen.doctor.repository.comment.HelpRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User;

@RestController
@RequestMapping('help')
@SessionAttributes('currentDoctor')
public class HelpController extends AbstractBaseController<Help, Long> {

	@Autowired
	HelpRepository helpRepository
	
	@Autowired
	HelpCommentRepository helpCommentRepository
	
	@Autowired
	HelpCommentGoodRepository helpCommentGoodRepository

	@Override
	MyRepository<Help, Long> getRepository() {
		helpRepository
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
		@ModelAttribute('currentDoctor') Doctor doctor,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {
		
		logger.trace('-- 获取求助信息 --')

		def helps, data = [], list = [], size = 0
		/*if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
		}*/

		def hcGoods = helpCommentGoodRepository.findAll(
				[
						'helpComment.doctor_equal': doctor,
						view_isFalse              : null,
				]
		)

		hcGoods.each {
			list << it.id
		}

		if (self) {
			helps = helpRepository.findByDoctor(doctor, pageable)
			size = helps ? helps.size() : 0

		} else {
			helps = helpRepository.findAll(pageable)
			size = helps ? helps.getContent().size() : 0
		}
		helps.each {
			// 查找评论详细集合
			def comments
			if (self) {
				comments = helpCommentRepository.findLimitCommentByGood(it, doctor)
			} else {
				comments = helpCommentRepository.findLimitCommentByGood(it)
			}

			def commentSize = helpCommentRepository.countCommentByHelp(it)
			def commentData = []
			comments.each { cit ->
				commentData << [
						'commentId'     : cit.commentId,
						'doctorId'      : cit.doctorId,
						'doctorName'    : cit.doctorName,
						'hospitalId'    : cit.hospitalId,
						'hospitalName'  : cit.hospitalName,
						'doctorAvatar'  : cit.doctorPath,
						'hospitalAvatar': cit.hospitalPath,
						'content'       : cit.commentContent,
						'time'          : cit.commentTime ? DateFormatUtils.format(cit.commentTime, 'yyyy-MM-dd') : "",
						'goods'         : cit.gSize
				]
			}
			data << [
					'helpId'     : it?.id,
					'helpContent': it?.content,
					'helpTime'   : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
					'userName'   : it?.user?.userInfo?.nickName,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'commentSize': commentSize,
					'commentData': commentData
			]
		}
		def more = true
		if (size < pageable.pageSize) {
			more = false
		}

		[
				'success': '1',
				'hcGoods': hcGoods ? hcGoods.size() : 0,
				'hcIds'  : list,
				'data'   : data,
				'more'   : more
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
		@PageableDefault(page = 0, size = 20,  sort = 'createdDate', direction = Direction.DESC)
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
					'commentId'   : it?.id,
					'doctorId'    : it?.doctor?.id,
					'doctorName'  : it?.doctor?.doctorInfo?.name,
					'hospitalName': it?.hospital?.hospitalInfo?.name,
					'content'     : it?.content,
					'doctorAvatar': it?.doctor?.doctorInfo?.avatarPath,
					'time'        : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
					'goods'       : goods
			]
		}
		if (pageable.pageNumber > 0) {
			return [
					'success'    : '1',
					'helpId'     : '',
					'helpContent': '',
					'helpTime'   : '',
					'userName'   : '',
					'userAvatar' : '',
					'more'       : more,
					'data'       : commentData
			]
		}
		[
			'success'    : '1',
			'helpId'     : help?.id,
			'helpContent': help?.content,
			'helpTime'   : DateFormatUtils.format(help.time, 'yyyy-MM-dd'),
			'userName'   : help?.user?.userInfo?.nickName,
			'userAvatar' : help?.user?.userInfo?.avatarPath,
			'more'       : more,
			'data'       : commentData
		]
	}

	/**
	 * 查看被点赞的评论
	 * @param ids
	 * @param doctor
	 */
	@RequestMapping(value = 'retrieveGoodComment', method = RequestMethod.POST)
	retrieveGoodComment(@RequestParam(required = false) String ids,
						@ModelAttribute('currentDoctor') Doctor doctor) {

		def list = [], commentIds = [], data = [], commentMap = [:]
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
		}
		if (list.size() <= 0) {
			return '{"success" : "1"}'
		}
		// 查找点赞纪录
		def goodComments = helpCommentGoodRepository.findByIds(list)
		goodComments.each {
			// 设置已阅
			it.view = true
			commentIds << it.helpComment.id

			if (commentMap.get(it.helpComment)) {
				def size = commentMap.get(it.helpComment) + 1
				commentMap.put(it.helpComment, size)
			} else {
				commentMap.put(it.helpComment, 1)
			}
		}

		// 查找点赞的求助主题
		def helps = helpRepository.findByComment(commentIds)

		helps.each {
			def commentData = []

			commentMap.each { k, v ->

				// 比配求助
				if (k.help.id == it.id) {
					commentData << [
							commentId     : k.id,
							commentContent: k.content,
							commentTime   : k.time,
							commentSize   : v
					]
				}

			}

			data << [
					helpId     : it.id,
					helpContent: it.content,
					helpTime   : it.time,
					commentData: commentData
			]
		}

		helpCommentGoodRepository.save(goodComments)

		[
				success: '1',
				data   : data
		]
	}
}
