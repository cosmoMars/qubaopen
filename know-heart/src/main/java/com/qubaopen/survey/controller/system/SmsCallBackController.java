package com.qubaopen.survey.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.system.SmsCallBack;
import com.qubaopen.survey.repository.system.SmsCallBackRepository;

@RestController
@RequestMapping("SmsCallBacks")
public class SmsCallBackController extends AbstractBaseController<SmsCallBack, Long> {

	@Autowired
	private SmsCallBackRepository smsCallBackRepository;

	@Override
	protected MyRepository<SmsCallBack, Long> getRepository() {
		return smsCallBackRepository;
	}

}
