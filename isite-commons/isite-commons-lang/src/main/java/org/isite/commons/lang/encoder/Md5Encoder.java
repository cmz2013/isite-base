package org.isite.commons.lang.encoder;

import org.isite.commons.lang.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * @Description MD5编码工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class Md5Encoder {

	private Md5Encoder() {
	}

	/**
	 * 字符串MD5值
	 */
	public static String digest(String src) throws NoSuchAlgorithmException {
		if (null == src) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(src.getBytes());
		byte tmp[] = md.digest(); 
		char rec[] = new char[16 * 2];
		int k = 0; 
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			rec[k++] = hexDigits[byte0 >>> 4 & 0xf];
			rec[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(rec);
	}

	/**
	 * 文件MD5值
	 */
	public static String digest(File file) throws IOException, NoSuchAlgorithmException {
		try (FileInputStream in = new FileInputStream(file)) {
			byte[] buffer = new byte[1024];
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			int length;
			while ((length = in.read(buffer, Constants.ZERO, 1024)) != -1) {
				messageDigest.update(buffer, Constants.ZERO, length);
			}
			BigInteger bigInt = new BigInteger(Constants.ONE, messageDigest.digest());
			return bigInt.toString(16);
		}
	}
}