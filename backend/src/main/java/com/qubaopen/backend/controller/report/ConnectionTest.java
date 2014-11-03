package com.qubaopen.backend.controller.report;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class ConnectionTest {

	// public static void main(String[] args) throws IOException {
	// String surl =
	// "https://oauth.api.189.cn/emp/oauth2/v3/authorize?app_id=178348250000035560&app_secret=abd2d041cb8613e0fee922ac7d7dbdff&redirect_uri=http://115.28.176.74/189sms/auth.htm&response_type=token";
	//
	// URL url = new URL(surl);
	//
	// URLConnection conn = url.openConnection();
	//
	// conn.setDoOutput(true);
	//
	// OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
	//
	// String str = "userName=18021012486&password=sk19880824";
	//
	// out.write(str);
	//
	// Reader reader = new InputStreamReader(new
	// BufferedInputStream(url.openStream()));
	//
	// System.out.println(reader.read());
	//
	// out.flush();
	//
	// out.close();
	// }

	public static void main(String[] args) {

//		JSONObject json = new JSONObject();
//		json.put("userName", "18021012486");
//		json.put("password", "sk19880824");

		HttpClient client = new HttpClient();
		String url = "http://open.189.cn/";
		//https://oauth.api.189.cn/emp/oauth2/v3/access_token?grant_type=authorization_code&code=91000975&app_id=178348250000035560&app_secret=abd2d041cb8613e0fee922ac7d7dbdff
//https://oauth.api.189.cn:443/emp/oauth2/v3/udblogin/26012302
		
		// PutMethod put = new
		// PutMethod("https://oauth.api.189.cn/emp/oauth2/v3/authorize?app_id=178348250000035560&app_secret=abd2d041cb8613e0fee922ac7d7dbdff&redirect_uri=http://115.28.176.74/189sms/auth.htm&response_type=token");
		PostMethod post = new PostMethod(url);

		try {
			NameValuePair[] data = { new NameValuePair("userName", "18021012486"), new NameValuePair("password", "sk19880824") };
			post.setRequestBody(data);  
//			post.setRequestEntity(new StringRequestEntity(json.toString(), "application/json", "UTF-8"));
			int responseCode = client.executeMethod(post);
			System.out.println(responseCode);

			String response = post.getResponseBodyAsString();
			URI purl = post.getURI();

			Header locationHeader = post.getResponseHeader("location");
			System.out.println(locationHeader.getName());
			String location = locationHeader.getValue();

			System.out.println(response);
			System.out.println(purl.toString());
			System.out.println(location);
		} catch (Exception e) {

		}

	}
}
