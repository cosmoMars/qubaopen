package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserChatSend;
import com.qubaopen.survey.repository.user.UserChatSendRepository;

@RestController
@RequestMapping("userChatSends")
public class UserChatSendController extends AbstractBaseController<UserChatSend, Long> {

	@Autowired
	private UserChatSendRepository userChatSendRepository;

	@Override
	protected MyRepository<UserChatSend, Long> getRepository() {
		return userChatSendRepository;
	}

}
