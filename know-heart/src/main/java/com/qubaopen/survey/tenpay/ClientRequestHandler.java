package com.qubaopen.survey.tenpay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClientRequestHandler extends PrepayIdRequestHandler {

    public String getXmlBody() {
        StringBuffer sb = new StringBuffer();
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"appkey".equals(k)) {
                sb.append("<" + k + ">" + v + "<" + k + ">" + "\r\n");
            }
        }
        return sb.toString();
    }

    public Map<String, Object> getJsonBody() {
        Map<String, Object> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"appkey".equals(k)) {
                result.put(k, v);
            }
        }
        return result;
    }
}
