package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserQuota;
import com.qubaopen.survey.repository.user.UserQuotaRepository;

@RestController
@RequestMapping("userQuotas")
public class UserQuotaController extends AbstractBaseController<UserQuota, Long> {

	@Autowired
	private UserQuotaRepository userQuotaRepository;

	@Override
	protected MyRepository<UserQuota, Long> getRepository() {
		return userQuotaRepository;
	}

}
