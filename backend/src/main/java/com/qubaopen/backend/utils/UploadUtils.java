package com.qubaopen.backend.utils;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.rs.PutPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by mars on 15/3/17.
 */
@Service
public class UploadUtils {


    public String uploadDailyDiscovery(Long id, MultipartFile multipartFile) {
        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
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
     * @param multipartFile
     * @return
     */
    public String uploadTo7niu(Integer type, String name, MultipartFile multipartFile) {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在

        String bucketName = null;
        String key = name + "_" + System.currentTimeMillis();
        String url = null;
        switch (type) {
            case 1 :
                bucketName = "zhixin-doctor";
                url = "http://7xi46q.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case 2 :
                bucketName = "zhixin-doctorrecord";
                url = "http://7xi4h0.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case 3 :
                bucketName = "zhixin-hospital";
                url = "http://7xi46r.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case 4 :
                bucketName = "zhixin-hospitaldoctor";
                url = "http://7xi46t.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case 5 :
                bucketName = "zhixin-next-discovery";
                url = "http://7xi5bi.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case 6 :
                bucketName = "zhixin-task-discovery";
                url = "http://7xi5bj.com2.z0.glb.clouddn.com/" + key;
                break;
            case 7 :
                bucketName = "zhixin-user";
                url = "http://7xi46o.com2.z0.glb.clouddn.com/" + key;
                break;
            case 8 :
                bucketName = "zhixin-hospital-record";
                url = "http://7xi6cw.com2.z0.glb.clouddn.com/" + key;
                break;
            default:
                bucketName = "zhixin";
                url = "http://7viiw7.com2.z0.glb.clouddn.com/" + key;
                break;
        }

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
