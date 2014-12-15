package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserWithdrawBankType;
import com.qubaopen.survey.repository.user.UserWithdrawBankTypeRepository;

@RestController
@RequestMapping("userWithdrawBankTypes")
public class UserWithdrawBankTypeController extends AbstractBaseController<UserWithdrawBankType, Long> {

	@Autowired
	private UserWithdrawBankTypeRepository userWithdrawBankTypeRepository;

	@Override
	protected MyRepository<UserWithdrawBankType, Long> getRepository() {
		return userWithdrawBankTypeRepository;
	}

}
