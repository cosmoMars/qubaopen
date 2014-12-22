package com.qubaopen.doctor.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
//import org.apache.log4j.net.SMTPAppender;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

	@SuppressWarnings("unused")
	private String sandCaptcha(String email, String captcha) {
//		Logger logger = Logger.getLogger(CaptchaService.class);
//
//		SMTPAppender appender = new SMTPAppender();
//		try {
//			appender.setSMTPUsername("mars.liu@qudiaoyan.com");
//			appender.setSMTPPassword("asdfqwer12");
//			appender.setTo(email);
//			appender.setFrom("mars.liu@qudiaoyan.com");
//			appender.setSMTPHost("smtp.mxhichina.com");
//			appender.setLocationInfo(true);
//			appender.setSubject("知心团队 验证码");
//			appender.setLayout(new PatternLayout());
//			appender.activateOptions();
//			logger.addAppender(appender);
//			logger.error("验证码：" + captcha);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return "1";
	}

}
