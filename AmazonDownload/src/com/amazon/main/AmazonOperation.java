package com.amazon.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.amazon.main.config.Config;
import com.amazon.main.util.DateUtil;
import com.amazon.main.util.FTPUtil;
import com.amazon.main.util.FileUtil;
import com.amazon.main.util.ZipCompressorByAnt;

public class AmazonOperation {
	private WebDriver driver;
	private String baseUrl;
	private ArrayList<String> screenShotList = null;
	private ArrayList<Integer> screenShotlocation = null;
	private ArrayList<String> siteList = new ArrayList<String>();

	public void init() {
		String mime_types = "application/pdf,application/vnd.adobe.xfdf,application/vnd.fdf,application/vnd.adobe.xdp+xml";
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		ProfilesIni pi = new ProfilesIni();
		firefoxProfile = pi.getProfile("default");// ����Ǵ�����
		// ��firefox�����,û�����ͻ��´�һ�������(û�в��,Ĭ�ϵ�����)
		firefoxProfile.setPreference("browser.download.dir",
				Config.getDownLoadTemporaryPath()); // ����ָ�����������ļ���Ŀ¼��
		firefoxProfile.setPreference("browser.download.folderList", 2); // ���ó�0
																		// �������ص������Ĭ������·�������ó�2
																		// ����Ա��浽ָ��Ŀ¼��
		firefoxProfile.setPreference(
				"browser.download.manager.showWhenStarting", false); // �Ƿ���ʾ��ʼ��TureΪ��ʾ��FlaseΪ����ʾ��

		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				mime_types + ",text/csv" + ",application/zip");
		// ָ��Ҫ����ҳ���Content-type ֵ����application/octet-stream��Ϊ�ļ�������
		firefoxProfile.setPreference(
				"plugin.disable_full_page_plugin_for_types", mime_types);
		firefoxProfile.setPreference("pdfjs.disabled", true);
		System.setProperty("webdriver.firefox.marionette",
				Config.getCodePath("geckodriver.exe"));

		driver = new FirefoxDriver(firefoxProfile);
		baseUrl = Config.getBaseUrl();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void closeFirefox() {
		driver.quit();
	}

	public void clearCookies() {
		driver.manage().deleteAllCookies();
	}

