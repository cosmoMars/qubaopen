package com.knowheart3.security;

import java.util.HashMap;
import java.util.Map;

public class MD5Test {

    private static SecurityUtils su = new SecurityUtils();
    private final static String ANDROID_CODE = "8012537";
    private final static String IOS_CODE = "027375";

    public static void main(String args[]) {

        checkSecurity();
    }

    /**
     * 登录
     *
     * @return
     */
    public static String checkSecurity() {
        Map<String, Object> singMap = new HashMap<String, Object>();
        singMap.put("from", IOS_CODE);
        singMap.put("phone", "13621673989");
        singMap.put("password", "11111111");
        singMap.put("userType", 1);
        String verifyResult = su.decryption(ANDROID_CODE, "84ea99669a99ba72b9b059809500284b", singMap);

        return verifyResult;
    }
}
