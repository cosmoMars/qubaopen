package com.qubaopen.survey.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.customer.Customer;
import com.qubaopen.survey.repository.customer.CustomerRepository;

@RestController
@RequestMapping("customers")
public class CustomerController extends AbstractBaseController<Customer, Long> {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected MyRepository<Customer, Long> getRepository() {
		return customerRepository;
	}

}
