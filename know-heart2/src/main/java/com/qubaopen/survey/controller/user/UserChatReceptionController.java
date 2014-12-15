package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserChatReception;
import com.qubaopen.survey.repository.user.UserChatReceptionRepository;

@RestController
@RequestMapping("userChatReceptions")
public class UserChatReceptionController extends AbstractBaseController<UserChatReception, Long> {

	@Autowired
	private UserChatReceptionRepository userChatReceptionRepository;

	@Override
	protected MyRepository<UserChatReception, Long> getRepository() {
		return userChatReceptionRepository;
	}

}
