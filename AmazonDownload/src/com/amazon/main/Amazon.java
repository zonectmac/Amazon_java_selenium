package com.amazon.main;

import java.util.List;

import com.amazon.main.config.Config;

public class Amazon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AmazonOperation ao = new AmazonOperation();
		ao.init();
		int exception = 0;
		int exceptionNum = 0;
		List<String> user = Config.getPaypalUser();
		for (int i = 0; i < user.size(); i++) {
			do {
				try {
					ao.clearCookies();
					exception = 0;
					startDownload(ao, i, user);
				} catch (Exception e) {
					System.out.println("exception------" + e.getMessage());
					exception = 1;
					exceptionNum++;
					ao.logout();
					e.printStackTrace();
				}
			} while (exception == 1 && exceptionNum < 1000);
		}
		ao.closeFirefox();
	}

	private static void startDownload(AmazonOperation ao, int i,
			List<String> user) throws InterruptedException {
		ao.testRr(user.get(i), Config.getPaypalPassword().get(i),
				Config.smtpServer(), Config.emailFromSend(),
				Config.emailPassword(), Config.emailTo(), Config.emailCc(),
				Config.emailSubject(), Config.eamilFromName(),
				Config.emailToName(), Config.emailToCcName());

	}

}
