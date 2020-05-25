package cn.md.jbehave;

import java.util.List;

import org.jbehave.core.embedder.Embedder;

import com.google.common.collect.Lists;

public class JBehaveTest {
	private static Embedder embedder = new Embedder();
	private static List<String> storyPaths = Lists.newArrayList("jbehave/math.story");

	public static void main(String[] args) {
		embedder.candidateSteps().add(new MathSteps());
		try {
			embedder.runStoriesAsPaths(storyPaths);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}