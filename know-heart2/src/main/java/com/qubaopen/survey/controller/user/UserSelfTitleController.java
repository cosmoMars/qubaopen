package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserSelfTitle;
import com.qubaopen.survey.repository.user.UserSelfTitleRepository;

@RestController
@RequestMapping("userSelfTitle")
public class UserSelfTitleController extends AbstractBaseController<UserSelfTitle, Long> {

	@Autowired
	private UserSelfTitleRepository userSelfTitleRepository;

	@Override
	protected MyRepository<UserSelfTitle, Long> getRepository() {
		return userSelfTitleRepository;
	}

}
