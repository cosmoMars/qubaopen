package com.qubaopen.survey.controller.user

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserFeedBack
import com.qubaopen.survey.entity.user.UserFeedBackType
import com.qubaopen.survey.repository.user.UserFeedBackRepository

@RestController
@RequestMapping('userFeedBacks')
@SessionAttributes('currentUser')
public class UserFeedBackController extends AbstractBaseController<UserFeedBack, Long> {

	@Autowired
	UserFeedBackRepository userFeedBackRepository

	@Override
	protected MyRepository<UserFeedBack, Long> getRepository() {
		userFeedBackRepository
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	add(@RequestBody @Valid UserFeedBack userFeedBack, BindingResult result) {
		println userFeedBack
		userFeedBackRepository.save(userFeedBack)
		'{"success": "1"}'
	}
	
	@RequestMapping(value = 'addUserFeedBack', method = RequestMethod.POST)
	addUserFeedBack(@RequestParam(required = false) String content,
		@RequestParam(required = false) String contactMethod,
		@RequestParam(required = false) Integer backType,
		@ModelAttribute('currentUser') User user) {
		
		def feedBackType
		if (backType) {
			switch (backType) {
				case 1 : feedBackType = UserFeedBack.FeedBackType.ORDINARY
				case 2 : feedBackType = UserFeedBack.FeedBackType.ENTERPRISE
			}
		} else {
			feedBackType = UserFeedBack.FeedBackType.ORDINARY
		}
	
		def userFeedBack = new UserFeedBack(
			content : content,
			feedBackTime : new Date(),
			contactMethod : contactMethod,
			user : user,
			feedBackType : feedBackType
		)
		userFeedBackRepository.save(userFeedBack)
		'{"success": "1"}'
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserFeedBack userFeedBack) {
		userFeedBackRepository.modify(userFeedBack)
		'{"success": "1"}'
	}
	
	/**
	 * @param typeId
	 * @param content
	 * @param user
	 * @return
	 * 提交意见反馈
	 */
	@RequestMapping(value = 'submitFeedBack', method = RequestMethod.POST)
	submitFeedBack(@RequestParam int idx,
		@RequestParam(required = false) String ids,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String title,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 提交意见反馈 --'
		
		def type = UserFeedBack.Type.values()[idx]
		
		def userFeedBack = new UserFeedBack(
			feedBackTime : new Date(),
			user : user,
			type : type
		)
		
		if (content != null) {
			userFeedBack.content = content
		}
		if (title !=  null) {
			userFeedBack.title = title
		}
		if (type == UserFeedBack.Type.Evaluate) {
			def strIds = ids.split(','),
				backTypes = [] as Set
				strIds.each {
					backTypes << new UserFeedBackType(id : it.trim() as long)
				}
			userFeedBack.setBackTypes(backTypes)
		}
		
		userFeedBackRepository.save(userFeedBack)
		'{"success" : "1"}'
		
	}
}
