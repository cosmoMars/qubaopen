package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCard;
import com.qubaopen.survey.repository.user.UserIDCardRepository;

@RestController
@RequestMapping("userIDCards")
public class UserIDCardController extends AbstractBaseController<UserIDCard, Long> {

	@Autowired
	private UserIDCardRepository userIDCardRepository;

	@Override
	protected MyRepository<UserIDCard, Long> getRepository() {
		return userIDCardRepository;
	}

}
