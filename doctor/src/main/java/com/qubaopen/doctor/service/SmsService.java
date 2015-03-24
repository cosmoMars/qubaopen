package com.qubaopen.doctor.service;

import com.qubaopen.doctor.repository.smstoken.SmsCallBackRepository;
import com.qubaopen.doctor.repository.smstoken.SmsTokenRepository;
import com.qubaopen.survey.entity.system.SmsCallBack;
import com.qubaopen.survey.entity.token.SmsToken;
import groovy.json.JsonSlurper;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private SmsCallBackRepository smsCallBackRepository;

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

    public Map<String, Object> sendSmsMessage(String phone, int type, String param) {

        SmsToken st = smsTokenRepository.findOne((long) type);

        StringBuilder builder = new StringBuilder();
        try {
            String encode = URLEncoder.encode(param, "utf-8");
            builder.append("app_id=").append(st.getAppId()).append('&')
                    .append("access_token=").append(st.getToken()).append('&')
                    .append("template_id=").append(st.getTemplateId()).append('&')
                    .append("template_param=").append(encode).append('&')
                    .append("acceptor_tel=").append(phone).append('&')
                    .append("timestamp=").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpEntity<String> request = new HttpEntity<String>(builder.toString());
        String result = restTemplate.postForObject(st.getUrl(), request, String.class);

        @SuppressWarnings("unchecked")
        Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(result);

        String resCode = String.valueOf(json.get("res_code"));

        SmsCallBack smsCallBack = new SmsCallBack();
        smsCallBack.setPhone(phone);
        smsCallBack.setTempleteId(st.getTemplateId());
        smsCallBack.setResCode(resCode);
        smsCallBack.setTime(new Date());
        smsCallBackRepository.save(smsCallBack);

        boolean isSuccess = StringUtils.equals(resCode, "0");
        Map<String,  Object> map = new HashMap<>();
        map.put("resCode", resCode);
        map.put("isSuccess", isSuccess);
        return map;
    }
}
