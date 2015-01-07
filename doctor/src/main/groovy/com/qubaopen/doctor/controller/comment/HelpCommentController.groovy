package com.qubaopen.doctor.controller.comment;

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
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.survey.entity.comment.Help
import com.qubaopen.survey.entity.comment.HelpComment
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo

@RestController
@RequestMapping('helpCommnet')
@SessionAttributes('currentDoctor')
public class HelpCommentController extends AbstractBaseController<HelpComment, Long> {

	private static Logger logger = LoggerFactory.getLogger(HelpCommentController.class)
	
	@Autowired
	HelpCommentRepository helpCommentRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	@Override
	MyRepository<HelpComment, Long> getRepository() {
		return helpCommentRepository
	}

	/**
	 * @param helpId
	 * @param content
	 * @param doctorId
	 * @return
	 * 医师评论
	 */
	@RequestMapping(value = 'commentHelp', method = RequestMethod.POST)
	commentHelp(@RequestParam(required = false) Long helpId,
		@RequestParam(required = false) String content,
		@ModelAttribute('currentDoctor') Doctor doctor
		) {
		
		logger.trace('-- 医师评论 --')
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
		
		if (!helpId) {
			return '{"success" : "0", "message" : "信息不完整"}'
		}
		
		def helpComment = new HelpComment(
			help : new Help(id : helpId),
			content : content,
			doctor : doctor,
			time : new Date()
		)
		helpComment = helpCommentRepository.save(helpComment)
		[
			'success' : '1',
			'commentId' : helpComment?.id
		]
	}
}
