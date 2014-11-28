package com.qubaopen.doctor.controller.comment;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.comment.HelpCommentGoodRepository;
import com.qubaopen.doctor.repository.comment.HelpCommentRepository;
import com.qubaopen.doctor.repository.comment.HelpRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.doctor.Doctor

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
	 * 获取求助信息
	 */
	@RequestMapping(value = 'retrieveHelpComment', method = RequestMethod.GET)
	retrieveHelpComment(@RequestParam(required = false) Boolean self,
		@ModelAttribute('currentDoctor') Doctor doctor,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable) {
		
		logger.trace('-- 获取求助信息 --')
		
		def helps
		if (self) {
			helps = helpRepository.findAll(
				[
					doctor_equal : doctor
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
