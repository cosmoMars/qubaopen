package com.qubaopen.survey.controller.user;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserIDCardBind
import com.qubaopen.survey.entity.user.UserIDCardLog;
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserIDCardLogRepository;
import com.qubaopen.survey.repository.user.UserIDCardRepository
import com.qubaopen.survey.service.IdentityValidationService
import com.qubaopen.survey.service.user.UserIDCardBindService;
import static com.qubaopen.survey.utils.ValidateUtil.*

@RestController
@RequestMapping('userIDCardBinds')
@SessionAttributes('currentUser')
public class UserIDCardBindController extends AbstractBaseController<UserIDCardBind, Long> {

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository
	
	@Autowired
	UserIDCardBindService userIDCardBindService
	
	@Override
	protected MyRepository<UserIDCardBind, Long> getRepository() {
		return userIDCardBindRepository;
	}

	/**
	 * 身份证认证
	 * @param idCard
	 * @param name
	 * @return
	 * 
	 * 错误次数每月最多3次，每个月只能修改1次
	 */
	@RequestMapping(value = 'submitUserIdCard', method = RequestMethod.POST)
	submitUserIdCard(@RequestParam String idCard, @RequestParam String name, @ModelAttribute('currentUser') User user) {
		
		logger.trace ' -- 身份证认证 -- '
		if (!isIdCard(idCard)) {
			return '{"success" : "0", "message" : "err100002"}'
		}
		
		userIDCardBindService.submitUserIdCard(idCard, name, user)
		
	}
}
