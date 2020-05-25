package com.github.east196.lab.javacn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于得到指定词性的词
 * @author Administrator
 *
 */
public class FreqWordsHandler {
	private static List<String> freqWordsList = null;

	public static boolean isFreqWords(String words) {
		// System.out.println(words);
		for (String s : freqWordsList) {
			if (s.equals(words)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getFreqWords(String path, String type) {
		List<String> reList = new ArrayList<String>();
		File file = new File(path);
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(reader);
			String tempStr = "";
			while ((tempStr = buffer.readLine()) != null) {
				if (tempStr.contains(",")) {
					String[] freqWors = tempStr.split(" ");
					String words = freqWors[0];
					String wordType = freqWors[2];
					String[] freqType = wordType.split(",");
					for (String splittype : freqType) {
						if (splittype.equals("N") || splittype.equals("V")
						// || splittype.equals("ADV")
						// || splittype.equals("ADJ")
						) {
							reList.add(words);
						}
					}
				} else if (tempStr.contains("IDIOM")) {
					String[] freqWors = tempStr.split(" ");
					reList.add(freqWors[0]);
				}
			}
			buffer.close();
			reader.close();
			return reList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static {
		if (freqWordsList == null) {
			freqWordsList = getFreqWords("x:\\Freq\\SogouLabDic.dic", "N");
		}
	}

	public static void main(String[] args) {
		isFreqWords("稗官小说");
	}
}