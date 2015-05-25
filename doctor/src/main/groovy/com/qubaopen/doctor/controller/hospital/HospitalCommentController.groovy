package com.qubaopen.doctor.controller.hospital;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.comment.HelpCommentRepository
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.comment.HelpComment
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalInfo

@RestController
@RequestMapping('hospitalComment')
@SessionAttributes('currentHospital')
public class HospitalCommentController extends AbstractBaseController<HelpComment, Long>{

	static Logger logger = LoggerFactory.getLogger(HospitalCommentController.class)
	
	@Autowired
	HelpCommentRepository helpCommentRepository
	
	@Autowired
	HospitalInfoRepository hospitalInfoRepository
	
	@Override
	MyRepository<HelpComment, Long> getRepository() {
		helpCommentRepository
	}
	
	/**
	 * @param helpId
	 * @param content
	 * @param hospital
	 * @return
	 * 诊所评论
	 */
	@RequestMapping(value = 'addComment', method = RequestMethod.POST)
	addComment(@RequestParam long helpId,
		@RequestParam(required = false) String content,
		@ModelAttribute('currentHospital') Hospital hospital) {
		 
		logger.trace '-- 诊所评论 --'
		
		def hi = hospitalInfoRepository.findOne(hospital.id)
		
		//诊所未通过认证 不能评论，除非是知小心
		if (hi.loginStatus != HospitalInfo.LoginStatus.Audited && hi.id!=1l) {
			return '{"success" : "0", "message" : "err1000"}'
		}
		
		def helpComment = new HelpComment(
			help : new Help(id : helpId),
			content : content,
			hospital : hospital,
			time : new Date()
		)
		helpComment = helpCommentRepository.save(helpComment)
		[
			'success' : '1',
			'commentId' : helpComment.id
		]
		
	}

	/**
	 * @param id
	 * @param content
	 * @param doctor
	 * @return
	 * 医师修改评论
	 */
	@RequestMapping(value = 'modifyHospitalComment', method = RequestMethod.POST)
	modifyHospitalComment(@RequestParam long id,
		@RequestParam(required = false) String content,
		@ModelAttribute('currentHospital') Hospital hospital) {
		
		logger.trace '-- 修改评论 --'
		
		if (content == null) {
			return '{"success" : "0", "message" : "亲，你还没有评论"}'
		}
		
		def helpComment = helpCommentRepository.findOne(id)
		
		helpComment.content = content
		
		helpCommentRepository.save(helpComment)
		
		'{"success" : "1"}'
		
	}
}
