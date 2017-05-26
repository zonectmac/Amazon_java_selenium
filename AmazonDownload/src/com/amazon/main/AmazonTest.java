package com.amazon.main;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class AmazonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		init();

	}

	public static void init() {
		File file = new File("C:\\FirefoxProfilesBackup\\g64uw3d7.default"); // profiles�ļ�Ŀ¼���������Ƿ��ڹ���Ŀ¼�µ�files�ļ�����
		FirefoxProfile firefoxProfile = new FirefoxProfile(file);

		System.setProperty("webdriver.firefox.marionette",
				"C:\\AmazonDownload\\Amazon\\geckodriver.exe");

		WebDriver driver = new FirefoxDriver(firefoxProfile);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://sellercentral.amazon.com");
	}
}
