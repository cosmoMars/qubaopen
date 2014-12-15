package com.qubaopen.survey.controller.user;

import groovy.json.JsonSlurper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("qZone")
public class QzoneController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QzoneController.class);

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "qZoneLogin", method = RequestMethod.GET)
	public Map<String, Object> qZoneLogin() {
		LOGGER.trace(" -- qzonelogin -- ");
		
		StringBuilder builder = new StringBuilder();
		builder.append("response_type=").append("code").append('&').append("client_id=").append("1103477380")
			.append('&').append("redirect_uri=").append("CODE").append('&')
			.append("grant_type=authorization_code");

		HttpEntity<String> request = new HttpEntity<String>(builder.toString());
		String result = restTemplate.postForObject("https://api.weixin.qq.com/sns/oauth2/access_token", request, String.class);

		@SuppressWarnings("unchecked")
		Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(result);

		System.out.println(json);
		
		String accessToken = String.valueOf(json.get("access_token"));
		
		System.out.println(accessToken);
		System.out.println(String.valueOf(json.get("expires_in")));
		System.out.println(String.valueOf(json.get("refresh_token")));
		System.out.println(String.valueOf(json.get("openid")));
		System.out.println(String.valueOf(json.get("scope")));
		
		Map<String, Object> map = new HashMap<String, Object>();
		return map;
	}
}
