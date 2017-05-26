package com.amazon.main.util;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.channels.FileChannel;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class FileUtil {
	/**
	 * ͳ���ļ��º�׺��Ϊָ�����ļ�����
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 * @return
	 */
	public static int fileNum(String path, String end) {
		int num = 0;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().endsWith(end)) {
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * ͳ���ļ��°����ַ�����Ϊָ�����ļ�����
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 * @return
	 */
	public static int fileNumContainStr(String path, String str) {
		int num = 0;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().contains(str)) {
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * �ж��ļ����Ƿ����ļ���С
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 * @return
	 */
	public static boolean fileSize(String path, String end) {

		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().endsWith(end)) {
					FileChannel fc = null;
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(tmp);
						fc = fis.getChannel();
						System.out.println("---fc--" + fc.size());
						if (fc.size() == 0) {
							return true;
						}

					} catch (FileNotFoundException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						System.out.println(e.getMessage());
					} finally {
						if (null != fc) {
							try {
								fis.close();
								fc.close();
							} catch (IOException e) {
								System.out.println(e.getMessage());
							}
						}
					}
				}
			} else {
				System.out.println("file doesn't exist or is not a file");
			}
		}
		return false;
	}

	/**
	 * �ж��ļ����Ƿ����ļ���С
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 * @return
	 */
	public static long fileSizeNum(String path, String end) {
		long size = 0;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().endsWith(end)) {
					FileChannel fc = null;
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(tmp);
						fc = fis.getChannel();
						size = fc.size();

					} catch (FileNotFoundException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						System.out.println(e.getMessage());
					} finally {
						if (null != fc) {
							try {
								fis.close();
								fc.close();
							} catch (IOException e) {
								System.out.println(e.getMessage());
							}
						}
					}
				}
			} else {
				size = 0;
				System.out.println("file doesn't exist or is not a file");
			}
		}
		return size;
	}

	/**
	 * ɾ��ָ����׺���Ҵ�СΪ0��
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 */
	public static void deleteNoFileSize(String path, String end) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				System.out.println("oooo22224433");
				if (tmp.toString().endsWith(end)) {
					System.out.println("oooo2222");
					if (fileSizeNum(path, end) == 0) {
						tmp.delete();
						System.out.println("oooo" + fileSize(path, end));
					} else {
						System.out.println("oooo" + fileSize(path, end));
					}
				}
			}
		}
	}

	/**
	 * ɾ��ָ����׺��,���ܴ�С�ǲ���Ϊ0��
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 */
	public static void deleteFile(String path, String end) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				if (tmp.toString().endsWith(end)) {
					tmp.delete();
					System.out.println("kkk");
				}
			}
		}
	}

	/**
	 * ɾ��ָ����׺��,���ܴ�С�ǲ���Ϊ0��
	 * 
	 * @param path�ļ���·��
	 * @param end��׺��
	 */
	public static void deleteAllFile(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File tmp = files[i];
			if (tmp.exists() && tmp.isFile()) {
				tmp.delete();
			}
		}
	}

	/**
	 * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
	 * 
	 * @param dir
	 *            ��Ҫɾ�����ļ�Ŀ¼
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// �ݹ�ɾ��Ŀ¼�е���Ŀ¼��
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// Ŀ¼��ʱΪ�գ�����ɾ��
		return dir.delete();
	}

	/**
	 * IText��ȡpdf�ļ�
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getPdfFileText(String fileName) throws IOException {
		PdfReader reader = new PdfReader(fileName);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		StringBuffer buff = new StringBuffer();
		TextExtractionStrategy strategy;
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			strategy = parser.processContent(i,
					new SimpleTextExtractionStrategy());
			buff.append(strategy.getResultantText());
		}
		reader.close();
		return buff.toString();
	}

	/**
	 * ���ļ���������
	 * 
	 * @param file
	 * @param toFile
	 */
	public static void renameFile(String file, String toFile) {

		File toBeRenamed = new File(file);
		// ���Ҫ���������ļ��Ƿ���ڣ��Ƿ����ļ�
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

			System.out.println("File does not exist: " + file);
			return;
		}

		File newFile = new File(toFile);

		// �޸��ļ���
		if (toBeRenamed.renameTo(newFile)) {
			System.out.println("File has been renamed.");
		} else {
			System.out.println("Error renmaing file");
		}

	}

	/**
	 * �ı��ļ�ת��Ϊָ��������ַ���
	 * 
	 * @param file
	 *            �ı��ļ�
	 * @param encoding
	 *            ��������
	 * @return ת������ַ���
	 * @throws IOException
	 */
	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// ��������д�������
			char[] buffer = new char[1024];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		// ����ת�����
		if (writer != null)
			return writer.toString();
		else
			return null;
	}

	/**
	 * �����ļ�
	 * 
	 * @param srcFilePath
	 *            Դ�ļ�·��
	 * @param destFilePath
	 *            Ŀ���ļ�·��
	 * @return {@code true}: ���Ƴɹ�<br>
	 *         {@code false}: ����ʧ��
	 */
	public static boolean copyFile(String srcFilePath, String destFilePath) {
		return copyFile(new File(srcFilePath), new File(destFilePath));
	}

	/**
	 * �����ļ�
	 * 
	 * @param srcFile
	 *            Դ�ļ�
	 * @param destFile
	 *            Ŀ���ļ�
	 * @return {@code true}: ���Ƴɹ�<br>
	 *         {@code false}: ����ʧ��
	 */
	public static boolean copyFile(File srcFile, File destFile) {
		return copyOrMoveFile(srcFile, destFile, true);
	}

	/**
	 * ���ƻ��ƶ��ļ�
	 * 
	 * @param srcFile
	 *            Դ�ļ�
	 * @param destFile
	 *            Ŀ���ļ�
	 * @param isMove
	 *            �Ƿ��ƶ�
	 * @return {@code true}: ���ƻ��ƶ��ɹ�<br>
	 *         {@code false}: ���ƻ��ƶ�ʧ��
	 */
	private static boolean copyOrMoveFile(File srcFile, File destFile,
			boolean isMove) {
		if (srcFile == null || destFile == null)
			return false;
		// Դ�ļ������ڻ��߲����ļ��򷵻�false
		if (!srcFile.exists() || !srcFile.isFile())
			return false;
		// Ŀ���ļ����������ļ��򷵻�false
		if (destFile.exists() && destFile.isFile())
			return false;
		// Ŀ��Ŀ¼�����ڷ���false
		if (!createOrExistsDir(destFile.getParentFile()))
			return false;
		try {
			return writeFileFromIS(destFile, new FileInputStream(srcFile),
					false) && !(isMove && !deleteFile(srcFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @return {@code true}: ɾ���ɹ�<br>
	 *         {@code false}: ɾ��ʧ��
	 */
	public static boolean deleteFile(File file) {
		return file != null
				&& (!file.exists() || file.isFile() && file.delete());
	}

	/**
	 * ��������д���ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @param is
	 *            ������
	 * @param append
	 *            �Ƿ�׷�����ļ�ĩ
	 * @return {@code true}: д��ɹ�<br>
	 *         {@code false}: д��ʧ��
	 */
	public static boolean writeFileFromIS(File file, InputStream is,
			boolean append) {
		if (file == null || is == null)
			return false;
		if (!createOrExistsFile(file))
			return false;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file, append));
			byte data[] = new byte[1024];
			int len;
			while ((len = is.read(data, 0, 1024)) != -1) {
				os.write(data, 0, len);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeIO(is, os);
		}
	}

	/**
	 * �ر�IO
	 * 
	 * @param closeables
	 *            closeable
	 */
	public static void closeIO(Closeable... closeables) {
		if (closeables == null)
			return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �ж��ļ��Ƿ���ڣ����������ж��Ƿ񴴽��ɹ�
	 * 
	 * @param file
	 *            �ļ�
	 * @return {@code true}: ���ڻ򴴽��ɹ�<br>
	 *         {@code false}: �����ڻ򴴽�ʧ��
	 */
	public static boolean createOrExistsFile(File file) {
		if (file == null)
			return false;
		// ������ڣ����ļ��򷵻�true����Ŀ¼�򷵻�false
		if (file.exists())
			return file.isFile();
		if (!createOrExistsDir(file.getParentFile()))
			return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �ж�Ŀ¼�Ƿ���ڣ����������ж��Ƿ񴴽��ɹ�
	 * 
	 * @param file
	 *            �ļ�
	 * @return {@code true}: ���ڻ򴴽��ɹ�<br>
	 *         {@code false}: �����ڻ򴴽�ʧ��
	 */
	public static boolean createOrExistsDir(File file) {
		// ������ڣ���Ŀ¼�򷵻�true�����ļ��򷵻�false���������򷵻��Ƿ񴴽��ɹ�
		return file != null
				&& (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * �ж��ļ��Ƿ��ڱ�д��
	 * 
	 * @return
	 */
	public static boolean isFileWriting(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			if (file.renameTo(file)) {
				System.out.println("�ļ�δ������");
				return false;
			} else {
				System.out.println("�ļ���������");
				return true;
			}
		} else {
			return false;
		}

	}
}