	public void logout() {
		try {

			Actions act = new Actions(driver);
			WebElement Settings = driver.findElement(By.linkText("Settings"));
			WebElement Logout = driver.findElement(By.linkText("Logout"));
			act.click(Settings).perform();
			act.moveToElement(Logout).click();
			System.out.println("------logout--------");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @param paypalUser
	 *            paypal�û���
	 * @param paypalPassword
	 *            paypal����
	 * @param emailSmtpHost
	 *            SMTP server
	 * @param emailAcountsend
	 *            ���������˻�
	 * @param emailpaw
	 *            ������������
	 * @param emailTo
	 *            Ҫ���͵��˻�
	 * @param emailCc
	 *            ���͵��˻�
	 * @param subject
	 *            �ʼ�����
	 * @param fromName
	 *            ����������
	 * @param toName
	 *            �ռ�������
	 * @param toCcName
	 *            ����������
	 * @throws InterruptedException
	 */
	public void testRr(String paypalUser, String paypalPassword,
			String emailSmtpHost, String emailAcountsend, String emailpaw,
			String emailTo, List<String> emailCc, String subject,
			String fromName, String toName, List<String> toCcName)
			throws InterruptedException {
		String filename = DateUtil.getDate()[0] + DateUtil.lastMonthNum()
				+ DateUtil.getDate()[2] + "_" + paypalUser;
		login(paypalUser, paypalPassword);

		if (siteList.size() > 0) {
			for (int i = 0; i < siteList.size(); i++) {
				while (!isElementPresent(By.id("sc-navtabs"))) {
					driver.navigate().refresh();
					Thread.sleep(3 * 1000);
				}
				new Select(driver.findElement(By
						.id("sc-mkt-picker-switcher-select")))
						.selectByVisibleText(siteList.get(i));
				Thread.sleep(3 * 1000);
				if (isElementPresent(By.id("sc-lang-switcher-header-select"))) {
					new Select(driver.findElement(By
							.id("sc-lang-switcher-header-select")))
							.selectByVisibleText("English");
				}
				if (isElementPresent(By.className("a-icon a-icon-close"))) {
					driver.findElement(By.className("a-icon a-icon-close"))
							.click();
				}
				Thread.sleep(5 * 1000);
				amazonDownload(siteList.get(i), paypalUser);
			}
		} else {
			if (isElementPresent(By.id("sc-lang-switcher-header-select"))) {
				new Select(driver.findElement(By
						.id("sc-lang-switcher-header-select")))
						.selectByVisibleText("English");
			}
			if (isElementPresent(By.className("a-icon a-icon-close"))) {
				driver.findElement(By.className("a-icon a-icon-close")).click();
			}
			String site = driver.findElement(
					By.className("sc-mkt-picker-switcher-txt")).getText();
			amazonDownload(site, paypalUser);
		}
		// zipAndEmail(emailSmtpHost, emailAcountsend, emailpaw, emailTo,
		// emailCc,
		// subject, fromName, toName, toCcName, filename);
		// Thread.sleep(3 * 1000);
		// boolean testUpload = FTPUtil.testUpload("120.77.2.209", "financeftp",
		// "ftp#666!", Config.getDownLoadPath() + filename + ".zip",
		// "/amazon", filename + ".zip");

		logout();
	}

	private void amazonDownload(String site, String user)
			throws InterruptedException {

		// while (!isElementPresent(By.id("sc-mkt-picker-switcher-select"))) {
		// driver.navigate().refresh();
		// Thread.sleep(3 * 1000);
		// }
		Thread.sleep(10 * 1000);
		// �㿪reports��������ƶ���payments�ϲ�ѡ��
		Actions act = new Actions(driver);
		// WebElement dropDown = driver.findElement(By.linkText("REPORTS"));
		// WebElement Payments = driver.findElement(By.linkText("Payments"));
		// act.click(dropDown).perform();
		// act.moveToElement(Payments).click();
		driver.findElement(By.linkText("REPORTS")).click();
		Thread.sleep(10 * 1000);
		boolean dateRangReport = dateRangReport(site, user);
		if (dateRangReport) {
			return;
		}
		statementView(site, user);
		allStatement(site, user);

		FTPUtil.testUploadMuti("120.77.2.209", "financeftp", "ftp#666!",
				Config.getDownLoadPath(), user,
				site.substring(4, site.length()));
		Thread.sleep(5 * 1000);
		System.out.println("------deleteall---");
		FileUtil.deleteAllFile(Config.getDownLoadPath());
		Thread.sleep(5 * 1000);
	}

	private void zipAndEmail(String emailSmtpHost, String emailAcountsend,
			String emailpaw, String emailTo, List<String> emailCc,
			String subject, String fromName, String toName,
			List<String> toCcName, String filename) throws InterruptedException {
		ZipCompressorByAnt zca = new ZipCompressorByAnt(
				Config.getDownLoadPath() + filename + ".zip");
		zca.compressExe(Config.getDownLoadPath());
		Thread.sleep(5 * 1000);
		// try {
		// new SendEmail().sendEmail(emailSmtpHost, emailAcountsend, emailpaw,
		// emailTo, emailCc, subject, fromName, toName, toCcName,
		// filename);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Thread.sleep(3 * 1000);

	}

	private void login(String user, String paw) throws InterruptedException {
		driver.get(baseUrl);
		while (driver.getTitle().contains("����")) {
			System.out.println("--------re");
			Thread.sleep(5 * 1000);
			login(user, paw);
		}
		driver.findElement(By.id("ap_email")).clear();
		driver.findElement(By.id("ap_email")).sendKeys(user);
		driver.findElement(By.id("ap_password")).clear();
		driver.findElement(By.id("ap_password")).sendKeys(paw);
		driver.findElement(By.id("signInSubmit")).click();
		Thread.sleep(10 * 000);
		System.out.println("----title--" + driver.getTitle());
		while (driver.getTitle().contains("����")) {
			System.out.println("--------re");
			Thread.sleep(5 * 1000);
			// driver.navigate().refresh();
			login(user, paw);
		}
		(new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.id("sc-navtabs")).isDisplayed();
			}
		});

