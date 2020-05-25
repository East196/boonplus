package cn.md.common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class VersionTest {

	@Test
	public void test() {
		Version version=Version.parse("4.1.1.200");
		assertThat(version.is(Version.parse("4.1.1.200")), is(true));
		assertThat(version.isLessThanOrEqualTo(Version.parse("4.1.1.200")), is(true));
		assertThat(version.isLessThan(Version.parse("4.1.1.201")), is(true));
	}

}
