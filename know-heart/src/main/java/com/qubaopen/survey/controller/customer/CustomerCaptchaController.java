package com.qubaopen.survey.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.customer.CustomerCaptcha;
import com.qubaopen.survey.repository.customer.CustomerCaptchaRepository;

@RestController
@RequestMapping("customerCaptchas")
public class CustomerCaptchaController extends AbstractBaseController<CustomerCaptcha, Long> {

	@Autowired
	private CustomerCaptchaRepository customerCaptchaRepository;

	@Override
	protected MyRepository<CustomerCaptcha, Long> getRepository() {
		return customerCaptchaRepository;
	}

}
