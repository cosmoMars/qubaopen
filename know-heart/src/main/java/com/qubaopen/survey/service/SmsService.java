package com.qubaopen.survey.service;

import groovy.json.JsonSlurper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qubaopen.survey.entity.token.SmsToken;
import com.qubaopen.survey.repository.smstoken.SmsTokenRepository;

@Service
public class SmsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SmsTokenRepository smsTokenRepository;

	@Value("${sms189_url}")
	private String sms189_url;

	@Value("${sms189_app_id}")
	private String sms189_app_id;

	@Value("${sms189_app_secret}")
	private String sms189_app_secret;

	@Value("${sms189_access_token}")
	private String sms189_access_token;

	@Value("${sms189_template_id}")
	private String sms189_template_id;

	@Value("${sms189_template_param}")
	private String sms189_template_param;

	public Map<String, Object> sendCaptcha(String phone) {
		return sendCaptcha(phone, RandomStringUtils.randomNumeric(6));
	}

	public Map<String, Object> sendCaptcha(String phone, String captcha) {
		
		SmsToken st = smsTokenRepository.findOne(1l);

		LOGGER.trace("sms189_url := {}", sms189_url);
		LOGGER.trace("sms189_app_id := {}", sms189_app_id);
		LOGGER.trace("sms189_app_secret := {}", sms189_app_secret);
		LOGGER.trace("sms189_access_token := {}", sms189_access_token);
		LOGGER.trace("sms189_template_id := {}", sms189_template_id);
		LOGGER.trace("sms189_template_param := {}", sms189_template_param);

		StringBuilder builder = new StringBuilder();
//		builder.append("app_id=").append(sms189_app_id).append('&')
//			.append("access_token=").append(sms189_access_token).append('&')
//			.append("template_id=").append(sms189_template_id).append('&')
//			.append("template_param=").append('{').append("\"validatecode\" : ").append(captcha).append('}').append('&')
//			.append("acceptor_tel=").append(phone).append('&')
//			.append("timestamp=").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		builder.append("app_id=").append(st.getAppId()).append('&')
			.append("access_token=").append(st.getToken()).append('&')
			.append("template_id=").append(st.getTemplateId()).append('&')
			.append("template_param=").append('{').append("\"validatecode\" : ").append(captcha).append('}').append('&')
			.append("acceptor_tel=").append(phone).append('&')
			.append("timestamp=").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));


		HttpEntity<String> request = new HttpEntity<String>(builder.toString());
		String result = restTemplate.postForObject(st.getUrl(), request, String.class);

		@SuppressWarnings("unchecked")
		Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(result);
		
		String resCode = String.valueOf(json.get("res_code"));
		boolean isSuccess = StringUtils.equals(resCode, "0");
		Map<String,  Object> map = new HashMap<String, Object>();
		map.put("resCode", resCode);
		map.put("isSuccess", isSuccess);
		return map;
	}

	@SuppressWarnings("unused")
	private void refreshToken() {
		StringBuilder codeBuilder = new StringBuilder();
		codeBuilder.append("app_id=178348250000035560").append("&")
			.append("redirect_uri=http://115.28.176.74/189sms/auth.htm").append("&")
			.append("response_type=code");
		HttpEntity<String> codeRequest = new HttpEntity<String>(codeBuilder.toString());
		
		String codeResult = restTemplate.postForObject("https://oauth.api.189.cn/emp/oauth2/v3/authorize", codeRequest, String.class);

		@SuppressWarnings("unchecked")
		Map<String, ?> codeJosn = (Map<String, ?>) new JsonSlurper().parseText(codeResult);
		String code = String.valueOf(codeJosn.get("code"));
		System.out.println(code);
		
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append("grant_type=authorization_code").append("&")
			.append("code=").append(code).append("&")
			.append("app_id=").append("178348250000035560").append("&")
			.append("app_secret=").append("abd2d041cb8613e0fee922ac7d7dbdff")
			.append("redirect_uri=").append("http://115.28.176.74/189sms/auth.htm");
		
		HttpEntity<String> tokenRequest = new HttpEntity<String>(tokenBuilder.toString());
		
		String tokenResult = restTemplate.postForObject("https://oauth.api.189.cn/emp/oauth2/v3/access_token", tokenRequest, String.class);
		String access_token = String.valueOf("access_token");
		String expires_in = String.valueOf("expires_in");
		String refresh_token = String.valueOf("refresh_token");
		String open_id = String.valueOf("open_id");
		String res_code = String.valueOf("res_code");
		String res_message = String.valueOf("res_message");
		
		System.out.println(access_token);
		System.out.println(expires_in);
		System.out.println(refresh_token);
		System.out.println(open_id);
		System.out.println(res_code);
		System.out.println(res_message);
		
		StringBuilder refreshBuilder = new StringBuilder();
		refreshBuilder.append("grant_type=").append("refresh_token").append("&")
			.append("refresh_token=").append(access_token).append("&")
			.append("app_id=").append("178348250000035560").append("&")
			.append("app_secret").append("abd2d041cb8613e0fee922ac7d7dbdff");
		HttpEntity<String> refreshRequest = new HttpEntity<String>(refreshBuilder.toString());
		
		String refreshResult = restTemplate.postForObject("https://oauth.api.189.cn/emp/oauth2/v3/access_token", refreshRequest, String.class);
		@SuppressWarnings("unchecked")
		Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(refreshResult);
		String accessToken = String.valueOf(json.get("access_Token"));
		String expiresIn = String.valueOf(json.get("expires_in"));
		String refreshToken = String.valueOf(json.get("refresh_token"));
		String resCode = String.valueOf(json.get("res_code"));
		String resMessage =String.valueOf(json.get("res_message"));
		
		System.out.println(accessToken);
		System.out.println(expiresIn);
		System.out.println(refreshToken);
		System.out.println(resCode);
		System.out.println(resMessage);
		
	}

}
