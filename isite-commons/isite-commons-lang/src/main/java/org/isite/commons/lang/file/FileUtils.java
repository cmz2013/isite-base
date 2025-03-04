package org.isite.commons.lang.file;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Error;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.commons.lang.utils.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileUtils {

	private static final String FAILED_CREATE_DIRECTORY = "failed to create directory: ";
	/**
	 * XML文件扩展名
	 */
	public static final String EXTENSION_XML = ".xml";
	/**
	 * Excel文件扩展名
	 */
	public static final String EXTENSION_XLSX = ".xlsx";
	/**
	 * Excel文件扩展名
	 */
	public static final String EXTENSION_XLS = ".xls";
	/**
	 * 模板文件扩展名
	 */
	public static final String EXTENSION_FTL = ".ftl";

	private FileUtils() {
	}

	/**
	 * 根据路径和时间范围查找文件列表
	 * @param path: 目录或文件
	 */
	public static List<File> find(String path, long timeFrom, long timeTo) {
		return find(new File(path), timeFrom, timeTo);
	}

	/**
	 * 根据路径和时间范围查找文件列表
	 * @param file: 目录或文件
	 */
	public static List<File> find(File file, long timeFrom, long timeTo) {
		List<File> results = new LinkedList<>();
		if (!file.exists()) {
			return results;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
                for (File item : files) {
					results.addAll(find(item, timeFrom, timeTo));
				}
			}
		} else if (file.lastModified() >= timeFrom && file.lastModified() <= timeTo) {
			results.add(file);
		}
		return results;
	}

	/**
	 * 根据路径和文件名查找文件列表
	 * @param path: 目录或文件
	 * @param name: 文件名
	 */
	public static List<File> find(String path, String name) {
		return find(new File(path), name);
	}

	/**
	 * 根据路径和文件名查找文件列表
	 * @param file: 目录或文件
	 * @param name: 文件名
	 */
	public static List<File> find(File file, String name) {
		List<File> results = new LinkedList<>();
		if (!file.exists()) {
			return results;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					results.addAll(find(item, name));
				}
			}
		} else if(file.getName().equals(name)) {
			results.add(file);
		}
		return results;
	}

	/**
	 * 根据路径和文件名查找文件
	 * @param path: 目录或文件
	 * @param name: 文件名
	 */
	public static File get(String path, String name) {
		return get(new File(path), name);
	}

	/**
	 * 根据路径和文件名查找文件
	 * @param file: 目录或文件
	 * @param name: 文件名
	 */
	public static File get(File file, String name) {
		if (!file.exists()) {
			return null;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					if (null != (file = get(item, name))) {
						return file;
					}
				}
			}
		} else if(file.getName().equals(name)) {
			return file;
		}
		return null;
	}

	/**
	 * 根据路径查询文件列表
	 * @param path: 目录或文件
	 */
	public static List<File> find(String path) {
		return find(new File(path));
	}

	/**
	 * 根据路径查询文件列表
	 */
	public static List<File> find(File file) {
		List<File> results = new LinkedList<>();
		if (!file.exists()) {
			return results;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					results.addAll(find(item));
				}
			}
		} else {
			results.add(file);
		}
		return results;
	}

	/**
	 * 统计文件个数
	 * @param path: 文件或目录
	 */
	public static int count(String path) {
		return count(new File(path));
	}

	/**
	 * 统计文件个数
	 */
	public static int count(File file) {
		int count = Constants.ZERO;
		if (!file.exists()) {
			return count;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					count += count(item);
				}
			}
		} else {
			count++;
		}
		return count;
	}

	/**
	 * 统计文件数
	 * @param path: 文件或目录
	 */
	public static int count(String path, long timeFrom, long timeTo) {
		return count(new File(path), timeFrom, timeTo);
	}

	/**
	 * 统计文件数
	 */
	public static int count(File file, long timeFrom, long timeTo) {
		int count = Constants.ZERO;
		if (!file.exists()) {
			return count;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					count += count(item);
				}
			}
		} else if (file.lastModified() >= timeFrom && file.lastModified() <= timeTo) {
			count++;
		}
		return count;
	}

	/**
	 * 新建文件
	 * @param pathname 文件全路径
	 * @param contents 文件内容
	 */
	public static File save(String pathname, String... contents) throws IOException {
		File file = new File(pathname);
		if (!file.getParentFile().exists()) {
			Assert.isTrue(file.getParentFile().mkdirs(), new Error(ResultStatus.EXPECTATION_FAILED.getCode(),
					FAILED_CREATE_DIRECTORY + file.getParentFile().getAbsolutePath()));
		}
		try (PrintWriter writer = new PrintWriter(file)) {
			for (String str : contents) {
				writer.println(str);
			}
			writer.flush();
		}
		return file;
	}

	/**
	 * 新建文件
	 */
	public static File save(String pathname, InputStream input) throws IOException {
		File file = new File(pathname);
		if (!file.getParentFile().exists()) {
			Assert.isTrue(file.getParentFile().mkdirs(), new Error(ResultStatus.EXPECTATION_FAILED.getCode(),
					FAILED_CREATE_DIRECTORY + file.getParentFile().getAbsolutePath()));
		}
		try (OutputStream output = new FileOutputStream(file)) {
			IoUtils.copy(input, output);
			return file;
		}
	}

	/**
	 * 压缩文件
	 * @param files: 文件列表
	 * @param archiverName: zip文档全路径
	 */
	public static void zip(File[] files, String archiverName) throws IOException, ArchiveException {
		File archiver = new File(archiverName);
		try (ZipArchiveOutputStream zos = new ArchiveStreamFactory()
				.createArchiveOutputStream("zip", new FileOutputStream(archiver))) {
			zip(zos, files, archiver.getParent());
		}
	}

	private static void zip(ZipArchiveOutputStream zos, File[] files, String path) throws IOException {
		if (ArrayUtils.isEmpty(files)) {
			return;
		}
		for (File file : files) {
			zos.putArchiveEntry(new ZipArchiveEntry(getEntryName(file, path)));
			if (file.isDirectory()) {
				zip(zos, file.listFiles(), path);
				continue;
			}
			IoUtils.copy(new FileInputStream(file), zos);
			zos.closeArchiveEntry();
		}
	}

	/**
	 * 获取文件相对路径,作为在ZIP中路径
	 */
	private static String getEntryName(File file, String path) {
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		String entryName;
		if (file.getAbsolutePath().contains(path)) {
			entryName = file.getAbsolutePath().substring(path.length());
		} else {
			entryName = file.getName();
		}
		if (file.isDirectory()) {
			// 标识file是文件夹
			entryName += File.separator;
		}
		return entryName;
	}

	/**
	 * 解压文件
	 * @param archiver: zip文档
	 * @param extractPath: 解压路径
	 */
	public static void unZip(File archiver, String extractPath) throws IOException {
		try (ZipFile zipFile = ZipFile.builder().setFile(archiver).get()) {
			Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();
				File file = new File(extractPath, entry.getName());
				if (entry.getName().endsWith(File.separator)) {
					Assert.isTrue(file.mkdirs(), new Error(ResultStatus.EXPECTATION_FAILED.getCode(),
							FAILED_CREATE_DIRECTORY + file.getAbsolutePath()));
					continue;
				}

				File parent = file.getParentFile();
				if (!parent.exists()) {
					Assert.isTrue(parent.mkdirs(), new Error(ResultStatus.EXPECTATION_FAILED.getCode(),
							FAILED_CREATE_DIRECTORY + parent.getAbsolutePath()));
				}
				try (OutputStream os = new FileOutputStream(file)) {
					IoUtils.copy(zipFile.getInputStream(entry), os);
				}
			}
		}
	}

	/**
	 * 复制文件或目录
	 * @param from 原文件全路径
	 * @param to 复制文件全路径
	 */
	public static void copy(String from, String to) throws IOException {
		File fromFile = new File(from);
		if (!fromFile.exists()) {
			return;
		}
		if (fromFile.isFile()) {
			copyFile(fromFile, new File(to));
		} else {
			copyDirectory(fromFile, new File(to));
		}
	}

	/**
	 * 复制文件
	 * @param from 原文件
	 * @param to 复制文件
	 */
	private static void copyFile(File from, File to) throws IOException {
		try (InputStream inStream = new FileInputStream(from);
			 OutputStream outStream = new FileOutputStream(to)) {
			byte[] buffer = new byte[1024];
			int byteread = Constants.ZERO;
			while ((byteread = inStream.read(buffer)) != -1) {
				outStream.write(buffer, Constants.ZERO, byteread);
			}
			outStream.flush();
		}
	}

	/**
	 * 复制文件夹
	 * @param from 原文件夹
	 * @param to 复制文件夹
	 */
	private static void copyDirectory(File from, File to) throws IOException {
		if (!to.exists()) {
			Assert.isTrue(to.mkdirs(), new Error(ResultStatus.EXPECTATION_FAILED.getCode(),
					FAILED_CREATE_DIRECTORY + to.getAbsolutePath()));
		}
		String[] files = from.list();
		if (ArrayUtils.isNotEmpty(files)) {
			for (String file : files) {
				from = new File(from.getPath() + File.separator + file);
				to = new File(to.getPath() + File.separator + file);
				if (from.isFile()) {
					copyFile(from, to);
				} else {
					copyDirectory(from, to);
				}
			}
		}
	}

	/**
	 * 删除文件或目录
	 */
	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File item : files) {
					delete(item);
				}
			}
		}
		Files.delete(file.toPath());
	}

	/**
	 * 移动文件或目录
	 * @param from 原文件全路径
	 * @param to 移动文件全路径
	 */
	public static void move(String from, String to) throws IOException {
		File fromFile = new File(from);
		if (!fromFile.exists()) {
			return;
		}
		if (fromFile.isFile()) {
			copyFile(fromFile, new File(to));
		} else {
			copyDirectory(fromFile, new File(to));
		}
		delete(fromFile);
	}

	/**
	 * 读取扩展名
	 * @param name: 文件名(文件全路径名称序列中的最后一个名称)
	 */
	public static String getExtension(String name) {
		int index = name.lastIndexOf(Constants.DOT);
		return index > Constants.ZERO ? name.substring(index).toLowerCase() : Constants.BLANK_STR;
	}
}
