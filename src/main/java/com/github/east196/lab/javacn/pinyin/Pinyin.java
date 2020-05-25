package com.github.east196.lab.javacn.pinyin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pinyin {
	Map<String, List<String>> map;

	private Pinyin() throws IOException {
		this.map = initMap();
	}

	private Map<String, List<String>> initMap() throws IOException {
		String resourceName = System.getProperty("user.dir")+"/unicode_to_hanyu_pinyin.txt";
		List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(resourceName), StandardCharsets.UTF_8);
		Map<String, List<String>> map = new HashMap<>();
		for (String line : lines) {
			if (line.trim().isEmpty())
				continue;
			String[] keyValues = line.split(" ");
			String key = keyValues[0];
			String values = keyValues[1];
			int indexOfLeftBracket = values.indexOf("(");
			int indexOfRightBracket = values.lastIndexOf(")");
			String stripedString = values.substring(indexOfLeftBracket + 1, indexOfRightBracket);
			List<String> value = Arrays.asList(stripedString.split(","));
			map.put(key, value);
		}
		return map;
	}

	public List<String> getPinyins(char word) {
		int codePointOfChar = word;
		String codepointHexStr = Integer.toHexString(codePointOfChar).toUpperCase();
		List<String> foundRecords = map.get(codepointHexStr);
		return foundRecords;
	}

	public List<List<String>> getPinyins(String words) {
		List<List<String>> foundRecords = new ArrayList<>();
		char[] wordArray = words.toCharArray();
		for (char word : wordArray) {
			foundRecords.add(getPinyins(word));
		}
		return foundRecords;
	}

	public static void main(String[] args) throws IOException {
		Pinyin p = new Pinyin();
		List<List<String>> s = p.getPinyins("中国");
		System.out.println(s);
	}
}
