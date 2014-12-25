package com.qubaopen.survey.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.log.LogCaptcha;
import com.qubaopen.survey.repository.log.LogCaptchaRepository;

@RestController
@RequestMapping("logCaptchas")
public class LogCaptchaController extends AbstractBaseController<LogCaptcha, Long> {

	@Autowired
	private LogCaptchaRepository logCaptchaRepository;

	@Override
	protected MyRepository<LogCaptcha, Long> getRepository() {
		return logCaptchaRepository;
	}

}
