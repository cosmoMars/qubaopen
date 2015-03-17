package com.qiniu;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.BatchStatRet;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;

/**
 * Created by mars on 15/3/16.
 */
public class Upload {

//    public static void main(String[] args) {
//        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
//        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";
//        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
//        // 请确保该bucket已经存在
//        String bucketName = "zhixin-user";
//        PutPolicy putPolicy = new PutPolicy(bucketName);
//        String uptoken = null;
//        try {
//            uptoken = putPolicy.token(mac);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        PutExtra extra = new PutExtra();
//        String key = "123452.jpg";
//        String localFile = "/Users/mars/ME/7fd3a3e1jw1dq7sp5j1qzj.jpg";
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
//        System.out.println("success");
//    }

//    public static void main(String[] args) throws Exception {
//        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
//        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";
//        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
//        String baseUrl = URLUtils.makeBaseUrl("7xi4h0.com2.z0.glb.qiniucdn.com", "D1_1426572759957");
//        GetPolicy getPolicy = new GetPolicy();
//        String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
//        System.out.println(downloadUrl);
//    }

//    public static void main(String[] args) {
//        String url = "http://7xi4h0.com2.z0.glb.qiniucdn.com/D1_1426572759957";
//        String[] str = url.split("/");
//        System.out.println(str.length);
//        System.out.println(str[1]);
//        System.out.println(str[2]);
//        System.out.println(str[3]);
//    }
}
