package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionOrder;
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository;

@RestController
@RequestMapping("selfQuestionOrders")
public class SelfQuestionOrderController extends AbstractBaseController<SelfQuestionOrder, Long> {

	@Autowired
	private SelfQuestionOrderRepository selfQuestionOrderRepository;

	@Override
	protected MyRepository<SelfQuestionOrder, Long> getRepository() {
		return selfQuestionOrderRepository;
	}

}
