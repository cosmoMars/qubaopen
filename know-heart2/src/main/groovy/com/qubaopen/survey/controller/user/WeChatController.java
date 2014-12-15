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
@RequestMapping("weChat")
public class WeChatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "weChatLogin", method = RequestMethod.GET)
	public Map<String, Object> weChatLogin() {
		LOGGER.trace(" -- wechatlogin -- ");

//		LOGGER.trace("sms189_url := {}", sms189_url);
//		LOGGER.trace("sms189_app_id := {}", sms189_app_id);
//		LOGGER.trace("sms189_app_secret := {}", sms189_app_secret);
//		LOGGER.trace("sms189_access_token := {}", sms189_access_token);
//		LOGGER.trace("sms189_template_id := {}", sms189_template_id);
//		LOGGER.trace("sms189_template_param := {}", sms189_template_param);

		
//		https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		StringBuilder builder = new StringBuilder();
		builder.append("app_id=").append("wxa46025ba7c946c08").append('&').append("secret=").append("446ad95448845342b0fa51c2ddcaa1cf")
			.append('&').append("code=").append("CODE").append('&')
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
