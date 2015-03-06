package com.knowheart3.service;

import groovy.json.JsonSlurper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsServiceToken {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceToken.class);

	@Autowired
	private RestTemplate restTemplate;

	public Map<String, Object> getToken() {

		StringBuilder builder = new StringBuilder();
		
		
		builder.append("app_id=404743360000037976").append("&")
			.append("app_secret=d0e50def25c6fe94c56d697e1dc328bd").append("&")
			.append("redirect_uri=").append("&")
			.append("response_type=token");
		
		System.out.println(builder.toString());
		LOGGER.trace("builder := {}", builder.toString());

		HttpEntity<String> request = new HttpEntity<String>(builder.toString());
		String result = restTemplate.postForObject("https://oauth.api.189.cn/emp/oauth2/v3/authorize", request, String.class);

		@SuppressWarnings("unchecked")
		Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(result);
		
		System.out.println(json.toString());
		String token = String.valueOf(json.get("res_code"));
		boolean isSuccess = StringUtils.equals(token, "0");
		Map<String,  Object> map = new HashMap<String, Object>();
		map.put("resCode", token);
		map.put("isSuccess", isSuccess);
		return map;
	}
	


}
