package com.qubaopen.doctor.utils;

import java.io.IOException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.URLUtils;
import org.apache.commons.codec.EncoderException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.doctor.repository.user.UserInfoRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserInfo;

/**
 * Created by mars on 15/3/17.
 */
@Service
public class UploadUtils {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DoctorInfoRepository doctorInfoRepository;

    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;

    @Transactional
    public String uploadUser(User user, MultipartFile multipartFile) {

        UserInfo userInfo = userInfoRepository.findOne(user.getId());

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

        String key = "U" + user.getId() + "_" + System.currentTimeMillis();
        String url = "http://7xi46o.com2.z0.glb.qiniucdn.com/" + key;
        userInfo.setAvatarPath(url);
        userInfo.setLastModifiedDate(new DateTime());
        userInfoRepository.save(userInfo);
//        String localFile = "/Users/mars/ME/7fd3a3e1jw1dq7sp5j1qzj.jpg";
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        return "success";
    }

    @Transactional
    public String uploadDoctorAvatar(Long doctorId, MultipartFile multipartFile) {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zhixin-doctor";

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();

        String key = "D" + doctorId + "_" + System.currentTimeMillis();
        String url = "http://7xi46q.com2.z0.glb.qiniucdn.com/" + key;
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
    @Transactional
    public String uploadDoctorRecord(Long hospitalId, MultipartFile multipartFile) {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zhixin-doctorrecord";

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();

        String key = "DR" + hospitalId + "_" + System.currentTimeMillis();
        String url = "http://7xi4h0.com2.z0.glb.qiniucdn.com/" + key;
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Transactional
    public String uploadHospital(Long hospitalId, MultipartFile multipartFile) {

        HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospitalId);

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zhixin-hospital";

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();
        String key = "H" + hospitalId + "_" + System.currentTimeMillis();
        String url = "http://7xi46r.com2.z0.glb.qiniucdn.com/" + key;
        hospitalInfo.setHospitalRecordPath(url);
        hospitalInfo.setLastModifiedDate(new DateTime());
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String uploadHospitalDoctor(Long hospitalId, MultipartFile multipartFile) {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zhixin-hospitaldoctor";

        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();
        String key = "HD" + hospitalId + "_" + System.currentTimeMillis();
        String url = "http://7xi46t.com2.z0.glb.qiniucdn.com/" + key;
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        return url;
    }


    public String retrievePriavteUrl(String url) {

        if (!url.startsWith("http://")) {
            return "";
        }

        String[] str = url.split("/");

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        String downloadUrl = null;
        try {
            if (null != str[2] && null != str[3]) {
                String baseUrl = URLUtils.makeBaseUrl(str[2], str[3]);
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
}
