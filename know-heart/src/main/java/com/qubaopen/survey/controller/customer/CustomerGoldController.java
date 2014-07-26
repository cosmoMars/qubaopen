package com.qubaopen.survey.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.customer.CustomerGold;
import com.qubaopen.survey.repository.customer.CustomerGoldRepository;

@RestController
@RequestMapping("customerGolds")
public class CustomerGoldController extends AbstractBaseController<CustomerGold, Long> {

	@Autowired
	private CustomerGoldRepository customerGoldRepository;

	@Override
	protected MyRepository<CustomerGold, Long> getRepository() {
		return customerGoldRepository;
	}

}
