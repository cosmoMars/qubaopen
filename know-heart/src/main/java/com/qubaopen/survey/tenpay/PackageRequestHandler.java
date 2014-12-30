package com.qubaopen.survey.tenpay;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PackageRequestHandler extends RequestHandler {

    /**
     * 获取带参数的请求URL
     * @return String
     * @throws UnsupportedEncodingException
     */
    public String getRequestURL() throws UnsupportedEncodingException {

        this.createSign();

        StringBuffer sb = new StringBuffer();
        String enc = "UTF-8";
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();

//            sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
            sb.append(k + "=" + v + "&");
        }

        //去掉最后一个&
        String reqPars = sb.substring(0, sb.lastIndexOf("&"));
        // 设置debug信息
        this.setDebugInfo("md5 sb:" + getDebugInfo() + "\r\npackage:" + reqPars);
        return reqPars;

    }

}
