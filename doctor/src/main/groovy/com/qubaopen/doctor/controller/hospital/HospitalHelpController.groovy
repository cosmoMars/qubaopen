package com.qubaopen.doctor.controller.hospital

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.comment.HelpCommentGoodRepository
import com.qubaopen.doctor.repository.comment.HelpCommentRepository
import com.qubaopen.doctor.repository.comment.HelpRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('hospitalHelp')
@SessionAttributes('currentHospital')
public class HospitalHelpController extends AbstractBaseController<Help, Long>{

	@Autowired
	HelpCommentRepository helpCommentRepository
	
	@Autowired
	HelpRepository helpRepository
	
	@Autowired
	HelpCommentGoodRepository helpCommentGoodRepository
	
	@Override
	MyRepository<Help, Long> getRepository() {
		helpCommentRepository
	}
	
	
	/**
	 * @param self
	 * @param pageable
	 * @param hospital
	 * @return
	 * 获取评论
	 */
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.POST)
	retrieveHelpComment(@RequestParam(required = false) Boolean self,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC) Pageable pageable,
		@ModelAttribute('currentHospital') Hospital hospital) {
		
		def helps, data = []
		if (self) {
			helps = helpRepository.findByHospital(hospital, pageable)
		} else {
			helps = helpRepository.findAllByPageable(pageable)
		}
		helps.each {
			def comments = helpCommentRepository.findLimitComment(it),
				commentSize =  helpCommentRepository.countCommentByHelp(it)
			def commentData = []
			comments.each { cit ->
				def goods = helpCommentGoodRepository.countByHelpComment(cit)
				def id, name, avatar, type = 0 // 0医师，1诊所
				if (cit?.doctor) {
					id = cit?.doctor?.id
					name = cit?.doctor?.doctorInfo?.name
					avatar = cit?.doctor?.doctorInfo?.avatarPath
				} else {
					id = cit?.hospital?.id
					name = cit?.hospital?.hospitalInfo?.name
					avatar = cit?.hospital?.hospitalInfo?.hospitalAvatar
					type = 1
				}



				commentData << [
						'commentId': cit?.id,
						'id'       : id,
						'name'     : name,
						'avatar'   : avatar,
						'content'  : cit?.content,
						'time'     : cit?.time,
						'goods'    : goods,
						'type'     : type
				]
					
			}
			data << [
					'helpId'     : it?.id,
					'helpContent': it?.content,
					'helpTime'   : DateFormatUtils.format(it?.time, 'yyyy-MM-dd'),
					'userName'   : it?.user?.userInfo?.nickName,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'userSex'    : it?.user?.userInfo?.sex?.ordinal(),
					'commentSize': commentSize,
					'commentData': commentData
			]
		}
		
		def more = true
		if (helps.size() < pageable.pageSize) {
			more = false
		}
		[
				'success': '1',
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
			def id, name, avatar, type = 0 // 0医师，1诊所

			if (it.doctor) {
				id = it?.doctor?.id
				name = it?.doctor?.doctorInfo?.name
				avatar = it?.doctor?.doctorInfo?.avatarPath
			} else {
				id = it?.hospital?.id
				name = it?.hospital?.hospitalInfo?.name
				avatar = it?.hospital?.hospitalInfo?.hospitalAvatar
				type = 1
			}
			commentData << [
					'commentId': it?.id,
					'id'       : id,
					'name'     : name,
					'avatar'   : avatar,
					'content'  : it?.content,
					'time'     : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
					'goods'    : goods,
					'type'     : type
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
	 * @param hospital
	 * @return
	 */
	@RequestMapping(value = 'retrieveGoodComment', method = RequestMethod.POST)
	retrieveGoodComment(@RequestParam(required = false) String ids,
						@ModelAttribute('currentHospital') Hospital hospital) {

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

	/**
	 * 获取点赞信息
	 * @param ids
	 * @param hospital
	 * @return
	 */
	@RequestMapping(value = 'retrieveGoodHelpComment', method = RequestMethod.POST)
	retrieveGoodHelpComment(@RequestParam(required = false) String ids,
							@ModelAttribute('currentHospital') Hospital hospital) {

		def idList = []

		if (ids) {
			def idStr = ids.split(',')

			idStr.each {
				idList << Long.valueOf(it.trim())
			}
		}
		if (idList.size() <= 0) {
			return '{"success" : "1"}'
		}
		// 查找点赞纪录
		def goodComments = helpCommentGoodRepository.findByIds(idList),
			data = []

		def now = new Date()

		goodComments.each {
			it.view = true

			def minTime = (now.time - it.createdDate.millis) / 1000 / 60 as int,
				time
			if (minTime < 60) {
				time = "${minTime == 0 ? 1 : minTime}分钟之前" as String
			} else {
				time = DateFormatUtils.format(it.createdDate.toDate(), 'M月d日 H:mm') as String
			}

			data << [
					helpId        : it.id,
					userAvatar    : it.user?.userInfo?.avatarPath,
					userName      : it.user?.userInfo?.nickName,
					hospitalName  : it.helpComment?.hospital?.hospitalInfo?.name,
					commentContent: it.helpComment?.content,
					time          : time
			]
		}

		helpCommentGoodRepository.save(goodComments)

		[
				success: '1',
				data   : data
		]
	}

}
