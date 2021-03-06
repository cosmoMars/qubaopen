package com.qubaopen.backend.utils;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mars on 15/3/17.
 */
@Service
public class UploadUtils {

    private static Mac mac;
    static {
        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";
        mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
    }


    public static String retrievePriavteUrl(String url) {

        if (null == url || !url.startsWith("http://")) {
            return "";
        }

        String[] str = url.split("/");


        List<String> list = new ArrayList<>();
        for (int i = 3; i < str.length ; i++) {
            list.add(str[i]);
        }

        String downloadUrl = null;
        try {
            if (null != str[2] && list.size() > 0) {
                String baseUrl = URLUtils.makeBaseUrl(str[2], StringUtils.join(list, "/"));
                GetPolicy getPolicy = new GetPolicy();
                downloadUrl = getPolicy.makeRequest(baseUrl, mac);
            }
        } catch (EncoderException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            e.printStackTrace();
        }
        return downloadUrl;
    }

    /**
     * 上传图片至7牛
     * type 1 公有图片，2 私有图片，3 默认
     * @param type
     * @param name
     * @param io
     * @return
     */
    public static String uploadTo7niu(Integer type, String name, InputStream io) {

        // 请确保该bucket已经存在

        String bucketName;
        String key = name + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssssss");
        String url;
        switch (type) {
            case 1 :
            bucketName = "zhixin-public";
            url = "http://7xi893.com2.z0.glb.clouddn.com/" + key;
            break;
            case 2 :
                bucketName = "zhixin-private";
                url = "http://7xi894.com2.z0.glb.clouddn.com/" + key;
                break;
            default:
                bucketName = "zhixin";
                url = "http://7viiw7.com2.z0.glb.clouddn.com/" + key;
                break;
        }

        PutPolicy putPolicy = new PutPolicy(bucketName);

        try {
            String uptoken = putPolicy.token(mac);
            PutExtra extra = new PutExtra();
            IoApi.Put(uptoken, key, io, extra);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}
