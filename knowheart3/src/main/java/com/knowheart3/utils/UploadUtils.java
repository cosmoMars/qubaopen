package com.knowheart3.utils;

import com.knowheart3.repository.doctor.DoctorInfoRepository;
import com.knowheart3.repository.hospital.HospitalInfoRepository;
import com.knowheart3.repository.user.UserInfoRepository;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;
import org.apache.commons.codec.EncoderException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String uploadUser(Long userId, MultipartFile multipartFile) {

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

        String key = "U" + userId + "_" + System.currentTimeMillis();
        String url = "http://7xi46o.com2.z0.glb.qiniucdn.com/" + key;
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        return url;
    }

    @Transactional
    public String uploadDoctor(Doctor doctor, MultipartFile multipartFile) {

        DoctorInfo doctorInfo = doctorInfoRepository.findOne(doctor.getId());

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

        String key = "D" + doctor.getId() + "_" + System.currentTimeMillis();
        String url = "http://7xi46q.com2.z0.glb.qiniucdn.com/" + key;
        doctorInfo.setAvatarPath(url);
        doctorInfo.setLastModifiedDate(new DateTime());
        doctorInfoRepository.save(doctorInfo);
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Transactional
    public String uploadHospital(Hospital hospital, MultipartFile multipartFile) {

        HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospital.getId());

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
        String key = "H" + hospital.getId() + "_" + System.currentTimeMillis();
        String url = "http://7xi46r.com2.z0.glb.qiniucdn.com/" + key;
        hospitalInfo.setHospitalRecordPath(url);
        hospitalInfo.setLastModifiedDate(new DateTime());
        try {
            IoApi.Put(uptoken, key, multipartFile.getInputStream(), extra);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    public String uploadHospitalDoctor() {

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

        String key = "123452.jpg";
        String localFile = "/Users/mars/ME/7fd3a3e1jw1dq7sp5j1qzj.jpg";
        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        return "success";
    }

    public String retrievePriavteUrl(String url) {

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

    /**
     * 上传图片至7牛
     * type 1 医师头像，2 医师资质，3 诊所资质，4 诊所医师资质，5 明日发现，6 每日发现，7 用户头像
     * @param type
     * @param name
     * @param multipartFile
     * @return
     */
    public String uploadTo7niu(String type, String name, MultipartFile multipartFile) {

        Config.ACCESS_KEY = "NdVj6TB7C78u0PhPenlU1kzgwWvBV1mazFeBk9ma";
        Config.SECRET_KEY = "PGgA48fZfELgr-IjpboBjvLXskOp94rgF66ed__X";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在

        String bucketName = null;
        String key = name + "_" + System.currentTimeMillis();
        String url = null;
        switch (type) {
            case "1" :
                bucketName = "zhixin-doctor";
                url = "http://7xi46q.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case "2" :
                bucketName = "zhixin-doctorrecord";
                url = "http://7xi4h0.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case "3" :
                bucketName = "zhixin-hospital";
                url = "http://7xi46r.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case "4" :
                bucketName = "zhixin-hospitaldoctor";
                url = "http://7xi46t.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case "5" :
                bucketName = "zhixin-next-discovery";
                url = "http://7xi5bi.com2.z0.glb.qiniucdn.com/" + key;
                break;
            case "6" :
                bucketName = "zhixin-task-discovery";
                url = "http://7xi5bj.com2.z0.glb.clouddn.com/" + key;
                break;
            case "7" :
                bucketName = "zhixin-user";
                url = "http://7xi46o.com2.z0.glb.clouddn.com/" + key;
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
