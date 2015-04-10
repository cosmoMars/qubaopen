package com.qubaopen.backend.utils;

import com.qubaopen.backend.repository.hostMail.HostMailRepository;
import com.qubaopen.survey.entity.mail.HostMail;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mars on 15/3/23.
 */
@Service
public class CommonEmail {

    @Autowired
    private HostMailRepository hostMailRepository;

    public String sendTextMail(String content, String email, String url) {

        HostMail hostMail = hostMailRepository.findOne(1l);

        HtmlEmail sEmail = new HtmlEmail ();
        //smtp host
        sEmail.setHostName("smtp.mxhichina.com");
        //登陆邮件服务器的用户名和密码
        sEmail.setAuthentication(hostMail.getUserName(), hostMail.getPassword());
        //接收人
        try {
            sEmail.addTo(email);
            //发送人
            sEmail.setFrom(hostMail.getUserName(), "知心团队", "UTF-8");
            //标题
            StringBuffer buffer = new StringBuffer();
            buffer.append("问题来了");
            buffer.append("<br>");
            buffer.append("<div><font size =\"3\" face=\"arial\" >" + content + "</font></div>");
            buffer.append("<div><font size =\"3\" face=\"arial\" >" + url + "</font></div>");
//            buffer.append("<a href='" + url + "'>");
            buffer.append("<br>");
            sEmail.setSubject("待解决问题booking");
            //邮件内容
            sEmail.setHtmlMsg(buffer.toString());
            sEmail.setCharset("UTF-8");

            //发送
            sEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return "1";
    }

}
