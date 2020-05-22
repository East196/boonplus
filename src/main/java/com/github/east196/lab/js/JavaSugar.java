package com.github.east196.lab.js;

import org.mozilla.javascript.tools.jsc.Main;

public class JavaSugar {
	public static void main(String args[]) {
		String[] argList=new String[]{"src/main/java/cn/snow/party/TungTest.js"};
		Main main = new Main();
		args = main.processOptions(argList);
		main.processSource(args);
	}
}
