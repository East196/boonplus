package com.github.east196.lab.javacn;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pfr1998 {
	//使用Table<词，词性> Value 词频
	static Map<String, TagedWord> wordMap = new HashMap<>(); // 单词表

	/***
	 * 将字符串类型的词性转换为位域型的词性变量
	 * @param strPOS 字符串类型的词性标注
	 * @return 位域型词性标注
	 */
	private static PosTag getPosTag(String strPOS) {
		strPOS = strPOS.trim().toLowerCase();

		switch (strPOS) {
		case " ag ":
		case " ad ":
		case " an ":
		case " a ": // 形容词 形语素
			return PosTag.POS_D_A;

		case " bg ":
		case " b ": // 区别词 区别语素
			return PosTag.POS_D_B;

		case " c ": // 连词 连语素
			return PosTag.POS_D_C;

		case " dg ":
		case " d ": // 副词 副语素
			return PosTag.POS_D_D;

		case " e ": // 叹词 叹语素
			return PosTag.POS_D_E;

		case " f ": // 方位词 方位语素
			return PosTag.POS_D_F;

		case " i ": // 成语
			return PosTag.POS_D_I;

		case " j ":
		case " l ": // 习语
			return PosTag.POS_D_L;

		case " mg ":
		case " m ": // 数词 数语素
			return PosTag.POS_A_M;

		case " mq ": // 数量词
			return PosTag.POS_D_MQ;

		case " na ":
		case " ng ":
		case " n ": // 名词 名语素
			return PosTag.POS_D_N;

		case " o ": // 拟声词
			return PosTag.POS_D_O;

		case " p ": // 介词
			return PosTag.POS_D_P;

		case " q ": // 量词 量语素
			return PosTag.POS_A_Q;

		case " rg ":
		case " r ": // 代词 代语素
			return PosTag.POS_D_R;

		case " s ": // 处所词
			return PosTag.POS_D_S;

		case " tg ":
		case " t ": // 时间词
			return PosTag.POS_D_T;

		case " u ": // 助词 助语素
			return PosTag.POS_D_U;

		case " vg ":
		case " vn ":
		case " vd ":
		case " vv ":
		case " v ": // 动词 动语素
			return PosTag.POS_D_V;

		case " w ": // 标点符号
			return PosTag.POS_D_W;

		case " x ": // 非语素字
			return PosTag.POS_D_X;

		case " yg ":
		case " y ": // 语气词 语气语素
			return PosTag.POS_D_Y;

		case " z ": // 状态词
			return PosTag.POS_D_Z;

		case " nr ": // 人名
			return PosTag.POS_A_NR;

		case " ns ": // 地名
			return PosTag.POS_A_NS;

		case " nt ": // 机构团体
			return PosTag.POS_A_NT;

		case " nx ": // 外文字符
			return PosTag.POS_A_NX;

		case " nz ": // 其他专名
			return PosTag.POS_A_NZ;

		case " h ": // 前接成分
			return PosTag.POS_D_H;

		case " k ": // 后接成分
			return PosTag.POS_D_K;

		case " un ": // 未知词性
			return PosTag.POS_UNK;

		default:
			return PosTag.POS_UNK;

		}

	}

	/***
	 * 将一个单词插入单词表
	 * @param word 单词
	 * @param strPOS 词性
	 */
	private static void insertOneWordToTbl(String word, String strPOS) {
		if (word == null || strPOS == null) {
			return;
		}

		word = word.trim();
		if (word == "") {
			return;
		}

		PosTag tPOS = getPosTag(strPOS);

		TagedWord tWord = wordMap.get(word);

		if (tWord == null) {
			tWord = new TagedWord();
			tWord.freq = 0;
			tWord.pos = 0;
			tWord.word = word;
			wordMap.put(word, tWord);
		}

		tWord.freq++;
		tWord.pos |= tPOS.ordinal();
	}

	/***
	 * 删除文本中每句前面的时间
	 * @param text
	 * @return
	 */
	private static String deleteDate(String text) {
		return text.replaceAll("1998/d+-/d+-/d+-/d+///w", "");
	}

	/***
	 * 从文件载入到字符串中
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static List<String> loadFromFile(String fileName) throws IOException{
		return Files.readAllLines(Paths.get(fileName), Charset.forName("GB2312"));
	}

	/**/// / <summary>
	// / 从字符串读入到单词表中
	// / </summary>
	// / <param name="text"></param>

	private static void readWordFromString(String text) {
		List<String> strs = new ArrayList<>();
		// TODO toJava CRegex.GetMatchStrings(text,
		// @" (/w+)/s*///s*([a-zA-Z]{1,2}) " , true , Ref strs);

		String word = "";

		for (int i = 0; i < strs.size(); i++) {
			if (i % 2 == 0) {
				word = strs.get(i);
			} else {
				insertOneWordToTbl(word, strs.get(i));
			}
		}
	}

	/***
	 * 获取所有单词列表
	 * @return 单词列表
	 */
	static public List<TagedWord> getWordList() {
		List<TagedWord> tagedWords = new ArrayList<>(wordMap.values());
		return tagedWords;
	}

}
