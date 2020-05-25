package cn.md.echarts;

import org.junit.Test;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.MarkType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PointData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;

public class BarTest1 {
	@Test
	public void test() {
		// 地址：http://echarts.baidu.com/doc/example/bar1.html
		GsonOption option = new GsonOption();
		option.title().text("某地区蒸发量和降水量").subtext("纯属虚构");
		option.tooltip().trigger(Trigger.axis);
		option.legend("蒸发量", "降水量");
		option.toolbox().show(true)
				.feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
		option.calculable(true);
		option.xAxis(new CategoryAxis().data(""));//Category=1,在Category里有不同数据
		option.yAxis(new ValueAxis());
		Bar bar = new Bar("蒸发量");
		bar.data(2.0);
		bar.markPoint().data(new PointData().type(MarkType.max).name("最大值"), new PointData().type(MarkType.min).name("最小值"));
		Bar bar2 = new Bar("降水量");
		bar2.data(2.6);
		bar2.markPoint().data(new PointData("年最高", 182.2).xAxis(7).yAxis(183).symbolSize(18), new PointData("年最低", 2.3).xAxis(11).yAxis(3));
		option.series(bar);
		System.out.println(option.toPrettyString());
	}
}