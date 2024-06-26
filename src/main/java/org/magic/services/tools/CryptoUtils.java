package org.magic.services.tools;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.AES256TextEncryptor;
import org.magic.services.logging.MTGLogger;

public class CryptoUtils {

	static Logger logger = MTGLogger.getLogger(CryptoUtils.class);

	private CryptoUtils() {

	}
	
	public static String encrypt(String strToEncrypt, String secret) {
		var textEncryptor = new AES256TextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		return textEncryptor.encrypt(strToEncrypt);
	}

	public static String decrypt(String strToDecrypt, String secret) {
		var textEncryptor = new AES256TextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		return textEncryptor.decrypt(strToDecrypt);
	}

	public static String getMD5(byte[] input) {
		  byte[] bytes= DigestUtils.getMd5Digest().digest(input);
			 StringBuilder sb = new StringBuilder();
		      for (byte b : bytes) {
		          sb.append(String.format("%02x", b));
		      }

		      return sb.toString();
	}


	public static String generateMD5(String s)
	{
		return DigestUtils.md5Hex(s).toUpperCase();
	}

	public static String generateSha256(String s)
	{
		return DigestUtils.sha256Hex(s).toUpperCase();
	}



	public static String toBase64(byte[] str) {
	    return Base64.getEncoder().encodeToString(str);
	}

	public static byte[] fromBase64(String s) {
	    return Base64.getDecoder().decode(s);
	}

	public static int randomInt(int i) {
		try {
			return SecureRandom.getInstanceStrong().nextInt(i);
		} catch (NoSuchAlgorithmException e) {
			return -1;
		}
	}

	public static Long randomLong() {
		try {
			return SecureRandom.getInstanceStrong().nextLong();
		} catch (NoSuchAlgorithmException e) {
			return -1L;
		}
	}



}