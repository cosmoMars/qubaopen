package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserLogType;
import com.qubaopen.survey.repository.user.UserLogTypeRepository;

@RestController
@RequestMapping("userLogTypes")
public class UserLogTypeController extends AbstractBaseController<UserLogType, Long> {

	@Autowired
	private UserLogTypeRepository userLogTypeRepository;

	@Override
	protected MyRepository<UserLogType, Long> getRepository() {
		return userLogTypeRepository;
	}

}
