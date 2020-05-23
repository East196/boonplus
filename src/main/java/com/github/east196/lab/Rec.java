package com.github.east196.lab;

import java.net.URL;

public class Rec {
	public static void main(String[] args) {
		String fileName = "abc.txt";
		URL configFileURL = Rec.class.getResource(fileName);//当前路径
		System.out.println("1"+configFileURL);
		if (configFileURL == null) {
			configFileURL = Rec.class.getClassLoader().getResource(fileName);//根路径
			System.out.println("2"+configFileURL);
			if (configFileURL == null) {
				configFileURL = ClassLoader.getSystemResource(fileName);
				System.out.println("3"+configFileURL);
			}
		}
		System.out.println("4"+configFileURL);
	}
}
