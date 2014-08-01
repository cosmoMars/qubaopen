package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserMessage
import com.qubaopen.survey.repository.user.UserMessageRepository

@RestController
@RequestMapping('userMessages')
public class UserMessageController extends AbstractBaseController<UserMessage, Long> {

	@Autowired
	UserMessageRepository userMessageRepository

	@Override
	protected MyRepository<UserMessage, Long> getRepository() {
		userMessageRepository
	}

}
