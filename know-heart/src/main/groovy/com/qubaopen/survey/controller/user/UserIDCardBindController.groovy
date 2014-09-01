package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCardBind;
import com.qubaopen.survey.repository.user.UserIDCardBindRepository;

@RestController
@RequestMapping("userIDCardBinds")
public class UserIDCardBindController extends AbstractBaseController<UserIDCardBind, Long> {

	@Autowired
	private UserIDCardBindRepository userIDCardBindRepository;

	@Override
	protected MyRepository<UserIDCardBind, Long> getRepository() {
		return userIDCardBindRepository;
	}

}
