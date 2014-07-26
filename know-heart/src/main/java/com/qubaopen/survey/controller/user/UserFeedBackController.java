package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserFeedBack;
import com.qubaopen.survey.repository.user.UserFeedBackRepository;

@RestController
@RequestMapping("userFeedBacks")
public class UserFeedBackController extends AbstractBaseController<UserFeedBack, Long> {

	@Autowired
	private UserFeedBackRepository userFeedBackRepository;

	@Override
	protected MyRepository<UserFeedBack, Long> getRepository() {
		return userFeedBackRepository;
	}

}
