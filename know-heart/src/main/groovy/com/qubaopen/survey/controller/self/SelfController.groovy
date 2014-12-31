package com.qubaopen.survey.controller.self

import java.net.Authenticator.RequestorType;

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.controller.FileUtils;
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.self.SelfGroupRepository;
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository
import com.qubaopen.survey.service.self.SelfService

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

	@Override
	protected MyRepository<Self, Long> getRepository() {
		selfRepository
	}

	/**
	 * 获取用户自测问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelf', method = RequestMethod.GET)
	retrieveSelf(@RequestParam(required = false) Boolean refresh, @ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取用户自测问卷 -- '

		if (!refresh) {
			refresh = false
		}
		def resultSelfs = selfService.retrieveSelf(user, refresh),
		data = []

		resultSelfs.each {
			def self = [
				'selfId' : it?.id,
				'managementType' : it?.selfManagementType?.id,
				'title' : it?.title
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
		[
			'success' : '1',
			'message' : '成功',
			'id' : result?.id,
			'resultTitle' : result?.selfResult?.title,
			'content' : result?.content,
			'optionTitle' : result?.title,
			'resultRemark' : result?.selfResult?.remark,
			'optionNum' : result?.resultNum
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
	retrieveSelfByType(@PathVariable Long typeId) {
		def selfs = selfRepository.findAll(
			[
				'selfManagementType.id_equal' : typeId,
				'status_equal' : Self.Status.ONLINE
			]
		)
		def data = [],
			specialSelf = selfRepository.findByMaxRecommendedValue()
		selfs.each {
			if (it.id != specialSelf.id) {
				data <<	[
					'selfId' : it?.id,
					'managementType' : it?.selfManagementType?.id,
					'title' : it?.title
				]
			}
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}

	@RequestMapping(value = 'findSelfUserQuestionnaire', method = RequestMethod.GET)
	findSelfUserQuestionnaire(@ModelAttribute('currentUser') User user) {
		
		selfUserQuestionnaireRepository.findRecentQuestionnarie(user)
	}



	/**
	 * 更新性格解析度
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'refreshAnalysis', method = RequestMethod.GET)
	refreshAnalysis(@ModelAttribute('currentUser') User user) {
		selfService.calcUserAnalysisRadio(user);		
	}
	
	
	/**
	 * 更新心理指数
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'refreshMentalStatus', method = RequestMethod.GET)
	refreshMentalStatus(@ModelAttribute('currentUser') User user) {
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
