package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestQuestionOrder;
import com.qubaopen.survey.repository.interest.InterestQuestionOrderRepository;

@RestController
@RequestMapping("interestQuestionOrders")
public class InterestQuestionOrderController extends AbstractBaseController<InterestQuestionOrder, Long> {

	@Autowired
	private InterestQuestionOrderRepository interestQuestionOrderRepository;

	@Override
	protected MyRepository<InterestQuestionOrder, Long> getRepository() {
		return interestQuestionOrderRepository;
	}

}
