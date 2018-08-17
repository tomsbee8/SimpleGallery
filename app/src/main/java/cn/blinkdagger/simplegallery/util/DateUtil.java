package cn.blinkdagger.simplegallery.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    /**
     * 根据时间戳获取年月日
     *
     * @param timemilles
     * @return
     */
    public static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

    public static String getShortDateString(long timemilles) {
        return simpleFormatter.format(new Date(timemilles));
    }

    public static long getShortDateMilles(long timemilles) {

        Calendar calendar =Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(timemilles);
        // 将时分秒,毫秒清零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
