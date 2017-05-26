package com.amazon.main.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipCompressorByAnt {
	private File zipFile;

	/**
	 * ѹ���ļ����캯��
	 * 
	 * @param pathName
	 *            ����ѹ�����ɵ�ѹ���ļ���Ŀ¼+ѹ���ļ���.zip
	 */
	public ZipCompressorByAnt(String finalFile) {
		zipFile = new File(finalFile);
	}

	/**
	 * ִ��ѹ������
	 * 
	 * @param srcPathName
	 *            ��Ҫ��ѹ�����ļ�/�ļ���
	 */
	public void compressExe(String srcPathName) {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists()) {
			throw new RuntimeException(srcPathName + "�����ڣ�");
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// fileSet.setIncludes("**/*.java"); //������Щ�ļ����ļ���
		// eg:zip.setIncludes("*.java");
		// fileSet.setExcludes(...); //�ų���Щ�ļ����ļ���
		zip.addFileset(fileSet);
		zip.execute();
	}
}
