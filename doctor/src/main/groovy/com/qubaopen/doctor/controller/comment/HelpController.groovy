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
						'doctorId' : cit.doctor?.id,
						'doctorName' : cit.doctor?.doctorInfo?.name,
						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
						'doctorContent' : cit.content,
						'doctorTime' : cit.time,
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
				helps = helpRepository.findAll(pageable)
			}
			helps.each {
				def comments = helpCommentRepository.findLimitComment(it)
				def commentData = []
				comments.each { cit ->
					def goods = helpCommentGoodRepository.countByHelpComment(cit)
					commentData << [
						'doctorId' : cit.doctor?.id,
						'doctorName' : cit.doctor?.doctorInfo?.name,
						'doctorAvatar' : cit?.doctor?.doctorInfo?.avatarPath,
						'doctorContent' : cit.content,
						'doctorTime' : cit.time,
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
		
		[
			'success' : '1',
			'data' : data
		]
	}

}
