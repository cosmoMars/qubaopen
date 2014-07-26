package com.qubaopen.survey.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.customer.CustomerGoldLogType;
import com.qubaopen.survey.repository.customer.CustomerGoldLogTypeRepository;

@RestController
@RequestMapping("customerGoldLogTypes")
public class CustomerGoldLogTypeController extends AbstractBaseController<CustomerGoldLogType, Long> {

	@Autowired
	private CustomerGoldLogTypeRepository customerGoldLogTypeRepository;

	@Override
	protected MyRepository<CustomerGoldLogType, Long> getRepository() {
		return customerGoldLogTypeRepository;
	}

}
