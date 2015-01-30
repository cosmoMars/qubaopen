package com.qubaopen.doctor.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.log4j.net.SMTPAppender;
import org.springframework.stereotype.Service;

import com.qubaopen.doctor.repository.mail.HostMailRepository;
import com.qubaopen.survey.entity.mail.HostMail;

@Service
public class CaptchaService {

	@Autowired
	private HostMailRepository hostMailRepository;

	public String sendTextMail(String url, long hospitalId, String email, String captcha) {

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
			mailMessage.setSubject("知心欢迎您 请立即激活您的账户");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("感谢您加入知心！");
			buffer.append("<br>");
			buffer.append("请在15分钟内点击以下链接激活您的知心帐户、完成注册。");
			buffer.append("<br>");
			buffer.append("<br>");
			buffer.append("<div><font size =\"3\" face=\"arial\" >" + url + "activateAccount?id=" + hospitalId +"&captcha=" + captcha + "</font></div>");
			buffer.append("<br>");
//			buffer.append("请在15分钟内点击完成注册");
//			buffer.append("<br>");
			buffer.append("[知心团队]");
			//设置邮件内容
//			mailMessage.setText(buffer.toString()); 
			mailMessage.setContent(buffer.toString(), "text/html;charset=utf-8");
			//发送邮件
			Transport tran = sendMailSession.getTransport();
			
			String nickName = "";
			try {
				nickName = MimeUtility.encodeText("知心团队");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			mailMessage.setFrom(new InternetAddress(nickName + " <" + hostMail.getUserName() + ">")); // 设置邮件发送人标题
			mailMessage.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置邮件接收人显示
			
			tran.connect(hostMail.getServerHost(), hostMail.getServerPort(), hostMail.getUserName(), hostMail.getPassword()); //连接到邮箱服务器

			tran.sendMessage(mailMessage, new Address[]{ new InternetAddress(email)}); //设置邮件接收人
			tran.close();
			
			return "1";
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return "0";
	}
	
	public String sendTextMail(String email, String captcha) throws UnsupportedEncodingException {

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
			mailMessage.setSubject("知心欢迎您");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("尊敬的知心用户您的验证码为：");
			buffer.append("<br>");
			buffer.append("<div><font size =\"5\" face=\"arial\" >" + captcha + "</font></div>");
			buffer.append("[知心团队]");
			mailMessage.setContent(buffer.toString(), "text/html;charset=utf-8");
//			mailMessage.setText(buffer.toString()); 
			//发送邮件
			Transport tran = sendMailSession.getTransport();
			
			String nickName = MimeUtility.encodeText("知心团队");
			
			mailMessage.setFrom(new InternetAddress(nickName + " <" + hostMail.getUserName() + ">")); // 设置邮件发送人标题
			mailMessage.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置邮件接收人显示
			
			tran.connect(hostMail.getServerHost(), hostMail.getServerPort(), hostMail.getUserName(), hostMail.getPassword());//连接到邮箱服务器

			tran.sendMessage(mailMessage, new Address[]{ new InternetAddress(email)});//设置邮件接收人
			tran.close();
			
			return "1";
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return "0";
	}

}
