package com.qubaopen.survey.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.customer.CustomerGoldLog;
import com.qubaopen.survey.repository.customer.CustomerGoldLogRepository;

@RestController
@RequestMapping("customerGoldLogs")
public class CustomerGoldLogController extends AbstractBaseController<CustomerGoldLog, Long> {

	@Autowired
	private CustomerGoldLogRepository customerGoldLogRepository;

	@Override
	protected MyRepository<CustomerGoldLog, Long> getRepository() {
		return customerGoldLogRepository;
	}

}
