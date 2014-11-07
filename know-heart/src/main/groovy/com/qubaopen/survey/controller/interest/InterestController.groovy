package com.qubaopen.survey.controller.interest

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.controller.FileUtils
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.interest.InterestRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository
import com.qubaopen.survey.service.interest.InterestService

@RestController
@RequestMapping('interests')
@SessionAttributes('currentUser')
public class InterestController extends AbstractBaseController<Interest, Long> {

	@Autowired
	InterestRepository interestRepository

	@Autowired
	InterestService interestService
	
	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository
	
	@Autowired
	FileUtils fileUtils

	@Override
	protected MyRepository<Interest, Long> getRepository() {
		interestRepository
	}

	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterest2', method = RequestMethod.POST)
	retrieveInterest2(@RequestParam(required = false) Long interestTypeId,
			@RequestParam(required = false) Long sortTypeId,
			@RequestParam(required = false) String ids,
			@ModelAttribute('currentUser') User user,
			Pageable pageable) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

		def resultIds
		if (ids != null) {
			resultIds = []
			def strIds = ids.split(',')
			strIds.each {
				resultIds << Long.valueOf(it.trim())
			}
		}

		interestService.retrieveInterest2(user.id, interestTypeId, sortTypeId, resultIds, pageable)
	}
			
	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterest', method = RequestMethod.POST)
	retrieveInterest(@RequestParam(required = false) Long typeId,
			@RequestParam(required = false) String ids,
			@ModelAttribute('currentUser') User user,
			Pageable pageable) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

		def resultIds
		if (ids != null) {
			resultIds = []
			def strIds = ids.split(',')
			strIds.each {
				resultIds << Long.valueOf(it.trim())
			}
		}

		interestService.retrieveInterest(user.id, typeId, resultIds, pageable)
	}

	/**
	 * 通过用户问题选项，计算得到结果选项
	 * @param questionOptions
	 * @return
	 */
	@RequestMapping(value = 'calculateInterestResult', method = RequestMethod.POST)
	calculateInterestResult(
			@RequestParam long interestId,
			@RequestParam(required = false) String questionJson,
			@ModelAttribute('currentUser') User user
	) {

		logger.trace ' -- 通过用户问题选项，计算得到结果选项 -- '
		if (!questionJson) {
			return '{"success": "0", "message": "err600"}'
		}


		def result = interestService.calculateInterestResult(user.id, interestId, questionJson)

		if (!result) {
			return '{"success" : "0", "message" : "err601"}' // 没有结果
		}
		[
			'success' : '1',
			'message' : '成功',
			'id' : result.id ?: '',
			'resultTitle' : result?.interestResult?.title ?: '',
			'content' : result.content ?: '',
			'optionTitle' : result.title ?: '',
			'resultNum' : result.resultNum ?: ''
		]
	}


	//	/**
	//	 * 上传图片
	//	 * @param interestId
	//	 * @param pic
	//	 */
	//	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	//	uploadPic(@RequestParam long interestId, @RequestParam(required = false) MultipartFile pic) {
	//
	//		logger.trace(' -- 上传图片 -- ')
	//
	//		def interest = interestRepository.findOne(interestId)
	//		interest.pic = pic.bytes
	//		try {
	//			interestRepository.save(interest)
	//			'{"success": "1"}'
	//		} catch (Exception e) {
	//			'{"success": "0", "message": "上传失败"}'
	//		}
	//
	//	}
	
	/**
	 * 上传兴趣问卷图片
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadPic(@RequestParam long interestId, @RequestParam(required = false) MultipartFile pic, HttpServletRequest request) {

		logger.trace(' -- 上传兴趣问卷图片 -- ')
		
		def interest = interestRepository.findOne(interestId)

		if (interest) {

			def filename = "i_${interestId}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
				interestPath = "${request.getServletContext().getRealPath('/')}pic/$filename"

			fileUtils.saveFile(pic.bytes, interestPath)

			interest.picPath = "/pic/$filename"
			interestRepository.save(interest)
			return '{"success": "1"}'
		}
		'{"success": "0", "message" : "err111"}'
	}
	
	//	/**
	//	 * 显示图片
	//	 * @param interestId
	//	 * @param output
	//	 * @return
	//	 */
	//	@RequestMapping(value = 'retrievePic/{interestId}', method = RequestMethod.GET)
	//	retrieveAvatar(@PathVariable long interestId, OutputStream output) {
	//
	//		logger.trace(' -- 显示图片 -- ')
	//
	//		def interest = interestRepository.findOne(interestId)
	//		output.write(interest.pic)
	//	}

	/**
	 * 获取好友问卷结果
	 * @param friendId
	 * @param interestId
	 * @return
	 */
	@RequestMapping(value = 'retrieveFriendAnswer', method = RequestMethod.GET)
	retrieveFriendAnswer(@RequestParam long friendId, @RequestParam long interestId) {

		logger.trace(' -- 获取好友问卷结果 -- ')

		interestService.retrieveFriendAnswer(friendId, interestId)

	}
	
	/**
	 * 获取用户兴趣测试答题纪录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterestHistory', method = RequestMethod.POST)
	retrieveInterestHistory(@RequestParam(required = false) Long typeId,
		@RequestParam(required = false) String ids,
		@ModelAttribute('currentUser') User user,
		Pageable pageable) {
		
		def interstUserQuestionnaires = []
		if (!typeId && StringUtils.isEmpty(ids)) {
			interstUserQuestionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user
				],
				pageable
			)
		}
		if (typeId && StringUtils.isEmpty(ids)) {
			interstUserQuestionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user,
					'interest.interestType.id_equal' : typeId
				],
				pageable
			)
		}
		if (!typeId && !StringUtils.isEmpty(ids)) {
			def resultIds = []
			if (ids != null) {
				resultIds = []
				def strIds = ids.split(',')
				strIds.each {
					resultIds << Long.valueOf(it.trim())
				}
			}
			interstUserQuestionnaires = interestUserQuestionnaireRepository.findQuestionnaireByFilter(user, resultIds, pageable)
		}
		if (typeId && !StringUtils.isEmpty(ids)) {
			def resultIds = []
			if (ids != null) {
				resultIds = []
				def strIds = ids.split(',')
				strIds.each {
					resultIds << Long.valueOf(it.trim())
				}
			}
			interstUserQuestionnaires = interestUserQuestionnaireRepository.findQuestionnaireByFilter(user, typeId, resultIds, pageable)
		}
		
		def data = []
		interstUserQuestionnaires.each {
			data << [
				'interestId' : it?.interest?.id,
				'interestTitle' : it?.interest?.title,
				'resultId' : it?.interestResultOption?.id,
				'resultTitle' : it?.interestResultOption?.interestResult?.title,
				'content' : it?.interestResultOption?.content,
				'optionTitle' : it?.interestResultOption?.title,
				'resultNum' : it?.interestResultOption?.resultNum
			]
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}
		
	@RequestMapping(value = 'retrieveInterestHistoryByFilter', method = RequestMethod.POST)
	retrieveInterestHistoryById(@RequestParam(required = false) Long historyId,
		@RequestParam(required = false) Long typeId,
		@ModelAttribute('currentUser') User user,
		@PageableDefault(page = 0, size = 20, sort = 'id', direction = Direction.DESC)
		Pageable pageable) {
		
		println pageable
		
		def interstUserQuestionnaires = interestService.retrieveInterestHistoryById(historyId, typeId, user, pageable)
		
		def data = []
		interstUserQuestionnaires.each {
			data << [
				'historyId' : it?.id,
				'date' : it?.time,
				'interestId' : it?.interest?.id,
				'interestTitle' : it?.interest?.title,
				'interestType' : it?.interest?.interestType?.id,
				'resultId' : it?.interestResultOption?.id,
				'resultTitle' : it?.interestResultOption?.interestResult?.title,
				'content' : it?.interestResultOption?.content,
				'optionTitle' : it?.interestResultOption?.title,
				'resultNum' : it?.interestResultOption?.resultNum
			]
		}
		def more = true
		if (data.size() < 20) {
			more = false
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data,
			'more' : more
		]
	}
	
}
