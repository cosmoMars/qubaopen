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
		return helpRepository
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
		
		def helps, data = [], list = []
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
		}
		if (self) {
			if (ids) {
				helps = helpRepository.findByDoctor(doctor, list, pageable)
			} else {
				helps = helpRepository.findByDoctor(doctor, pageable)
			}
			helps.each {
				def comments = helpCommentRepository.findByHelpAndDoctor(it, doctor)
				def commentData = []
				comments.each { cit ->
					def goods = helpCommentGoodRepository.countByHelpComment(cit)
					commentData << [
						'commentId' : cit?.id,
						'doctorId' : cit?.doctor?.id,
						'doctorName' : cit?.doctor?.doctorInfo?.name,
						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
						'doctorContent' : cit?.content,
						'doctorTime' : cit?.time,
						'goods' : goods
					]
						
				}
				data << [
					'helpId' : it?.id,
					'helpContent' : it?.content,
					'helpTime' : DateFormatUtils.format(it?.time, 'yyyy-MM-dd'),
					'userName' : it?.user?.userInfo?.nickName,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'commentData' : commentData
				]
			}
		} else {
			if (ids) {
				helps = helpRepository.findAllHelp(list, pageable)
			} else {
				helps = helpRepository.findAllByPageable(pageable)
			}
			helps.each {
				def comments = helpCommentRepository.findLimitComment(it)
				def commentData = []
				comments.each { cit ->
					def goods = helpCommentGoodRepository.countByHelpComment(cit)
					commentData << [
						'commentId' : cit?.id,
						'doctorId' : cit?.doctor?.id,
						'doctorName' : cit.doctor?.doctorInfo?.name,
						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
						'doctorContent' : cit?.content,
						'doctorTime' : cit?.time,
						'goods' : goods
					]
						
				}
				data << [
					'helpId' : it?.id,
					'helpContent' : it?.content,
					'helpTime' : DateFormatUtils.format(it?.time, 'yyyy-MM-dd'),
					'userName' : it?.user?.userInfo?.nickName,
					'userAvatar' : it?.user?.userInfo?.avatarPath,
					'commentData' : commentData
				]
			}
		}
		def more = true
		if (helps.size() < pageable.pageSize) {
			more = false
		}
		[
			'success' : '1',
			'data' : data,
			'more' : more
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
				'goods' : goods
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
