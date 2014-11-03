package com.qubaopen.survey.controller.sms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.survey.service.SmsServiceToken

@RestController
@RequestMapping('sms189')
public class SmsController {
	
	@Autowired
	private SmsServiceToken smsServiceToken
	
	@RequestMapping(value = 'getToken', method = RequestMethod.GET)
	getToken() {

		smsServiceToken.getToken()
	}
	
	@RequestMapping(value = 'token', method = RequestMethod.GET)
	saveToken(HttpServletRequest request, HttpServletResponse response) {

		println request.toString()
		String openId = request.getParameter("open_id")
		String accessToken = request.getParameter("access_token")
		println openId
		println accessToken
//		smsServiceToken.getToken()
	}

}
