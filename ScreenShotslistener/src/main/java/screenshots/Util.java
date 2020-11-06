package screenshots;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {

    public static final int duration = 6000;//6秒内的截屏

    /**
     * 截屏依据中的路径判断关键字
     */
    private static final String[] KEYWORDS = {
            "screenshot", "Screenshots", "Screenshot_", "Screen_shot", "Screen__shots", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };


    public static boolean hasKeyWords(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        for (String key : KEYWORDS) {
            if (path.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private static TimeZone gDefaultTimeZone;
    private static SimpleDateFormat gFormatter = new SimpleDateFormat();

    static {
        gDefaultTimeZone = TimeZone.getTimeZone("GMT+8");
        gFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static String format(String paramString, Date paramDate) {
        return format(paramString, paramDate, gDefaultTimeZone);
    }

    private static String format(String paramString, Date paramDate, TimeZone paramTimeZone) {
        synchronized (gFormatter) {
            gFormatter.setTimeZone(paramTimeZone);
            gFormatter.applyPattern(paramString);
            String str = gFormatter.format(paramDate);
            return str;
        }
    }

    public static String toDateTime(long paramLong) {
        return format("yyyyMMddHHmmss", new Date(paramLong));
    }

    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean checkDate(long date) {
        long current = System.currentTimeMillis();
        long imgDate = new Date(date).getTime();
        if (imgDate >= (current - duration)) {
            return true;
        }
        return false;
    }
}
