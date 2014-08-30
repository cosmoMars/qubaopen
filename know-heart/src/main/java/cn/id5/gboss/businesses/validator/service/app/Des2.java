package cn.id5.gboss.businesses.validator.service.app;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class Des2 {

	private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

	public static String encode(String key, byte[] data) throws Exception {
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// key的长度不能够小于8位字节
		Key secretKey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());// 向量
		AlgorithmParameterSpec paramSpec = iv;

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
		byte[] bytes = cipher.doFinal(data);
		return Base64.encodeBase64String(bytes);
	}

	public static String decode(String key, byte[] data) throws Exception {
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// key 的长度不能够小于 8 位字节
		Key secretKey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
		AlgorithmParameterSpec paramSpec = iv;
		cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
		return new String(cipher.doFinal(data), "GBK");
	}

	public static String decodeValue(String key, String data) throws Exception {
		String datas;
		if (System.getProperty("os.name") != null
				&& (System.getProperty("os.name").equalsIgnoreCase("sunos") || System
						.getProperty("os.name").equalsIgnoreCase("linux"))) {
			datas = decode(key, Base64.decodeBase64(data));
		} else {
			datas = decode(key, Base64.decodeBase64(data));
		}

		return datas;
	}

}
