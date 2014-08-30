package com.qubaopen.survey.service;

import org.springframework.stereotype.Service;

import cn.id5.gboss.businesses.validator.service.app.Des2;
import cn.id5.gboss.businesses.validator.service.app.QueryValidatorServices;
import cn.id5.gboss.businesses.validator.service.app.QueryValidatorServicesProxy;

@Service
public class IdentityValidationService {
	
	public void identityValidation(String id, String name) throws Exception {
		
		QueryValidatorServicesProxy proxy = new QueryValidatorServicesProxy(
				"http://gboss.id5.cn/services/QueryValidatorServices");
		
		QueryValidatorServices service = proxy.getQueryValidatorServices();
		
		String key = "12345678";
		String userName = Des2.encode(key, "shzc1".getBytes());
		String password = Des2.encode(key, "shzc1_3gxyI$P3".getBytes());
		System.setProperty("javax.net.ssl.trustStore", "keystore");
		
		String datasource = Des2.encode(key, "1A020201".getBytes());
		
		id = "31011019820728511X";
		name = "王强";
		String param = name + "," + id;
		
		String resultXML = service.querySingle(userName, password, datasource,
				Des2.encode(key, param.getBytes("GBK")));
		
		System.out.println("resultXml  ================================ " + resultXML);
		
		resultXML = Des2.decodeValue(key, resultXML);
		System.out.println("resultXml2 ================================ " + resultXML);
	}

}
