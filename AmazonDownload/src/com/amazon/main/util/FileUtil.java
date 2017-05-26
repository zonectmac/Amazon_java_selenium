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
	 * 统计文件下后缀名为指定的文件个数
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 统计文件下包含字符串的为指定的文件个数
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 判断文件下是否有文件大小
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 判断文件下是否有文件大小
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 删除指定后缀名且大小为0的
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 删除指定后缀名,不管大小是不是为0的
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 删除指定后缀名,不管大小是不是为0的
	 * 
	 * @param path文件夹路径
	 * @param end后缀名
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
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * IText读取pdf文件
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
	 * 对文件重新命名
	 * 
	 * @param file
	 * @param toFile
	 */
	public static void renameFile(String file, String toFile) {

		File toBeRenamed = new File(file);
		// 检查要重命名的文件是否存在，是否是文件
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

			System.out.println("File does not exist: " + file);
			return;
		}

		File newFile = new File(toFile);

		// 修改文件名
		if (toBeRenamed.renameTo(newFile)) {
			System.out.println("File has been renamed.");
		} else {
			System.out.println("Error renmaing file");
		}

	}

	/**
	 * 文本文件转换为指定编码的字符串
	 * 
	 * @param file
	 *            文本文件
	 * @param encoding
	 *            编码类型
	 * @return 转换后的字符串
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
			// 将输入流写入输出流
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
		// 返回转换结果
		if (writer != null)
			return writer.toString();
		else
			return null;
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFilePath
	 *            源文件路径
	 * @param destFilePath
	 *            目标文件路径
	 * @return {@code true}: 复制成功<br>
	 *         {@code false}: 复制失败
	 */
	public static boolean copyFile(String srcFilePath, String destFilePath) {
		return copyFile(new File(srcFilePath), new File(destFilePath));
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @return {@code true}: 复制成功<br>
	 *         {@code false}: 复制失败
	 */
	public static boolean copyFile(File srcFile, File destFile) {
		return copyOrMoveFile(srcFile, destFile, true);
	}

	/**
	 * 复制或移动文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param isMove
	 *            是否移动
	 * @return {@code true}: 复制或移动成功<br>
	 *         {@code false}: 复制或移动失败
	 */
	private static boolean copyOrMoveFile(File srcFile, File destFile,
			boolean isMove) {
		if (srcFile == null || destFile == null)
			return false;
		// 源文件不存在或者不是文件则返回false
		if (!srcFile.exists() || !srcFile.isFile())
			return false;
		// 目标文件存在且是文件则返回false
		if (destFile.exists() && destFile.isFile())
			return false;
		// 目标目录不存在返回false
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
	 * 删除文件
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 删除成功<br>
	 *         {@code false}: 删除失败
	 */
	public static boolean deleteFile(File file) {
		return file != null
				&& (!file.exists() || file.isFile() && file.delete());
	}

	/**
	 * 将输入流写入文件
	 * 
	 * @param file
	 *            文件
	 * @param is
	 *            输入流
	 * @param append
	 *            是否追加在文件末
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
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
	 * 关闭IO
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
	 * 判断文件是否存在，不存在则判断是否创建成功
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 存在或创建成功<br>
	 *         {@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsFile(File file) {
		if (file == null)
			return false;
		// 如果存在，是文件则返回true，是目录则返回false
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
	 * 判断目录是否存在，不存在则判断是否创建成功
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 存在或创建成功<br>
	 *         {@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsDir(File file) {
		// 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
		return file != null
				&& (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * 判断文件是否在被写入
	 * 
	 * @return
	 */
	public static boolean isFileWriting(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			if (file.renameTo(file)) {
				System.out.println("文件未被操作");
				return false;
			} else {
				System.out.println("文件正在下载");
				return true;
			}
		} else {
			return false;
		}

	}
}
