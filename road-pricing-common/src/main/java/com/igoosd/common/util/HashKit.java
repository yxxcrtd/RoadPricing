package com.igoosd.common.util;

import java.security.MessageDigest;
import java.util.Random;

public class HashKit {
	private static java.security.SecureRandom random = new java.security.SecureRandom();

	public static String md5(String srcStr) {
		return hash("MD5", srcStr);
	}

	public static String sha1(String srcStr) {
		return hash("SHA-1", srcStr);
	}

	public static String sha256(String srcStr) {
		return hash("SHA-256", srcStr);
	}

	public static String sha384(String srcStr) {
		return hash("SHA-384", srcStr);
	}

	public static String sha512(String srcStr) {
		return hash("SHA-512", srcStr);
	}

	public static String hash(String algorithm, String srcStr) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
			return toHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1)
				result.append("0");
			result.append(hex);
		}
		return result.toString();
	}

	/**
	 * md5 128bit 16bytes sha1 160bit 20bytes sha256 256bit 32bytes sha384
	 * 384bit 48bites sha512 512bit 64bites
	 */
	public static String generateSalt(int numberOfBytes) {
		byte[] salt = new byte[numberOfBytes];
		random.nextBytes(salt);
		return toHex(salt);
	}

	public static String  getRandStr(int len){
		StringBuilder sb = new StringBuilder(len);
		Random random = new Random();
		int[] indexs = random.ints(len,0,chars.length).toArray();
		for (int index: indexs){
			sb.append(chars[index]);
		}
		return sb.toString();
	}



	private static final char[] chars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
	,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
	'1','2','3','4','5','6','7','8','9','0'};

	public static void main(String[] args) {
		String passwd = "123456";
		String md5 = HashKit.md5(passwd);
		System.out.println(generateSalt(6));
	}
}
