package com.qubaopen.survey;

import cn.id5.gboss.businesses.validator.service.app.Des2;
import cn.id5.gboss.businesses.validator.service.app.QueryValidatorServices;
import cn.id5.gboss.businesses.validator.service.app.QueryValidatorServicesProxy;

public class IdentityValidationTest {

	public static void main(String[] args) throws Exception {
		QueryValidatorServicesProxy proxy = new QueryValidatorServicesProxy();
		proxy.setEndpoint("https://gboss.id5.cn/services/QueryValidatorServices?wsdl");
		QueryValidatorServices service = proxy.getQueryValidatorServices();
		
		String key = "12345678";
		String userName = Des2.encode(key, "shzc1".getBytes());
		String password = Des2.encode(key, "shzc1_3gxyI$P3".getBytes());
		//System.setProperty("javax.net.ssl.trustStore", "keystore");
		
		String datasource = Des2.encode(key, "1A020201".getBytes());
		
		String id = "31011019820728511X";
		String name = "王强";
		String param = name + "," + id;
		
		String resultXML = service.querySingle(userName, password, datasource,
				Des2.encode(key, param.getBytes("GBK")));
		
		System.out.println(resultXML);
		
		resultXML = Des2.decode(key, resultXML.getBytes());
		System.out.println(resultXML);
	}

}
