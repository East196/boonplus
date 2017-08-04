package com.github.east196.boonplus

import com.google.common.base.Charsets
import com.google.common.base.Function
import com.google.common.base.Predicate
import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import com.google.common.hash.HashCode
import com.google.common.hash.Hashing
import com.google.common.io.ByteStreams
import com.google.common.io.Files
import java.io.File
import java.io.InputStream
import java.lang.reflect.Array
import java.util.Collection
import java.util.List
import java.util.Map
import java.util.UUID
import java.util.concurrent.TimeUnit

class BoonPlus {
	def static void sleep(long seconds) {
		TimeUnit.SECONDS.sleep(seconds)
	}

	def static String uuid() {
		return UUID.randomUUID().toString()
	}

	def static <F, T> List<T> map(List<F> fromList, Function<? super F, ? extends T> function) {
		return Lists.transform(fromList, function)
	}

	def static <T> List<T> filter(List<T> unfiltered, Predicate<? super T> predicate) {
		return Lists.newArrayList(Iterables.filter(unfiltered, predicate))
	}

	def static boolean notBlank(CharSequence cs) {
		return !isBlank(cs)
	}

	def static boolean isBlank(CharSequence cs) {
		var int strLen
		if ((cs === null) || ((strLen = cs.length()) === 0)) {
			return true
		}
		for (var int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) === false) {
				return false
			}
		}
		return true
	}

	def static boolean notEmpty(Object obj) {
		return length(obj) !== 0
	}

	def static boolean isEmpty(Object obj) {
		return length(obj) === 0
	}

	def static int length(Object obj) {
		if (obj === null) {
			return 0
		} else if (obj instanceof CharSequence) {
			return ((obj as CharSequence)).length()
		} else if (obj instanceof Collection<?>) {
			return ((obj as Collection<?>)).size()
		} else if (obj instanceof Map<?,?>) {
			return ((obj as Map<?, ?>)).size()
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj)
		} else {
			return 1
		}
	}

	def static <T> T[] toArray(Collection<T> list, Class<T> type) {
		return list.toArray((Array.newInstance(type, list.size()) as T[]))
	}

	def static String md5(String text) {
		var HashCode hashCode = Hashing.md5().hashString(text, Charsets.UTF_8)
		return hashCode.toString()
	}

	def static String md5(File file) {

		var HashCode hashCode = Files.hash(file, Hashing.md5())
		return hashCode.toString()
	}

	def static void copy(File from, File to) {

		Files.copy(from, to)

	}

	def static void copy(InputStream from, File to) {
		Files.write(ByteStreams.toByteArray(from), to)

	}

	def static String toString(File file) {

		return Files.toString(file, Charsets.UTF_8)

	}

	def static String getFileExtension(String filename) {
		if (filename.endsWith(".tar.gz")) {
			return "tar.gz"
		}
		return Files.getFileExtension(filename)
	}

	def static String getNameWithoutExtension(String filename) {
		var String nameWithoutExtension = Files.getNameWithoutExtension(filename)
		if (filename.endsWith(".tar.gz")) {
			nameWithoutExtension += ".tar"
		}
		return nameWithoutExtension
	}
}
