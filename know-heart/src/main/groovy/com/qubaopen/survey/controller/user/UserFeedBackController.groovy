package com.qubaopen.survey.controller.user

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserFeedBack
import com.qubaopen.survey.repository.user.UserFeedBackRepository

@RestController
@RequestMapping('userFeedBacks')
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
		userFeedBackRepository.save(userFeedBack)
		'{"success": "1"}'
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserFeedBack userFeedBack) {
		userFeedBackRepository.modify(userFeedBack)
		'{"success": "1"}'
	}
}