		while (!isElementPresent(By.id("sc-navtabs"))) {
			driver.navigate().refresh();
			Thread.sleep(3 * 1000);
		}
		if (isElementPresent(By.id("sc-mkt-picker-switcher-select"))) {
			Select statementSelect = new Select(driver.findElement(By
					.id("sc-mkt-picker-switcher-select")));
			List<WebElement> options = statementSelect.getOptions();
			for (int i = 0; i < options.size(); i++) {
				siteList.add(options.get(i).getText());
				System.out.println("----sitelist----"
						+ options.get(i).getText());
			}
		}
	}

	private void allStatement(String site, String user)
			throws InterruptedException {
		driver.findElement(By.linkText("All Statements")).click();
		Thread.sleep(5 * 1000);
		for (int i = 0; i < screenShotlocation.size(); i++) {
			allStatementDownload(i, site, user);
			Thread.sleep(3 * 1000);
		}
	}

	private void allStatementDownload(int index, String site, String user)
			throws InterruptedException {
		// ��ɾ����ʱ�ļ������������
		FileUtil.deleteAllFile(Config.getDownLoadTemporaryPath());
		Thread.sleep(1000);
		driver.findElement(
				By.xpath(".//*[@id='content-main-entities']/table[2]/tbody/tr["
						+ screenShotlocation.get(index)
						+ "]/td[8]/div[2]/a/span/span")).click();
		File tmpFile = new File(Config.getDownLoadTemporaryPath());
		File[] files = tmpFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().endsWith(".part")) {
					while (FileUtil.isFileWriting(tmp.getAbsolutePath())) {
						Thread.sleep(1000);
						System.out.println("5-----while---");
					}
				}
			}
		}
		Thread.sleep(3000);
		if (FileUtil.fileNumContainStr(Config.getDownLoadTemporaryPath(),
				".txt") > 0) {
			while (FileUtil.fileSizeNum(Config.getDownLoadTemporaryPath(),
					".txt") == 0) {
				FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), ".txt");
				FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), ".part");
				Thread.sleep(3 * 1000);
				driver.findElement(
						By.xpath(".//*[@id='content-main-entities']/table[2]/tbody/tr["
								+ screenShotlocation.get(index)
								+ "]/td[8]/div[2]/a/span/span")).click();
				File tmpFile2 = new File(Config.getDownLoadTemporaryPath());
				File[] files2 = tmpFile2.listFiles();
				for (int i = 0; i < files2.length; i++) {
					File tmp = files2[i];
					if (tmp.exists() && tmp.isFile()) {
						if (tmp.toString().endsWith(".part")) {
							while (FileUtil
									.isFileWriting(tmp.getAbsolutePath())) {
								Thread.sleep(1000);
								System.out.println("6-----while---");
							}
						}
					}
				}
				FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), ".part");
				System.out.println("7-----while---");
			}

			String tofile = Config.getDownLoadPath()
					+ site.substring(4, site.length()) + "_"
					+ screenShotList.get(index) + "_" + user + ".xls";

			System.out.println("afterName--" + tofile);
			Thread.sleep(5 * 1000);
			File tmpFile3 = new File(Config.getDownLoadTemporaryPath());
			File[] files3 = tmpFile3.listFiles();
			for (int i = 0; i < files3.length; i++) {
				File tmp = files3[i];
				if (tmp.exists() && tmp.isFile()) {
					if (tmp.toString().endsWith(".txt")) {
						boolean copyFile = FileUtil.copyFile(tmp, new File(
								tofile));
						System.out.println("---copyFile-" + copyFile);
						Thread.sleep(1000);
					}
				}
			}
			System.out.println("ho");
			Thread.sleep(10 * 000);
		}
		Thread.sleep(5 * 1000);
	}

	private void statementView(String site, String user)
			throws InterruptedException {
		driver.findElement(By.linkText("Statement View")).click();
		Thread.sleep(5 * 1000);
		screenShotList = new ArrayList<String>();
		screenShotlocation = new ArrayList<Integer>();
		Select statementSelect = new Select(driver.findElement(By.id("groups")));
		List<WebElement> options = statementSelect.getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().contains(DateUtil.lastMonth())
					&& options.get(i).getText()
							.contains(DateUtil.currencyDate()[0])
					&& !options.get(i).getText().contains("Open")) {
				screenShotList.add(options.get(i).getText());
				screenShotlocation.add(i + 2);
			}
		}
		for (int i = 0; i < screenShotList.size(); i++) {
			selectAndScreenShot(screenShotList.get(i), site, user);
			Thread.sleep(5 * 1000);
		}
		Thread.sleep(10 * 1000);
	}

	private void selectAndScreenShot(String select, String site, String user)
			throws InterruptedException {
		new Select(driver.findElement(By.id("groups")))
				.selectByVisibleText(select);
		Thread.sleep(10 * 1000);
		// snapShot((TakesScreenshot) driver, select + ".png");
		try {
			snapShotLocation(By.id("a-page"), site.substring(4, site.length())
					+ "_" + select + "_" + user + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] takeScreenshot(WebDriver driver) throws IOException {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		return ((TakesScreenshot) augmentedDriver)
				.getScreenshotAs(OutputType.BYTES);
		// TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		// return takesScreenshot.getScreenshotAs(OutputType.BYTES);
	}

	public BufferedImage createElementImage(WebDriver driver,
			WebElement webElement) throws IOException {
		// ���webElement��λ�úʹ�С��
		Point location = webElement.getLocation();
		Dimension size = webElement.getSize();
		// ����ȫ����ͼ��
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(
				takeScreenshot(driver)));
		// ��ȡwebElement����λ�õ���ͼ��
		BufferedImage croppedImage = originalImage.getSubimage(location.getX(),
				location.getY(), size.getWidth(), size.getHeight());
		return croppedImage;
	}

	/**
	 * ��λ��ͼ
	 * 
	 * @param by
	 * @throws IOException
	 */
	private void snapShotLocation(By by, String filename) throws IOException {

		WebElement element = driver.findElement(by);
		BufferedImage elementImage = createElementImage(driver, element);
		ImageIO.write(elementImage, "png", new File(Config.getDownLoadPath()
				+ filename));
	}

	/**
	 * ��ͼ,ȫ��
	 * 
	 * @param drivername
	 * @param filename
	 */
	private void snapShot(TakesScreenshot drivername, String filename) {

		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		try {

			FileUtils.copyFile(scrFile, new File(Config.getDownLoadPath()
					+ filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished");
		}
	}

	private boolean dateRangReport(String site, String user)
			throws InterruptedException {
		driver.findElement(By.linkText("Date Range Reports")).click();
		Thread.sleep(5 * 1000);
		if (!driver.findElement(By.id("reportsTable")).isDisplayed()) {
			System.out.println("--return--");
			return true;
		}
		String[] id = { "drrReportTypeRadioSummary",
				"drrReportTypeRadioTransaction" };
		for (int i = 0; i < id.length; i++) {
			generateReport(id[i]);
			Thread.sleep(3 * 1000);
		}
		Thread.sleep(10 * 1000);
		if (driver.findElement(By.id("mt-header-count-value")).getText()
				.equals("0")) {
			System.out.println("--report count-"
					+ driver.findElement(By.id("mt-header-count-value"))
							.getText());
			return true;
		}
		dateRangClickDownload(site, user);
		Thread.sleep(5 * 1000);
		return false;
	}

	private void dateRangClickDownload(String site, String user)
			throws InterruptedException {

		while (driver.findElement(By.xpath(".//*[@id='0-ddrAction']"))
				.getText().contains("In Progress")) {
			System.out.println("-----action---"
					+ driver.findElement(By.xpath(".//*[@id='0-ddrAction']"))
							.getText());
			driver.findElement(By.xpath(".//*[@id='0-ddrAction']/div/a[1]"))
					.click();
			Thread.sleep(10 * 1000);
		}
		while (driver.findElement(By.xpath(".//*[@id='1-ddrAction']"))
				.getText().contains("In Progress")) {
			System.out.println("-----action---"
					+ driver.findElement(By.xpath(".//*[@id='1-ddrAction']"))
							.getText());
			driver.findElement(By.xpath(".//*[@id='1-ddrAction']/div/a[1]"))
					.click();
			Thread.sleep(10 * 1000);
		}
		Thread.sleep(5 * 1000);
		dateRangDownload(site, user);

	}

	private void dateRangDownload(String site, String user)
			throws InterruptedException {
		File downFile = new File(Config.getDownLoadPath());
		if (!downFile.exists() && !downFile.isDirectory()) {
			downFile.mkdirs();
		}
		File downTemporaryFile = new File(Config.getDownLoadTemporaryPath());
		if (!downTemporaryFile.exists() && !downTemporaryFile.isDirectory()) {
			downTemporaryFile.mkdirs();
		}
		dateRangeDownloadFile(0, Config.MONTHLYTRANSACTION, ".csv");
		if (FileUtil.fileNumContainStr(Config.getDownLoadTemporaryPath(),
				".csv") > 0) {
			copyPDFOrXLS(".csv", Config.MONTHLYTRANSACTION, 0, site, user);
		}
		Thread.sleep(10 * 1000);
		dateRangeDownloadFile(1, Config.MONTHLYSUMMARY, ".pdf");
		if (FileUtil.fileNumContainStr(Config.getDownLoadTemporaryPath(),
				".pdf") > 0) {

			copyPDFOrXLS(".pdf", Config.MONTHLYSUMMARY, 1, site, user);
		}
	}

	private void dateRangeDownloadFile(int i, String name, String fileType)
			throws InterruptedException {
		// ��ɾ����ʱ�ļ������������
		FileUtil.deleteAllFile(Config.getDownLoadTemporaryPath());
		if (driver.findElement(By.xpath(".//*[@id='" + i + "-ddrAction']"))
				.findElement(By.xpath(".//*[@id='downloadButton-announce']"))
				.getText().equals("Download")) {

			driver.findElement(By.xpath(".//*[@id='" + i + "-ddrAction']"))
					.findElement(
							By.xpath(".//*[@id='downloadButton-announce']"))
					.click();
			Thread.sleep(5 * 1000);
			while (FileUtil.isFileWriting(Config.getDownLoadTemporaryPath()
					+ DateUtil.currencyDate()[0] + DateUtil.lastMonth() + name
					+ fileType + ".part")) {
				Thread.sleep(1000);
				System.out.println("1-----while---");
			}
			Thread.sleep(3000);

		}
	}

	/**
	 * �����ļ��������ƶ�
	 * 
	 * @param fileType
	 * @throws InterruptedException
	 */
	private void copyPDFOrXLS(String fileType, String name, int i, String site,
			String user) throws InterruptedException {
		String fileName = DateUtil.currencyDate()[0] + DateUtil.lastMonth()
				+ name + fileType;
		while (FileUtil
				.fileSizeNum(Config.getDownLoadTemporaryPath(), fileType) == 0) {
			FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), fileType);
			FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), ".part");
			driver.findElement(By.xpath(".//*[@id='" + i + "-ddrAction']"))
					.findElement(
							By.xpath(".//*[@id='downloadButton-announce']"))
					.click();
			Thread.sleep(10000);

			while (FileUtil.isFileWriting(Config.getDownLoadTemporaryPath()
					+ fileName + ".part")) {
				Thread.sleep(1000);
				System.out.println("2-----while---");
			}
			FileUtil.deleteFile(Config.getDownLoadTemporaryPath(), ".part");
			System.out.println("3-----while---");
		}
		String beforeName = Config.getDownLoadTemporaryPath() + fileName;
		String afterName = Config.getDownLoadTemporaryPath() + Config.AMAZONCOM
				+ "(" + i + ")" + DateUtil.getDate()[0]
				+ DateUtil.lastMonthNum() + fileType;
		if (fileType.equals(".csv")) {
			fileType = ".xls";
		}
		String tofile = Config.getDownLoadPath()
				+ site.substring(4, site.length()) + "_"
				+ DateUtil.getDate()[0] + DateUtil.lastMonthNum() + "_" + user
				+ "_" + name + fileType;
		System.out.println("beforeName--" + beforeName);
		System.out.println("afterName--" + afterName);
		Thread.sleep(2000);
		boolean copyFile = FileUtil.copyFile(new File(beforeName), new File(
				tofile));
		System.out.println("---copyFile-" + copyFile);
		Thread.sleep(1000);

		System.out.println("ho");
		Thread.sleep(10000);
	}

	private void generateReport(String id) throws InterruptedException {
		driver.findElement(
				By.xpath(".//*[@id='drrGenerateReportButton']/span/input"))
				.click();

		(new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("a-modal-scroller"))
						.isDisplayed();
			}
		});
		WebElement link = driver.findElement(By.className("a-modal-scroller"))
				.findElement(By.id(id));
		((JavascriptExecutor) driver).executeScript("$(arguments[0]).click()",
				link);
		Thread.sleep(2 * 1000);
		WebElement link2 = driver
				.findElement(By.className("a-modal-scroller"))
				.findElement(
						By.xpath(".//*[@id='drrGenerateReportsGenerateButton']/span/input"));
		((JavascriptExecutor) driver).executeScript("$(arguments[0]).click()",
				link2);

	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
