package cn.md;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class AssetJTest {
	@Test
	public void test() {
		assertThat(0).isZero();
		assertThat(0.000001).isEqualTo(0.000001);
	}

	@Test
	public void testFail() {
		// fail("在不检查任何条件的情况下使断言失败。显示一则消息");
	}
}
