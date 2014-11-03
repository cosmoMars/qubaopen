package com.qubaopen.backend.controller.report;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class ConnectionTest1 {

	public static void main(String[] args) {

		String loginUrl = "https://oauth.api.189.cn:443/emp/oauth2/v3/udblogin/26012302";
		String dataUrl = "https://oauth.api.189.cn/emp/oauth2/v3/authorize?app_id=178348250000035560&app_secret=abd2d041cb8613e0fee922ac7d7dbdff&redirect_uri=http://115.28.176.74/189sms/auth.htm&response_type=token";

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(loginUrl);

		NameValuePair[] data = { new NameValuePair("userName", "18021012486"), new NameValuePair("password", "sk19880824") };
		postMethod.setRequestBody(data);
		try {
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//			httpClient.executeMethod(postMethod);
			
			int responseCode = httpClient.executeMethod(postMethod);
			System.out.println(responseCode);
			
			Cookie[] cookies = httpClient.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
			}
			GetMethod getMethod = new GetMethod(dataUrl);
			getMethod.setRequestHeader("cookie", tmpcookies.toString());
			int getCode = httpClient.executeMethod(getMethod);
			// 打印出返回数据，检验一下是否成功
//			String text = getMethod.getResponseBodyAsString();
			System.out.println(getCode);
			System.out.println(getMethod.getURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
//		JSONObject json = new JSONObject();
//		json.put("userName", "18021012486");
//		json.put("password", "sk19880824");

		
		
		
		/*HttpClient client = new HttpClient();
		String url = "http://10.0.0.88:8080/know-heart/users/login";

		// PutMethod put = new
		// PutMethod("https://oauth.api.189.cn/emp/oauth2/v3/authorize?app_id=178348250000035560&app_secret=abd2d041cb8613e0fee922ac7d7dbdff&redirect_uri=http://115.28.176.74/189sms/auth.htm&response_type=token");
		PostMethod post = new PostMethod(url);

		try {
			NameValuePair[] data = { new NameValuePair("phone", "13611845993"), new NameValuePair("password", "11111111") };
			post.setRequestBody(data);  
//			post.setRequestEntity(new StringRequestEntity(json.toString(), "application/json", "UTF-8"));
			int responseCode = client.executeMethod(post);
			System.out.println(responseCode);

			String response = post.getResponseBodyAsString();
			URI purl = post.getURI();

//			Header locationHeader = post.getResponseHeader("location");
//			System.out.println(locationHeader.getName());
//			String location = locationHeader.getValue();

			System.out.println(response);
			System.out.println(purl.toString());
//			System.out.println(location);
		} catch (Exception e) {

		}
*/
		
	}
}
