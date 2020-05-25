package cn.md.common.utils;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

public class JodaTimeTest {
	@Test
	public void test() {
		DateTime dt = new DateTime();// 取得当前时间
		Date date = new Date();
		assertEquals(date.getTime(), dt.getMillis(), 100L);

		// 根据指定格式,将时间字符串转换成DateTime对象
		DateTime dt2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("1985-12-12 23:23:23");
		// 年,月,日,时,分,秒,毫秒 1985-12-12 23:23:23.000
		DateTime dt3 = new DateTime(1985, 12, 12, 23, 23, 23, 0);
		assertEquals(dt2, dt3);
		// 将dt对象，按照指定格式输出字符串
		assertEquals("1985-12-12 23:23:23", dt3.toString("yyyy-MM-dd HH:mm:ss"));

		// 判断是否闰月
		DateTime dt4 = new DateTime();
		assertEquals(false, dt4.monthOfYear().isLeap());

		// 取得 3秒前的时间
		DateTime dt5 = dt.secondOfMinute().addToCopy(-3);
		DateTime dt51 = dt.plusSeconds(-3);
		DateTime dt52 = dt.minusSeconds(3);
		assertEquals(dt51, dt52);
		assertEquals(dt51, dt5);

		// DateTime与java.util.Date对象,当前系统TimeMillis转换
		long now = System.currentTimeMillis();
		DateTime dt6 = new DateTime(new Date(now));
		DateTime dt7 = new DateTime(now);
		assertEquals(dt6, dt7);

	}
	
	public  Integer getVipDueDaysOld(DateTime vipDue) {//day是每周的day
		if(vipDue.isBeforeNow())return 0;
		DateTime now=new DateTime();
		Period period=new Period(now, vipDue);
		return period.getDays()+1;
	}
	
	public Integer getVipDueDays(DateTime vipDue) {//day是每月的day
		if (vipDue.isBeforeNow())
			return 0;
		DateTime now = new DateTime();
		int realDays = Days.daysBetween(now, vipDue).getDays();
		return realDays + 1;
	}	
	
	@Test
	public void testPeriod() {
		System.out.println(getVipDueDays(new DateTime().plusHours(3)));
		System.out.println(getVipDueDays(new DateTime().plusDays(23)));
		System.out.println(getVipDueDays(new DateTime().plusHours(25)));
		System.out.println(getVipDueDays(new DateTime().plusHours(-3)));
	}
}