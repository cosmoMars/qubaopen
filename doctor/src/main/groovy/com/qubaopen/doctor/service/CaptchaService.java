package com.qubaopen.doctor.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.net.SMTPAppender;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

	@SuppressWarnings("unused")
	private String sandCaptcha(String captcha) {
		Logger logger = Logger.getLogger(CaptchaService.class);

		SMTPAppender appender = new SMTPAppender();
		try {
			appender.setSMTPUsername("username");
			appender.setSMTPPassword("password");
			appender.setTo("xxx@xxx.com");
			appender.setFrom("yyy@yyy.com");
			appender.setSMTPHost("smtp.163.com");
			appender.setLocationInfo(true);
			appender.setSubject("captcha");
			appender.setLayout(new PatternLayout());
			appender.activateOptions();
			logger.addAppender(appender);
			logger.error(captcha);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Printing ERROR Statements", e);
		}
		return captcha;
	}

}
