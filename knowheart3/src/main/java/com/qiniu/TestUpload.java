package com.qiniu;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by mars on 15/3/16.
 */
@RestController
@RequestMapping("testUpload")
public class TestUpload {

    @RequestMapping
    public void uplaodTest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zhixin-user";
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();
        String key = "123452.jpg";
        String localFile = "/Users/mars/ME/7fd3a3e1jw1dq7sp5j1qzj.jpg";
        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print("success");
        out.close();
    }


}
