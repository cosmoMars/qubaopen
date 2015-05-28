package com.knowheart3.service;

import groovy.json.JsonSlurper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/5/14.
 */
@RestController
@RequestMapping("189sms")
public class Sms {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        System.out.println("enter auth-------------------------------------------");
        // 读取异步通知数据
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }
        reader.close();

        // 解析异步通知数据
        String notifyJson = new String(buffer);

        System.out.println(notifyJson);


    }

    @RequestMapping(value = "sms")
    public void sms() {
        StringBuilder builder = new StringBuilder();
        builder.append("app_id=404743360000037976");
        builder.append("&");
        builder.append("app_secret=d0e50def25c6fe94c56d697e1dc328bd");
        builder.append("&");
        builder.append("redirect_uri=http://115.28.176.74/189sms/auth.htm");
        builder.append("&");
        builder.append("response_type=token");


        HttpEntity<String> request = new HttpEntity<String>(builder.toString());

        String result = restTemplate.postForObject("https://oauth.api.189.cn/emp/oauth2/v3/authorize", request, String.class);


//        String result = restTemplate.postForObject(st.getUrl(), request, String.class);

        if (result != null) {
            @SuppressWarnings("unchecked")
            Map<String, ?> json = (Map<String, ?>) new JsonSlurper().parseText(result);

            System.out.println(json.toString());
        }


        Map<String, Object> map = new HashMap<>();
    }
}
