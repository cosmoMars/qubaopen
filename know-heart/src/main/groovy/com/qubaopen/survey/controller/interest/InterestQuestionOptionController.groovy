package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.InterestQuestionOption
import com.qubaopen.survey.repository.interest.InterestQuestionOptionRepository

@RestController
@RequestMapping('interestQuestionOptions')
public class InterestQuestionOptionController extends AbstractBaseController<InterestQuestionOption, Long> {

	@Autowired
	InterestQuestionOptionRepository interestQuestionOptionRepository

	@Override
	protected MyRepository<InterestQuestionOption, Long> getRepository() {
		interestQuestionOptionRepository
	}

	/**
	 * 上传图片
	 * @param questionId
	 * @param pic
	 */
	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadPic(@RequestParam long optionId, @RequestParam(required = false) MultipartFile pic) {

		logger.trace(' -- 上传图片 -- ')

		def option = interestQuestionOptionRepository.findOne(optionId)
		option.pic = pic.bytes
		try {
			interestQuestionOptionRepository.save(option)
			'{"success": "1"}'
		} catch (Exception e) {
			'{"success": "0", "message": "上传失败"}'
		}

	}

	/**
	 * 显示图片
	 * @param interestId
	 * @param output
	 * @return
	 */
	@RequestMapping(value = 'retrievePic/{optionId}', method = RequestMethod.GET)
	retrieveAvatar(@PathVariable long optionId, OutputStream output) {

		logger.trace(' -- 显示图片 -- ')

		def option = interestQuestionOptionRepository.findOne(optionId)
		output.write(option.pic)
	}

}
