package com.amazon.main.util;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.amazon.main.config.Config;

public class SendEmail {
	// // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
	// public String myEmailAccount = Config.emailFromSend().get(0);
	// public String myEmailPassword = Config.emailPassword().get(0);
	// // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
	// // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
	// public String myEmailSMTPHost = Config.smtpServer().get(0);
	// // 收件人邮箱（替换为自己知道的有效邮箱）
	// public String receiveMailAccount = Config.emailTo().get(0);
	// // 收件人邮箱（替换为自己知道的有效邮箱）
	// public String receiveMailCcAccount = Config.emailCc().get(0);

	/**
	 * 创建一封复杂邮件（文本+图片+附件）
	 */
	public MimeMessage createMimeMessage(Session session, String sendMail,
			String receiveMail, List<String> receiveMailCc, String subject,
			String fromName, String toName, List<String> toCcName,
			String zipName) throws Exception {
		// 1. 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendMail, fromName, "UTF-8"));
		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail,
				toName, "UTF-8"));
		for (int i = 0; i < receiveMailCc.size(); i++) {
			message.addRecipient(RecipientType.CC, new InternetAddress(
					receiveMailCc.get(i), toCcName.get(i), "UTF-8"));
		}
		// 4. Subject: 邮件主题
		message.setSubject(subject, "UTF-8");
		// 添加文本
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(FileUtil.file2String(
				new File(Config.getCodePath(Config.EMAILCONTENT)), "UTF-8"),
				"text/html;charset=UTF-8");
		/*
		 * 下面是邮件内容的创建:
		 */
		// 9. 创建附件“节点”
		MimeBodyPart attachment = new MimeBodyPart();
		DataHandler dh2 = new DataHandler(new FileDataSource(
				Config.getDownLoadPath() + zipName + ".zip")); // 读取本地文件
		attachment.setDataHandler(dh2); // 将附件数据添加到“节点”
		attachment.setFileName(MimeUtility.encodeText(dh2.getName())); // 设置附件的文件名（需要编码）
		// 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(htmlPart);
		mm.addBodyPart(attachment); // 如果有多个附件，可以创建多个多次添加
		mm.setSubType("mixed"); // 混合关系
		// 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
		message.setContent(mm);
		// 12. 设置发件时间
		message.setSentDate(new Date());
		// message.setText(, "utf-8"));// 设置文本内容
		// 13. 保存上面的所有设置
		message.saveChanges();
		return message;
	}

	/**
	 * 
	 * @param emailSmtpHost
	 *            smtpServer
	 * @param emailAcountsend
	 *            emailFromSend
	 * @param emailpaw
	 *            emailPassword
	 * @param emailTo
	 * @param emailCc
	 * @throws Exception
	 */
	public void sendEmail(String emailSmtpHost, String emailAcountsend,
			String emailpaw, String emailTo, List<String> emailCc,
			String subject, String fromName, String toName,
			List<String> toCcName, String zipName) throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", emailSmtpHost); // 发件人的邮箱的 SMTP
															// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
		// 开启 SSL 连接, 以及更详细的发送步骤请看上一篇: 基于 JavaMail 的 Java 邮件发送：简单邮件发送
		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
		// 3. 创建一封邮件
		MimeMessage message = createMimeMessage(session, emailAcountsend,
				emailTo, emailCc, subject, fromName, toName, toCcName, zipName);
		// 也可以保持到本地查看
		// message.writeTo(file_out_put_stream);
		// 4. 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();
		// 5. 使用 邮箱账号 和 密码 连接邮件服务器
		// 这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
		transport.connect(emailAcountsend, emailpaw);
		// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人,
		// 抄送人, 密送人
		transport.sendMessage(message, message.getAllRecipients());
		// 7. 关闭连接
		transport.close();

	}
}
