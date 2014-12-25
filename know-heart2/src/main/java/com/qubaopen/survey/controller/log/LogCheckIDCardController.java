package com.qubaopen.survey.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.log.LogCheckIDCard;
import com.qubaopen.survey.repository.log.LogCheckIDCardRepository;

@RestController
@RequestMapping("logCheckIDCards")
public class LogCheckIDCardController extends AbstractBaseController<LogCheckIDCard, Long> {

	@Autowired
	private LogCheckIDCardRepository logCheckIDCardRepository;
	
	@Override
	protected MyRepository<LogCheckIDCard, Long> getRepository() {
		return logCheckIDCardRepository;
	}

}
