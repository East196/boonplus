package com.github.east196.fireworks;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;

public class Time {

    public static Date praseDate(String dateOrDateTime) {
        String date = dateOrDateTime.replace("+08:00", ".000Z").replace("(null)", ".000Z");
        try {
            return DateUtils.parseDate(date, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy/MM/dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static final FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat LONG_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    public static final FastDateFormat YYYYMMDDHHMMSS_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");
    public static final FastDateFormat YYYYMMDD_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public static String shortDate(Date date) {
        return SHORT_DATE_FORMAT.format(date);
    }

    public static String shortDate(long millis) {
        return SHORT_DATE_FORMAT.format(millis);
    }

    public static String longDate(Date date) {
        return LONG_DATE_FORMAT.format(date);
    }

    public static String longDate(long millis) {
        return LONG_DATE_FORMAT.format(millis);
    }
}
