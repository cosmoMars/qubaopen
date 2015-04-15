package com.knowheart3.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

@Component(value = "securityUtils")
public class SecurityUtils {

    private final static String ANDROID_CODE = "8012537";
    private final static String IOS_CODE = "027375";
    private final static String ANDROID_SECRET = "DIYG98";
    private final static String IOS_SECRET = "HIMR40";
    public static final String FROM_NAME = "from";
    public static final String SIGN_NAME = "sign";

    /**
     * ;
     * 请求验证
     *
     * @param from       请求来自于哪里
     * @param sign       签名
     * @param parameters Map参数
     * @return
     */
    public String decryption(String from, String sign, Map<String, Object> parameters) {
        //验证请求来自于哪里

        System.out.println(StringUtils.equals(ANDROID_CODE, from.trim()));
        System.out.println(StringUtils.equals(IOS_CODE, from.trim()));

        Assert.isTrue(!StringUtils.equals(ANDROID_CODE, from.trim()) || !StringUtils.equals(IOS_CODE, from.trim()), "未能识别请求来自于哪里~!");
       /* if (!StringUtils.equals(ANDROID_CODE, from.trim()) && !StringUtils.equals(IOS_CODE,from.trim())) {
            log.info("未能识别请求来自于哪里~!");
//			return ass.failure("未能识别请求来自于哪里~!");
        }
        if (null == sign || "".equals(sign.trim())) {
            log.info("未能识别请求来自于哪里~!");
//			return ClientUtils.failure("未签名的请求~!");
        }*/

        StringBuffer sb = new StringBuffer();
        parameters.remove(FROM_NAME);
        parameters.remove(SIGN_NAME);
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>(parameters);
        for (Entry<String, Object> entry : treeMap.entrySet()) {

            sb.append(entry.getKey() + "=" + entry.getValue() + ";");
        }
        if (StringUtils.equals(ANDROID_CODE, from.trim())) {
            sb.append(ANDROID_SECRET);
        } else if (StringUtils.equals(IOS_CODE, from.trim())) {
            sb.append(IOS_SECRET);
        }
        System.out.println(sb.toString());

        String encryStr = DigestUtils.md5Hex(sb.toString());
        System.out.println(encryStr);


        System.out.println(StringUtils.equals(encryStr, sign.trim()));
        Assert.isTrue(StringUtils.equals(encryStr, sign.trim()), "签名验证失败~!");
        return "success";
    }

}