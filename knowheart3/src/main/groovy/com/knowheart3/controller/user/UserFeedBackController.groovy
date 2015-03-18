package com.knowheart3.controller.user

import com.knowheart3.service.CaptchaService

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.user.UserFeedBackRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserFeedBack
import com.qubaopen.survey.entity.user.UserFeedBackType

@RestController
@RequestMapping('userFeedBacks')
@SessionAttributes('currentUser')
public class UserFeedBackController extends AbstractBaseController<UserFeedBack, Long> {

	@Autowired
	UserFeedBackRepository userFeedBackRepository

    @Autowired
    CaptchaService captchaService

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
			feedBackType : feedBackType
		)
        if (null != null) {
            userFeedBack.user = user
        }
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
	@Transactional
	@RequestMapping(value = 'submitFeedBack', method = RequestMethod.POST)
	submitFeedBack(@RequestParam int idx,
		@RequestParam(required = false) String ids,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String title,
        @RequestParam(required = false) String contactMethod,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 提交意见反馈 --'
		
		def type = UserFeedBack.Type.values()[idx]
		
		def userFeedBack = new UserFeedBack(
			feedBackTime : new Date(),
			type : type
		)
        if (null != user) {
            userFeedBack.user = user
        }
		
		if (content != null) {
			userFeedBack.content = content
		}
		if (title !=  null) {
			userFeedBack.title = title
		}
        if (contactMethod != null) {
            userFeedBack.contactMethod = contactMethod
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

    /**
     *
     * @param area
     * @param content
     * @param contactMethod
     * @return
     * 提交没有医师反馈
     */
    @RequestMapping(value = 'submitNoDoctor', method = RequestMethod.POST)
    submitNoDoctor(@RequestParam(required = false) String area,
        @RequestParam(required = false) String content,
        @RequestParam(required = false) String contactMethod) {

        def message,
            feedback = new UserFeedBack()
        if (null != area) {
            feedback.title = area
            message += (area + " ： ")
        }
        if (null != content) {
            message += content
            feedback.content = message
        }
        if (null != contactMethod) {
            feedback.contactMethod = contactMethod
        }
//        captchaService.sendTextMail(message, contactMethod)
        userFeedBackRepository.save(feedback)
        '{"success" : "1"}'
    }
}
