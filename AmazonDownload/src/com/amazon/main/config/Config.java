package com.amazon.main.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.main.util.FileUtil;
import com.amazon.main.util.StringUtil;

public class Config {

	public static final String CONFIG = "config.txt";
	public static final String EMAILCONTENT = "emailContent.txt";
	public static final String MONTHLYSUMMARY = "MonthlySummary";
	public static final String MONTHLYTRANSACTION = "MonthlyTransaction";
	public static final String AMAZONCOM = "Amazon.com";

	// public static final String FILECODE = "paypal/";

	private static JSONObject StringJson() {
		JSONObject configObject = null;
		String string = FileUtil.file2String(new File(getCodePath(CONFIG)),
				"utf-8");
		String replaceBlank = StringUtil.replaceBlank(string);
		try {

			configObject = new JSONObject(replaceBlank);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return configObject;
	}

	public static List<String> getPaypalUser() {
		List<String> user = new ArrayList<String>();
		try {
			JSONArray array = StringJson().getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				user.add(array.getJSONObject(i).getString("User"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;

	}

	public static List<String> getPaypalPassword() {
		List<String> paw = new ArrayList<String>();
		try {
			JSONArray array = StringJson().getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				paw.add(array.getJSONObject(i).getString("Password"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paw;

	}

	public static String eamilFromName() {
		String string = null;
		try {
			string = StringJson().getString("eamilFromName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static String emailToName() {
		String string = null;
		try {
			string = StringJson().getString("emailToName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static ArrayList<String> emailToCcName() {
		ArrayList<String> ef = new ArrayList<String>();
		try {
			JSONArray array = StringJson().getJSONArray("emailToCcName");
			for (int i = 0; i < array.length(); i++) {
				String string = array.getString(i);
				ef.add(string);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ef;

	}

	public static String emailTo() {
		String string = null;
		try {
			string = StringJson().getString("emailTo");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static ArrayList<String> emailCc() {
		ArrayList<String> ec = new ArrayList<String>();
		try {
			JSONArray array = StringJson().getJSONArray("emailCc");
			for (int i = 0; i < array.length(); i++) {
				String string = array.getString(i);
				ec.add(string);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ec;

	}

	public static String emailSubject() {
		String string = null;
		try {
			string = StringJson().getString("emailSubject");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static String emailBody() {
		String string = null;
		try {
			string = StringJson().getString("emailBody");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static String smtpServer() {
		String string = null;
		try {
			string = StringJson().getString("smtpServer");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static String emailFromSend() {
		String string = null;
		try {
			string = StringJson().getString("emailFromSend");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	public static String emailPassword() {
		String string = null;
		try {
			string = StringJson().getString("emailPassword");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	/**
	 * 相对路径获取绝对路径
	 * 
	 * @param path相对路径
	 *            ，如：NewTest/Download.CSV
	 * @return
	 */
	public static String getCodePath(String path) {

		return new File(path).getAbsolutePath();

	}

	public static String getDownLoadTemporaryPath() {
		String string = null;
		try {
			string = StringJson().getString("downLoadTemporaryPath");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string.replace("\\", "\\\\");

	}

	public static String getDownLoadPath() {
		String string = null;
		try {
			string = StringJson().getString("downLoadPath");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string.replace("\\", "\\\\");

	}

	public static String getBaseUrl() {
		String string = null;
		try {
			string = StringJson().getString("baseUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;

	}

	// public static String getDownLoadPathFinal() {
	// String string = null;
	// try {
	// string = StringJson().getString("downLoadPathFinal");
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return string.replace("\\", "\\\\");
	//
	// }

	// public static ConfigBean getConfig() {
	// String string = FileUtil.file2String(new File(getCodePath(CONFIG)),
	// "utf-8");
	// String replaceBlank = StringUtil.replaceBlank(string);
	// ConfigBean cb = new Gson().fromJson(replaceBlank, ConfigBean.class);
	// return cb;
	// }
}
