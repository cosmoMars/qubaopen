package com.knowheart3.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knowheart3.repository.HostMail.HostMailRepository;
import com.qubaopen.survey.entity.mail.HostMail;

//import org.apache.log4j.net.SMTPAppender;

@Service
public class CaptchaService {

    @Autowired
    HostMailRepository hostMailRepository;

	public String sendTextMail(String content, String contentMethod) throws UnsupportedEncodingException {

		HostMail hostMail = hostMailRepository.findOne(1l);
		
		Properties properties = new Properties();
		
		properties.setProperty("mail.transport.protocol", "smtp"); //发送邮件协议
		properties.setProperty("mail.smtp.auth", "true"); //需要验证


		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(properties);
		sendMailSession.setDebug(true);
		try {

			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 设置邮件消息的发送者
			mailMessage.setFrom(new InternetAddress(hostMail.getUserName()));
			
			// 设置邮件消息的主题
			mailMessage.setSubject("知心用户反馈");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("<br>");
			buffer.append("<div><font size =\"5\" face=\"arial\" >" + "内容： " + content + "</font></div>");
            buffer.append("<div><font size =\"5\" face=\"arial\" >" + "联系方式： " + contentMethod + "</font></div>");
			buffer.append("[知心团队]");
			mailMessage.setContent(buffer.toString(), "text/html;charset=utf-8");
//			mailMessage.setText(buffer.toString()); 
			//发送邮件
			Transport tran = sendMailSession.getTransport();
			
			String nickName = MimeUtility.encodeText("知心团队");
			
			mailMessage.setFrom(new InternetAddress(nickName + " <" + hostMail.getUserName() + ">")); // 设置邮件发送人标题
//			mailMessage.setRecipient(RecipientType.TO, new InternetAddress(content)); // 设置邮件接收人显示
			
			tran.connect(hostMail.getServerHost(), hostMail.getServerPort(), hostMail.getUserName(), hostMail.getPassword());//连接到邮箱服务器

			tran.sendMessage(mailMessage, new Address[]{ new InternetAddress("349280576@qq.com"), new InternetAddress("361714571@qq.com")});//设置邮件接收人
			tran.close();
			
			return "1";
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return "0";
	}

}
