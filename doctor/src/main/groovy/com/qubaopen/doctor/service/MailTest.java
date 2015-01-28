package com.qubaopen.doctor.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailTest {
	
	//javax mail 测试
	public static void main(String[] args) throws AddressException, MessagingException, UnsupportedEncodingException {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");//发送邮件协议
		properties.setProperty("mail.smtp.auth", "true");//需要验证
		// properties.setProperty("mail.debug", "true");//设置debug模式 后台输出邮件发送的过程
		Session session = Session.getInstance(properties);
		session.setDebug(true);//debug模式
		//邮件信息
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("postmaster@qudiaoyan.cn"));//设置发送人
		message.setText("captcha");//设置邮件内容
		message.setSubject("验证码");//设置邮件主题
		//发送邮件
		Transport tran = session.getTransport();
		// tran.connect("smtp.sohu.com", 25, "wuhuiyao@sohu.com", "xxxx");//连接到新浪邮箱服务器
		tran.connect("smtp.mxhichina.com", 25, "postmaster@qudiaoyan.cn", "Qubaopen2013");//连接到新浪邮箱服务器
		// tran.connect("smtp.qq.com", 25, "Michael8@qq.vip.com", "xxxx");//连接到QQ邮箱服务器

		String nickName = MimeUtility.encodeText("知心团队");  
		
		message.setFrom(new InternetAddress(nickName + " <postmaster@qudiaoyan.cn>")); // 设置邮件发送人标题
		message.setRecipient(RecipientType.TO, new InternetAddress("455429974@qq.com")); // 设置邮件接收人显示
		
		tran.sendMessage(message, new Address[]{new InternetAddress("455429974@qq.com")});//设置邮件接收人
		tran.close();
	}
	
	/*public static void main(String[] args) throws MessagingException {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.mxhichina.com");
        // 发件人的账号
        props.put("mail.user", "postmaster@qudiaoyan.cn");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "Qubaopen2013");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress("349280576@qq.com");
        message.setRecipient(RecipientType.TO, to);

        // 设置抄送
//        InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
//        message.setRecipient(RecipientType.CC, cc);
//
//        // 设置密送，其他的收件人不能看到密送的邮件地址
//        InternetAddress bcc = new InternetAddress("aaaaa@163.com");
//        message.setRecipient(RecipientType.CC, bcc);

        // 设置邮件标题
        message.setSubject("测试邮件");

        // 设置邮件的内容体
        message.setContent("<a href='http://www.fkjava.org'>测试的HTML邮件</a>", "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
        System.out.println("111111111");
    }*/
}
