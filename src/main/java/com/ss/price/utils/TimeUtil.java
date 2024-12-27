package com.ss.price.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private static boolean cheackLocalTimeIsTrue = true;

    public TimeUtil() {
    }

    public static String ts2time(long timestamp) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timeText = null;
        if (timestamp > 0L && timestamp > 1000000000L) {
            if (timestamp > 1000000000000L) {
                pattern = "yyyy-MM-dd HH:mm:ss.SSS";
            } else {
                timestamp *= 1000L;
            }

            timeText = formatBeijingTime(pattern).format(timestamp);
        } else {
            System.out.println("必须输入10位或13位时间戳！");
        }

        return timeText;
    }

    public static Long time2ts10(String time) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if (time.contains("/")) {
            pattern = "yyyy/MM/dd HH:mm:ss";
        }

        Long parse = 0L;

        try {
            parse = formatBeijingTime(pattern).parse(time).getTime();
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return parse / 1000L;
    }

    public static String tdtime() {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        return getFormatTime(pattern, get13TimeStamp());
    }

    public static Long time2ts13(String time) {
        String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
        if (time.contains("/")) {
            pattern = "yyyy/MM/dd HH:mm:ss:SSS";
        } else if (time.contains(".")) {
            pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        }

        Long parse = 0L;

        try {
            parse = formatBeijingTime(pattern).parse(time).getTime();
        } catch (ParseException var4) {
            System.out.println("时间格式错误：" + var4.getMessage());
            var4.printStackTrace();
        }

        return parse;
    }

    public static String getFormatTime(String pattern, long timestamp) {
        return formatBeijingTime(pattern).format(new Date(timestamp));
    }

    public static Long getFormatTimeTs13(String pattern, String time) {
        try {
            return formatBeijingTime(pattern).parse(time).getTime();
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getFormatNowTimeToSeconds(String pattern) {
        Long timeStamp = 0L;
        if (pattern == null || pattern == "") {
            pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        }

        if (netTime() > 1574000000L) {
            timeStamp = netTime();
        } else {
            timeStamp = beijingTimeStamp13();
        }

        return formatBeijingTime(pattern).format(new Date(timeStamp));
    }

    public static String getFormatNowTimeToSeconds() {
        return getFormatNowTimeToSeconds((String)null);
    }

    public static Long get13TimeStamp() {
        Long time = netTime();
        return time > 1574000000L ? time : beijingTimeStamp13();
    }

    public static Long get10TimeStamp() {
        Long time = netTime();
        return time > 1574000000L ? time / 1000L : beijingTimeStamp13() / 1000L;
    }

    private static Long beijingTimeStamp13() {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";

        try {
            return formatBeijingTime(pattern).parse(formatBeijingTime(pattern).format(System.currentTimeMillis())).getTime();
        } catch (ParseException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(beijingTimeStamp13());
    }

    private static Long netTime() {
        Long localTime = beijingTimeStamp13();
        String url = "http://timeStamp.tianqi.com";
        return localTime;
    }

    public static SimpleDateFormat formatBeijingTime(String pattern) {
        SimpleDateFormat bjSdf = new SimpleDateFormat(pattern, Locale.CHINA);
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return bjSdf;
    }

    public static String millisecondTakesTimeToDateStr(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = ms / (long)dd;
        Long hour = (ms - day * (long)dd) / (long)hh;
        Long minute = (ms - day * (long)dd - hour * (long)hh) / (long)mi;
        Long second = (ms - day * (long)dd - hour * (long)hh - minute * (long)mi) / (long)ss;
        Long milliSecond = ms - day * (long)dd - hour * (long)hh - minute * (long)mi - second * (long)ss;
        StringBuffer sb = new StringBuffer();
        if (day > 0L) {
            sb.append(day + "天");
        }

        if (hour > 0L) {
            sb.append(hour + "小时");
        }

        if (minute > 0L) {
            sb.append(minute + "分");
        }

        if (second > 0L) {
            sb.append(second + "秒");
        }

        if (milliSecond > 0L) {
            sb.append(milliSecond + "毫秒");
        }

        return sb.toString();
    }
}
