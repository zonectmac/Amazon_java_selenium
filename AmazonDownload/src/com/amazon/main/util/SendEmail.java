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
	// // �����˵� ���� �� ���루�滻Ϊ�Լ�����������룩
	// public String myEmailAccount = Config.emailFromSend().get(0);
	// public String myEmailPassword = Config.emailPassword().get(0);
	// // ����������� SMTP ��������ַ, ����׼ȷ, ��ͬ�ʼ���������ַ��ͬ, һ���ʽΪ: smtp.xxx.com
	// // ����163����� SMTP ��������ַΪ: smtp.163.com
	// public String myEmailSMTPHost = Config.smtpServer().get(0);
	// // �ռ������䣨�滻Ϊ�Լ�֪������Ч���䣩
	// public String receiveMailAccount = Config.emailTo().get(0);
	// // �ռ������䣨�滻Ϊ�Լ�֪������Ч���䣩
	// public String receiveMailCcAccount = Config.emailCc().get(0);

	/**
	 * ����һ�⸴���ʼ����ı�+ͼƬ+������
	 */
	public MimeMessage createMimeMessage(Session session, String sendMail,
			String receiveMail, List<String> receiveMailCc, String subject,
			String fromName, String toName, List<String> toCcName,
			String zipName) throws Exception {
		// 1. �����ʼ�����
		MimeMessage message = new MimeMessage(session);
		// 2. From: ������
		message.setFrom(new InternetAddress(sendMail, fromName, "UTF-8"));
		// 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
		message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail,
				toName, "UTF-8"));
		for (int i = 0; i < receiveMailCc.size(); i++) {
			message.addRecipient(RecipientType.CC, new InternetAddress(
					receiveMailCc.get(i), toCcName.get(i), "UTF-8"));
		}
		// 4. Subject: �ʼ�����
		message.setSubject(subject, "UTF-8");
		// ����ı�
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(FileUtil.file2String(
				new File(Config.getCodePath(Config.EMAILCONTENT)), "UTF-8"),
				"text/html;charset=UTF-8");
		/*
		 * �������ʼ����ݵĴ���:
		 */
		// 9. �����������ڵ㡱
		MimeBodyPart attachment = new MimeBodyPart();
		DataHandler dh2 = new DataHandler(new FileDataSource(
				Config.getDownLoadPath() + zipName + ".zip")); // ��ȡ�����ļ�
		attachment.setDataHandler(dh2); // ������������ӵ����ڵ㡱
		attachment.setFileName(MimeUtility.encodeText(dh2.getName())); // ���ø������ļ�������Ҫ���룩
		// 10. ���ã��ı�+ͼƬ���� ���� �Ĺ�ϵ���ϳ�һ����Ļ�ϡ��ڵ㡱 / Multipart ��
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(htmlPart);
		mm.addBodyPart(attachment); // ����ж�����������Դ������������
		mm.setSubType("mixed"); // ��Ϲ�ϵ
		// 11. ���������ʼ��Ĺ�ϵ�������յĻ�ϡ��ڵ㡱��Ϊ�ʼ���������ӵ��ʼ�����
		message.setContent(mm);
		// 12. ���÷���ʱ��
		message.setSentDate(new Date());
		// message.setText(, "utf-8"));// �����ı�����
		// 13. �����������������
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
		// 1. ������������, ���������ʼ��������Ĳ�������
		Properties props = new Properties(); // ��������
		props.setProperty("mail.transport.protocol", "smtp"); // ʹ�õ�Э�飨JavaMail�淶Ҫ��
		props.setProperty("mail.smtp.host", emailSmtpHost); // �����˵������ SMTP
															// ��������ַ
		props.setProperty("mail.smtp.auth", "true"); // ��Ҫ������֤
		// ���� SSL ����, �Լ�����ϸ�ķ��Ͳ����뿴��һƪ: ���� JavaMail �� Java �ʼ����ͣ����ʼ�����
		// 2. �������ô����Ự����, ���ں��ʼ�����������
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true); // ����Ϊdebugģʽ, ���Բ鿴��ϸ�ķ��� log
		// 3. ����һ���ʼ�
		MimeMessage message = createMimeMessage(session, emailAcountsend,
				emailTo, emailCc, subject, fromName, toName, toCcName, zipName);
		// Ҳ���Ա��ֵ����ز鿴
		// message.writeTo(file_out_put_stream);
		// 4. ���� Session ��ȡ�ʼ��������
		Transport transport = session.getTransport();
		// 5. ʹ�� �����˺� �� ���� �����ʼ�������
		// ������֤����������� message �еķ���������һ�£����򱨴�
		transport.connect(emailAcountsend, emailpaw);
		// 6. �����ʼ�, �������е��ռ���ַ, message.getAllRecipients() ��ȡ�������ڴ����ʼ�����ʱ��ӵ������ռ���,
		// ������, ������
		transport.sendMessage(message, message.getAllRecipients());
		// 7. �ر�����
		transport.close();

	}
}
