package org.ace.java.component.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service("PasswordCodecHandler")
public class PasswordCodecHandler {
	Base64 codec = null;

	public PasswordCodecHandler() {
		codec = new Base64();
	}

	public String encode(String password) {
		byte[] temp;
		String encodedPassword = null;
		temp = codec.encode(password.getBytes());
		encodedPassword = new String(temp);
		return encodedPassword;
	}

	public String decode(String encodedPassword) {
		byte[] temp;
		String decodedPassword;
		temp = codec.decode(encodedPassword.getBytes());
		decodedPassword = new String(temp);
		return decodedPassword;
	}

	public static void main(String[] args) {
		PasswordCodecHandler passwordCodecHandler = new PasswordCodecHandler();
		String s1 = passwordCodecHandler.encode("password");
		System.out.println(s1);

		String s2 = passwordCodecHandler.encode("admin");
		System.out.println(s2);

		String s3 = passwordCodecHandler.encode("administrator");
		System.out.println(s3);

		String s4 = passwordCodecHandler.encode("mmh");
		System.out.println(s4);
	}
}
