package com.amazon.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUtil {
	/**
	 * FTP上传单个文件测试
	 * 
	 * @param server
	 *            ftp服务器，例如120.77.2.209
	 * @param user
	 *            ftp用户名
	 * @param paw
	 *            ftp密码
	 * @param localFile
	 *            要上传的本地文件夹
	 * @param serverPath
	 *            上传的ftp服务器目录，如/amazon
	 * @param remoteName
	 *            上传后的文件名字
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
			// 设置上传目录
			ftpClient.changeWorkingDirectory(serverPath);
			ftpClient.setBufferSize(1024 * 20);
			ftpClient.setControlEncoding("GBK");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean storeFile = ftpClient.storeFile(remoteName, fis);
			if (storeFile) {
				fis.close();
				System.out.println("----upload successful----");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
		return false;
	}

	/**
	 * FTP上传多个文件测试
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
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum())) {// 如果不能进入dir下，说明此目录不存在！
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum())) {
					System.out.println("创建文件目录【" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "】 失败！");
				}
			}
			if (!ftpClient.changeWorkingDirectory("/amazon/"
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum() + "/"
					+ loginUser)) {// 如果不能进入dir下，说明此目录不存在！
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum() + "/" + loginUser)) {
					System.out.println("创建文件目录【" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "】 失败！");
				}
			}
			if (!ftpClient.changeWorkingDirectory("/amazon/"
					+ DateUtil.getDate()[0] + DateUtil.lastMonthNum() + "/"
					+ loginUser + "/" + serverPath)) {// 如果不能进入dir下，说明此目录不存在！
				if (!ftpClient.makeDirectory("/amazon/" + DateUtil.getDate()[0]
						+ DateUtil.lastMonthNum() + "/" + loginUser + "/"
						+ serverPath)) {
					System.out.println("创建文件目录【" + "/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "/" + serverPath + "】 失败！");
				}
			}
			File file = new File(localFilePath);
			File[] listFiles = file.listFiles();
			int allFile = 0;
			for (File tmp : listFiles) {
				if (tmp.exists() && tmp.isFile()) {
					fis = new FileInputStream(tmp);
					// 设置上传目录
					ftpClient.changeWorkingDirectory("/amazon/"
							+ DateUtil.getDate()[0] + DateUtil.lastMonthNum()
							+ "/" + loginUser + "/" + serverPath);
					ftpClient.setBufferSize(1024 * 20);
					ftpClient.setControlEncoding("GBK");
					// 设置文件类型（二进制）
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
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}
}
