package com.amazon.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUtil {
	/**
	 * FTP�ϴ������ļ�����
	 * 
	 * @param server
	 *            ftp������������120.77.2.209
	 * @param user
	 *            ftp�û���
	 * @param paw
	 *            ftp����
	 * @param localFile
	 *            Ҫ�ϴ��ı����ļ���
	 * @param serverPath
	 *            �ϴ���ftp������Ŀ¼����/amazon
	 * @param remoteName
	 *            �ϴ�����ļ�����
	 */
	public static boolean testUpload(String server, String user, String paw,
			String localFile, String serverPath, String remoteName) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;

		try {
			ftpClient.connect(server);
			ftpClient.login(user, paw);
			ftpClient.enterLocalPassiveMode();
			File srcFile = new File(localFile);
			fis = new FileInputStream(srcFile);
			// �����ϴ�Ŀ¼
			ftpClient.changeWorkingDirectory(serverPath);
			ftpClient.setBufferSize(1024 * 20);
			ftpClient.setControlEncoding("GBK");
			// �����ļ����ͣ������ƣ�
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean storeFile = ftpClient.storeFile(remoteName, fis);
			if (storeFile) {
				fis.close();
				System.out.println("----upload successful----");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
		return false;
	}

	/**
	 * FTP�ϴ�����ļ�����
	 */
	public static void testUploadMuti(String server, String user, String paw,
			String localFilePath, String loginUser, String serverPath) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;

		try {
			ftpClient.connect(server);
			ftpClient.login(user, paw);
			ftpClient.enterLocalPassiveMode();
			System.out.println("------serverPath------" + serverPath);
			if (!ftpClient.changeWorkingDirectory("/amazon/"
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum())) {// ������ܽ���dir�£�˵����Ŀ¼�����ڣ�
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum())) {
					System.out.println("�����ļ�Ŀ¼��" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "�� ʧ�ܣ�");
				}
			}
			if (!ftpClient.changeWorkingDirectory("/amazon/"
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum() + "/"
					+ loginUser)) {// ������ܽ���dir�£�˵����Ŀ¼�����ڣ�
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum() + "/" + loginUser)) {
					System.out.println("�����ļ�Ŀ¼��" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "�� ʧ�ܣ�");
				}
			}
			if (!ftpClient.changeWorkingDirectory("/amazon/"
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum() + "/"
					+ loginUser + "/" + serverPath)) {// ������ܽ���dir�£�˵����Ŀ¼�����ڣ�
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum() + "/" + loginUser + "/"
						+ serverPath)) {
					System.out.println("�����ļ�Ŀ¼��" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "/" + serverPath + "�� ʧ�ܣ�");
				}
			}
			File file = new File(localFilePath);
			File[] listFiles = file.listFiles();
			int allFile = 0;
			for (File tmp : listFiles) {
				if (tmp.exists() && tmp.isFile()) {
					fis = new FileInputStream(tmp);
					// �����ϴ�Ŀ¼
					ftpClient.changeWorkingDirectory("/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "/" + serverPath);
					ftpClient.setBufferSize(1024 * 20);
					ftpClient.setControlEncoding("GBK");
					// �����ļ����ͣ������ƣ�
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
					boolean storeFile = ftpClient.storeFile(tmp.getName(), fis);
					if (storeFile) {
						fis.close();
						allFile++;
						System.out.println("----successful--" + tmp.getName());
					}
				}
			}
			if (allFile == listFiles.length) {
				System.out.println("------uploadall-----");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	}
}
