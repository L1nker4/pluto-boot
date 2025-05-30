package com.github.pluto.boot.base.utils;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/4 14:18
 * @description： 
 */
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/4 14:18
 * @description： 使用 Guava 实现 MD5 加密
 */
public class MD5Util {

	protected MD5Util() {
	}

	private static final int HASH_ITERATIONS = 2;

	public static String encrypt(String password) {
		return hashWithIterations(password, password, HASH_ITERATIONS);
	}

	public static String encrypt(String username, String password) {
		String salt = username.toLowerCase() + password;
		return hashWithIterations(password, salt, HASH_ITERATIONS);
	}

	private static String hashWithIterations(String input, String salt, int iterations) {
		String combined = input + salt;
		String hash = md5(combined);
		for (int i = 1; i < iterations; i++) {
			hash = md5(hash);
		}
		return hash;
	}

	private static String md5(String input) {
		return Hashing.md5().hashString(input, StandardCharsets.UTF_8).toString();
	}

	public static void main(String[] args) {
		System.out.println(encrypt("admin", "admin"));
	}
}

