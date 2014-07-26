package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserBankCard;
import com.qubaopen.survey.repository.user.UserBankCardRepository;

@RestController
@RequestMapping("userBankCards")
public class UserBankCardController extends AbstractBaseController<UserBankCard, Long> {

	@Autowired
	private UserBankCardRepository userBankCardRepository;

	@Override
	protected MyRepository<UserBankCard, Long> getRepository() {
		return userBankCardRepository;
	}

}
