package com.knowheart3.controller.interest

import com.knowheart3.repository.interest.InterestRepository
import com.knowheart3.repository.interest.InterestUserQuestionnaireRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.service.interest.InterestService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

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
	
	@Autowired
	UserInfoRepository userInfoRepository
	
	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Override
	protected MyRepository<Interest, Long> getRepository() {
		interestRepository
	}

	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterest', method = RequestMethod.POST)
	retrieveInterest(@RequestParam(required = false) Long interestTypeId,
			@RequestParam(required = false) Long sortTypeId,
			@RequestParam(required = false) String ids,
			@ModelAttribute('currentUser') User user,
			Pageable pageable) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		def resultIds
		if (ids != null) {
			resultIds = []
			def strIds = ids.split(',')
			strIds.each {
				resultIds << Long.valueOf(it.trim())
			}
		}

		interestService.retrieveInterest(user.id, interestTypeId, sortTypeId, resultIds, pageable)
	}
			
	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'newRetrieveInterest', method = RequestMethod.POST)
	newRetrieveInterest(@RequestParam(required = false) Long typeId,
			@RequestParam(required = false) String ids,
			@ModelAttribute('currentUser') User user,
			Pageable pageable) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		def resultIds
		if (ids != null) {
			resultIds = []
			def strIds = ids.split(',')
			strIds.each {
				resultIds << Long.valueOf(it.trim())
			}
		}

		interestService.newRetrieveInterest(user.id, typeId, resultIds, pageable)
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

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		if (!questionJson) {
			return '{"success": "0", "message": "err600"}'
		}


		def result = interestService.calculateInterestResult(user.id, interestId, questionJson)

		if (!result) {
			return '{"success" : "0", "message" : "err601"}' // 没有结果
		}
		def userInfo = userInfoRepository.findOne(user.id),
		    gradeHint = false
		if (!userInfo.evaluate) {
			def selfCount = selfUserQuestionnaireRepository.countByUser(user),
				interestCount = interestUserQuestionnaireRepository.countByUser(user)
			def allCount = selfCount + interestCount
			
			if (allCount == 10 || allCount == 30 || allCount == 100) {
				gradeHint = true
			}
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

	/**
	 * 上传兴趣问卷图片
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadPic(@RequestParam long interestId, @RequestParam(required = false) MultipartFile pic, HttpServletRequest request) {

		logger.trace(' -- 上传兴趣问卷图片 -- ')
        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
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

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
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
		
	/**
	 * 根据条件查询问卷历史
	 * @param historyId
	 * @param typeId
	 * @param user
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterestHistoryByFilter', method = RequestMethod.POST)
	retrieveInterestHistoryByFilter(@RequestParam(required = false) Long historyId,
		@RequestParam(required = false) Long typeId,
		@ModelAttribute('currentUser') User user,
		@PageableDefault(page = 0, size = 20, sort = 'id', direction = Direction.DESC)
		Pageable pageable) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		def interstUserQuestionnaires = interestService.retrieveInterestHistoryByFilter(historyId, typeId, user, pageable)
		
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
