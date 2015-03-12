package com.knowheart3.controller.self

import com.knowheart3.controller.FileUtils
import com.knowheart3.repository.favorite.UserFavoriteRepository
import com.knowheart3.repository.interest.InterestUserQuestionnaireRepository
import com.knowheart3.repository.self.SelfGroupRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.service.self.SelfService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping('selfs')
@SessionAttributes('currentUser')
public class SelfController extends AbstractBaseController<Self, Long> {

	@Autowired
	SelfRepository selfRepository

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfService selfService

	@Autowired
	FileUtils fileUtils
	
	@Autowired
	SelfGroupRepository selfGroupRepository
	
	@Autowired
	UserInfoRepository userInfoRepository
	
	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

    @Autowired
    UserFavoriteRepository userFavoriteRepository

	@Override
	protected MyRepository<Self, Long> getRepository() {
		selfRepository
	}

/**
 * @param id
 * @param user
 * @return
 * 获取自测信息
 */
    @RequestMapping(value = 'retrieveSelfContent', method = RequestMethod.POST)
    retrieveSelfContent(@RequestParam(required = false) Long id,
        @ModelAttribute('currentUser') User user) {

        def favorite
        def self = selfRepository.findOne(id)
        if (null != user.id) {
            favorite = userFavoriteRepository.findOneByFilters([
                user_equal : user,
                self_equal : self
            ])
        }

        def isFavorite = false
        if (favorite != null) {
            isFavorite = true
        }

        [
            'success' : '1',
            'id' : self.id,
            'name' : self?.title,
            'guidanceSentence' : self?.guidanceSentence,
            'tips' : self?.tips,
            'content' : self?.remark,
            'isFavorite' : isFavorite
        ]
    }

	/**
	 * 获取用户自测问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelf', method = RequestMethod.GET)
	retrieveSelf(@RequestParam(required = false) Boolean refresh, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取用户自测问卷 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		if (!refresh) {
			refresh = false
		}
		def resultSelfs = selfService.retrieveSelf(user, refresh),
		data = []

		resultSelfs.each {
			def self = [
				'selfId' : it?.id,
				'managementType' : it?.selfManagementType?.id,
				'title' : it?.title,
				'version' : it?.version
			]
			data << self
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}

	/**
	 * 获取问卷文字信息
	 * @param selfId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelfScript/{selfId}', method = RequestMethod.GET)
	retrieveSelfScript(@PathVariable long selfId) {
		def self = selfRepository.findOne(selfId)

		[
			'success' : '1',
			'message' : '成功',
			'title' : self?.title,
			'guidanceSentence' : self?.guidanceSentence,
			'tips' : self?.tips,
			'remark' : self?.remark
		]
	}

	/**
	 * 计算自测结果选项
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @return
	 */
	@RequestMapping(value = 'calculateSelfResult', method = RequestMethod.POST)
	calculateSelfReslut(@RequestParam(required = false) long selfId,
			@RequestParam(required = false) String questionJson,
			@RequestParam(required = false) Boolean refresh,
			@ModelAttribute('currentUser') User user
	) {

		logger.trace ' -- 计算自测结果选项 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

		if (StringUtils.isEmpty(questionJson)) {
			return '{"success" : "0", "message" : "err600"}'
		}

		if (refresh == null) {
			refresh = true
		}

		def result = selfService.calculateSelfReslut(user.id, selfId, questionJson, refresh)

		if (result instanceof String) {
			return result
		}

		if (!result) {
			return '{"success" : "0", "message" : "err601"}'
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
			'id' : result?.id,
			'resultTitle' : result?.selfResult?.title,
			'content' : result?.content,
			'optionTitle' : result?.title,
			'resultRemark' : result?.selfResult?.remark,
			'optionNum' : result?.resultNum,
			'gradeHint' : gradeHint
		]
	}

	/**
	 * 上传图片
	 * @param pic
	 * @param selfId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = 'uploadSelfPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadAvatar(@RequestParam(required = false) MultipartFile pic, @RequestParam long selfId, HttpServletRequest request) {

		logger.trace(' -- 上传头像 -- ')
		def self = selfRepository.findOne(selfId)

		if (pic) {

			def filename = "${selfId}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
			picPath = "${request.getServletContext().getRealPath('/')}pic/$filename"

			fileUtils.saveFile(pic.bytes, picPath)

			self.picPath = "/pic/$filename"
			selfRepository.save(self)
			return '{"success": "1"}'
		}
		'{"success": "0", "message" : "err102"}'
	}

	/**
	 * 获取自测问卷
	 * @param typeId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelfByType/{typeId}', method = RequestMethod.GET)
	retrieveSelfByType(@PathVariable Long typeId, @ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		/*def selfs = selfRepository.findAll(
			[
				'selfManagementType.id_equal' : typeId,
				'status_equal' : Self.Status.ONLINE
			]
		)
		
		def data = [],
			specialSelf = selfRepository.findByMaxRecommendedValue()
			
		def selfs = selfService.retrieveSelfByTypeId(user, typeId)
		selfs.each {
			if (it.id != specialSelf.id) {
				data <<	[
					'selfId' : it?.id,
					'managementType' : it?.selfManagementType?.id,
					'title' : it?.title
				]
			}
		}*/
		
		def specialSelf = selfRepository.findByMaxRecommendedValue()
		
		def todayQuestionnaires = selfUserQuestionnaireRepository.findByTimeAndTypeIdWithOutSpecial(DateFormatUtils.format(new Date(), 'yyyy-MM-dd'), typeId, specialSelf, user)
		
//		if (todayQuestionnaires?.size() >= 2) {
//			return '{"success" : "0", "message" : "亲，该类别每日2题上线你已经答满咯～"}' // err603
//		}
		
		def data = [],
			remainCount = 2 - todayQuestionnaires?.size() ?: 0
		def selfs = selfService.retrieveSelfByTypeId(user, typeId, specialSelf)
		selfs.each {
			data <<	[
				'selfId' : it?.id,
				'managementType' : it?.selfManagementType?.id,
				'title' : it?.title,
				'version' : it?.version
			]
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data,
			'remainCount' : remainCount
		]
	}

	@RequestMapping(value = 'findSelfUserQuestionnaire', method = RequestMethod.GET)
	findSelfUserQuestionnaire(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		selfUserQuestionnaireRepository.findRecentQuestionnarie(user)
	}



	/**
	 * 更新性格解析度
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'refreshAnalysis', method = RequestMethod.GET)
	refreshAnalysis(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		selfService.calcUserAnalysisRadio(user);		
	}
	
	
	/**
	 * 更新心理指数
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'refreshMentalStatus', method = RequestMethod.GET)
	refreshMentalStatus(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		selfService.calcUserMentalStatus(user);
	}
	
	/**
	 * 获取自测问卷组问卷
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelfByGroupId/{groupId}', method = RequestMethod.GET)
	retrieveSelfByGroupId(@PathVariable Long groupId) {
		
		logger.trace ' -- 查询自测组问卷 -- '
		
		def selfGroup = selfGroupRepository.findOne(groupId),
			data = []
		selfGroup.selfs.each {
			def self = [
				'selfId' : it?.id,
				'managementType' : it?.selfManagementType?.id,
				'title' : it?.title
			]
			data <<	self
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}
}
