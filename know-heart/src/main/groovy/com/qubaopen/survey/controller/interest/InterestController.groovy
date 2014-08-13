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
import com.qubaopen.survey.entity.QuestionnaireTagType
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestType
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.QuestionnaireTagTypeRepository
import com.qubaopen.survey.repository.interest.InterestRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository
import com.qubaopen.survey.repository.user.UserFriendRepository
import com.qubaopen.survey.service.interest.InterestService

@RestController
@RequestMapping('interests')
public class InterestController extends AbstractBaseController<Interest, Long> {

	@Autowired
	InterestRepository interestRepository

	@Autowired
	InterestService interestService

	@Autowired
	UserFriendRepository userFriendRepository

	@Autowired
	QuestionnaireTagTypeRepository questionnaireTagTypeRepository

	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Override
	protected MyRepository<Interest, Long> getRepository() {
		interestRepository
	}

	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterest/{userId}', method = RequestMethod.GET)
	retrieveInterest(@PathVariable long userId) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

		def user = new User(id : userId)
		def interestList = interestRepository.findUnfinishInterest(user)

		// TODO 计算答问卷的好友数量
//		def friendCount = userFriendRepository.countUserFriend(user)
//		def friend = userFriendRepository.findByUser(user)

		def result = []
		result << ['success' : '1', 'message' : '成功']
		interestList.each {
			def tagNames = []
			it.questionnaireTagTypes.each { q ->
				tagNames << q.name
			}
			def friendCount = interestUserQuestionnaireRepository.countUserFriend(user, it)
			def interest = [
				'id' : it.id,
				'interestType' : it.interestType.name,
				'questionnaireTagType' : tagNames,
				'type' : it.type.toString(),
				'title' : it.title,
				'golds' : it.golds ?: 0,
				'status' : it.status.toString(),
				'remark' : it.remark,
				'totalRespondentsCount' : it.totalRespondentsCount ?: 0,
				'recommendedValue' : it.recommendedValue ?: 0,
				'friendCount' : friendCount
			]
			result << interest
		}
		result
	}

	/**
	 * 通过用户问题选项，计算得到结果选项
	 * @param questionOptions
	 * @return
	 */
	@RequestMapping(value = 'calculateInterestResult', method = RequestMethod.GET)
	calculateInterestResult(
		@RequestParam long userId,
		@RequestParam(required = false) long interestId,
		@RequestParam(required = false) String questionJson) {

		logger.trace ' -- 通过用户问题选项，计算得到结果选项 -- '

		if (!interestId) {
			return '{"success": "0", "message": "err1234"}'
		}
		if (!questionJson) {
			return '{"success": "0", "message": "err1234"}'
		}

		interestService.calculateInterestResult(userId, interestId, questionJson)
	}

	/**
	 * 保存问卷
	 * @param interestTypeId
	 * @param tagTypeIds
	 * @param type
	 * @param title
	 * @param golds
	 * @param status
	 * @param remark
	 * @param totalRespondentsCount
	 * @param recommendedValue
	 * @param pic
	 * @return
	 */
	@RequestMapping(value = 'addInterest', method = RequestMethod.POST, consumes = 'multipart/form-data')
	addInterest(@RequestParam long interestTypeId,
		@RequestParam(required = false) long[] tagTypeIds,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String title,
		@RequestParam(required = false) int golds,
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String remark,
		@RequestParam(required = false) int totalRespondentsCount,
		@RequestParam(required = false) int recommendedValue,
		@RequestParam(required = false) MultipartFile pic
		) {
		def interestType = new InterestType(id : interestTypeId)

		def tags = [] as Set
		if(tagTypeIds && tagTypeIds.length > 0) {
			tagTypeIds.each {
				def tag = new QuestionnaireTagType(id : it.id)
				tags.add(tag)
			}
		}
		def enumType = null
		if (type) {
			switch (type) {
				case 'DISOREDER' :
					enumType = Interest.Type.DISORDER
					break
				case 'SORCE' :
					enumType = Interest.Type.SORCE
					break
				case 'DISORDERSCORE' :
					enumType = Interest.Type.DISORDERSCORE
					break

			}
		}
		def enumStatus = null
		if (status) {
			switch (status) {
				case 'INITIAL' :
					enumStatus = Interest.Status.INITIAL
					break
				case 'ONLINE' :
					enumStatus = Interest.Status.ONLINE
					break
				case 'CLOSED' :
					enumStatus = Interest.Status.CLOSED
					break
			}
		}

		def interest = new Interest(
			interestType : interestType,
			questionnaireTagTypes : tags ?: null,
			type : enumType ?: null,
			title : title ?: null,
			golds : golds,
			status : enumStatus ?: null,
			remark : remark,
			totalRespondentsCount : totalRespondentsCount,
			recommendedValue : recommendedValue,
			pic : pic.bytes
		)

		interestRepository.save(interest)
	}

	/**
	 * 上传图片
	 * @param interestId
	 * @param pic
	 */
	@RequestMapping(value = 'uploadPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadPic(@RequestParam long interestId, @RequestParam(required = false) MultipartFile pic) {

		logger.trace(' -- 上传图片 -- ')

		def interest = interestRepository.findOne(interestId)
		interest.pic = pic.bytes
		try {
			interestRepository.save(interest)
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
	@RequestMapping(value = 'retrievePic/{interestId}', method = RequestMethod.GET)
	retrieveAvatar(@PathVariable long interestId, OutputStream output) {

		logger.trace(' -- 显示图片 -- ')

		def interest = interestRepository.findOne(interestId)
		output.write(interest.pic)
	}

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

}
