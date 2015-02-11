package com.qubaopen.doctor.controller.hospital;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.comment.HelpCommentGoodRepository
import com.qubaopen.doctor.repository.comment.HelpCommentRepository
import com.qubaopen.doctor.repository.comment.HelpRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.user.User;

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
				commentData << [
					'commentId' : cit?.id,
					'doctorId' : cit?.doctor?.id,
					'doctorName' : cit.doctor?.doctorInfo?.name,
					'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
					'hospitalName' : cit?.hospital?.hospitalInfo?.name,
					'content' : cit?.content,
					'time' : cit?.time,
					'goods' : goods
				]
					
			}
			data << [
				'helpId' : it?.id,
				'helpContent' : it?.content,
				'helpTime' : DateFormatUtils.format(it?.time, 'yyyy-MM-dd'),
				'userName' : it?.user?.userInfo?.nickName,
				'userAvatar' : it?.user?.userInfo?.avatarPath,
				'userSex' : it?.user?.userInfo?.sex?.ordinal(),
				'commentSize' : commentSize,
				'commentData' : commentData
			]
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
				'commentId' : it?.id,
				'doctorId' : it?.doctor?.id,
				'doctorName' : it?.doctor?.doctorInfo?.name,
				'hospitalName' : it?.hospital?.hospitalInfo?.name,
				'content' : it?.content,
				'doctorAvatar' : it?.doctor?.doctorInfo?.avatarPath,
				'time' : DateFormatUtils.format(it.time, 'yyyy-MM-dd'),
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
