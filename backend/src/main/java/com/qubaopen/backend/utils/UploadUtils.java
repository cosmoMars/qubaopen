package com.qubaopen.backend.utils;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.rs.PutPolicy;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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


    public String uploadDailyDiscovery(Long id, MultipartFile multipartFile) {

        // 请确保该bucket已经存在
        String bucketName = "zhixin-next-discovery";

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();

        String key = "DDP" + id + "_" + System.currentTimeMillis();
        String url = "http://7xi46t.com2.z0.glb.qiniucdn.com/" + key;
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        return url;

    }

    /**
     * 上传图片至7牛
     * type 1 医师头像，2 医师资质，3 诊所头像，4 诊所医师资质，5 明日发现，6 每日发现，7 用户头像，8 诊所资质
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
