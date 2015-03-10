package com.knowheart3.controller.interest

import com.knowheart3.repository.interest.InterestQuestionRepository
import com.knowheart3.service.interest.InterestService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.InterestQuestion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('interestQuestions')
public class InterestQuestionController extends AbstractBaseController<InterestQuestion, Long> {

	@Autowired
	InterestQuestionRepository interestQuestionRepository

	@Autowired
	InterestService interestService

	@Override
	protected MyRepository<InterestQuestion, Long> getRepository() {
		interestQuestionRepository
	}

	/**
	 * 通过问卷查询问卷问题，问题顺序，特殊问题插入
	 * @param interestId
	 * @return
	 */
	@RequestMapping(value = 'findByInterest/{interestId}', method = RequestMethod.GET)
	findByInterest(@PathVariable long interestId) {

		logger.trace ' -- 通过问卷查询问卷问题，问题顺序，特殊问题插入 -- '

		interestService.findByInterest(interestId)

	}

//	/**
//	 * 上传图片
//	 * @param questionId
//	 * @param pic
//	 */
//	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
//	uploadPic(@RequestParam long questionId, @RequestParam(required = false) MultipartFile pic) {
//
//		logger.trace(' -- 上传图片 -- ')
//
//		def question = interestQuestionRepository.findOne(questionId)
//		question.pic = pic.bytes
//		try {
//			interestQuestionRepository.save(question)
//			'{"success": "1"}'
//		} catch (Exception e) {
//			'{"success": "0", "message": "上传失败"}'
//		}
//
//	}
//
//	/**
//	 * 显示图片
//	 * @param interestId
//	 * @param output
//	 * @return
//	 */
//	@RequestMapping(value = 'retrievePic/{questionId}', method = RequestMethod.GET)
//	retrieveAvatar(@PathVariable long questionId, OutputStream output) {
//
//		logger.trace(' -- 显示图片 -- ')
//
//		def question = interestQuestionRepository.findOne(questionId)
//		output.write(question.pic)
//	}

}
