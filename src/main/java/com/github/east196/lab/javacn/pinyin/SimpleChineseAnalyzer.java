package com.github.east196.lab.javacn.pinyin;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cn.smart.HMMChineseTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;

/**
 * <p>
 * SimpleChineseAnalyzer is an analyzer for Chinese or mixed Chinese-English
 * text. The analyzer uses probabilistic knowledge to find the optimal word
 * segmentation for Simplified Chinese text. The text is first broken into
 * sentences, then each sentence is segmented into words.
 * </p>
 * <p>
 * Segmentation is based upon the <a
 * href="http://en.wikipedia.org/wiki/Hidden_Markov_Model">Hidden Markov
 * Model</a>. A large training corpus was used to calculate Chinese word
 * frequency probability.
 * </p>
 * <p>
 * This analyzer requires a dictionary to provide statistical data.
 * SimpleChineseAnalyzer has an included dictionary out-of-box.
 * </p>
 * <p>
 * The included dictionary data is from <a
 * href="http://www.ictclas.org">ICTCLAS1.0</a>. Thanks to ICTCLAS for their
 * hard work, and for contributing the data under the Apache 2 License!
 * </p>
 * 
 * @lucene.experimental
 */
public final class SimpleChineseAnalyzer extends Analyzer {

	private final CharArraySet stopWords;

	private static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";

	private static final String STOPWORD_FILE_COMMENT = "//";

	/**
	 * Returns an unmodifiable instance of the default stop-words set.
	 * 
	 * @return an unmodifiable instance of the default stop-words set.
	 */
	public static CharArraySet getDefaultStopSet() {
		return DefaultSetHolder.DEFAULT_STOP_SET;
	}

	/**
	 * Atomically loads the DEFAULT_STOP_SET in a lazy fashion once the outer
	 * class accesses the static final set the first time.;
	 */
	private static class DefaultSetHolder {
		static final CharArraySet DEFAULT_STOP_SET;

		static {
			try {
				DEFAULT_STOP_SET = loadDefaultStopWordSet();
			} catch (IOException ex) {
				// default set should always be present as it is part of the
				// distribution (JAR)
				throw new RuntimeException("Unable to load default stopword set");
			}
		}

		static CharArraySet loadDefaultStopWordSet() throws IOException {
			// make sure it is unmodifiable as we expose it in the outer class
			return CharArraySet.unmodifiableSet(WordlistLoader.getWordSet(
					IOUtils.getDecodingReader(SimpleChineseAnalyzer.class, DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8), STOPWORD_FILE_COMMENT));
		}
	}

	/**
	 * Create a new SimpleChineseAnalyzer, using the default stopword list.
	 */
	public SimpleChineseAnalyzer() {
		this(true);
	}

	/**
	 * <p>
	 * Create a new SimpleChineseAnalyzer, optionally using the default stopword
	 * list.
	 * </p>
	 * <p>
	 * The included default stopword list is simply a list of punctuation. If
	 * you do not use this list, punctuation will not be removed from the text!
	 * </p>
	 * 
	 * @param useDefaultStopWords
	 *            true to use the default stopword list.
	 */
	public SimpleChineseAnalyzer(boolean useDefaultStopWords) {
		stopWords = useDefaultStopWords ? DefaultSetHolder.DEFAULT_STOP_SET : CharArraySet.EMPTY_SET;
	}

	/**
	 * <p>
	 * Create a new SimpleChineseAnalyzer, using the provided {@link Set} of
	 * stopwords.
	 * </p>
	 * <p>
	 * Note: the set should include punctuation, unless you want to index
	 * punctuation!
	 * </p>
	 * 
	 * @param stopWords
	 *            {@link Set} of stopwords to use.
	 */
	public SimpleChineseAnalyzer(CharArraySet stopWords) {
		this.stopWords = stopWords == null ? CharArraySet.EMPTY_SET : stopWords;
	}

	@Override
	public TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final Tokenizer tokenizer=new HMMChineseTokenizer(reader);
		TokenStream result = new PorterStemFilter(tokenizer);
		if (!stopWords.isEmpty()) {
			result = new StopFilter(result, stopWords);
		}
		return new TokenStreamComponents(tokenizer, result);
	}
}