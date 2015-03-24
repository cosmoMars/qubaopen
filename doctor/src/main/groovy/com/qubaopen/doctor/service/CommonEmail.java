package com.qubaopen.doctor.service;

import com.qubaopen.doctor.repository.mail.HostMailRepository;
import com.qubaopen.survey.entity.mail.HostMail;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mars on 15/3/23.
 */
@org.springframework.stereotype.Service
public class CommonEmail {
    @Autowired
    private HostMailRepository hostMailRepository;

    public String sendTextMail(String url, long hospitalId, String email, String captcha) {

        HostMail hostMail = hostMailRepository.findOne(1l);

        SimpleEmail sEmail = new SimpleEmail();
        //smtp host
        sEmail.setHostName("mail.transport.protocol");
        //登陆邮件服务器的用户名和密码
        sEmail.setAuthentication(hostMail.getUserName(), hostMail.getPassword());
        //接收人
        try {
            sEmail.addTo(email);

            //发送人
            sEmail.setFrom(hostMail.getUserName(), "知心团队");
            //标题
            StringBuffer buffer = new StringBuffer();
            buffer.append("感谢您加入知心！");
            buffer.append("<br>");
            buffer.append("请在15分钟内点击以下链接激活您的知心帐户、完成注册。");
            buffer.append("<br>");
            buffer.append("<br>");
            buffer.append("<div><font size =\"3\" face=\"arial\" >" + url + "activateAccount?id=" + hospitalId +"&captcha=" + captcha + "</font></div>");
            buffer.append("<br>");
            buffer.append("[知心团队]");
            sEmail.setSubject("知心欢迎您 请立即激活您的账户");
            //邮件内容
            sEmail.setMsg(buffer.toString());
            //发送
            sEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return "1";
    }
}
