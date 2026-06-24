package com.example.personalfinance.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Tiện ích xử lý ngày tháng cho Android.
 */
public class DateUtils {

    private static final SimpleDateFormat API_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    /**
     * Format ngày để gửi lên API: 2026-06-24
     */
    public static String toApiFormat(Date date) {
        return date != null ? API_FORMAT.format(date) : null;
    }

    /**
     * Format ngày để hiển thị trên UI: 24/06/2026
     */
    public static String toDisplayFormat(Date date) {
        return date != null ? DISPLAY_FORMAT.format(date) : "";
    }

    /**
     * Format tháng/năm: 06/2026
     */
    public static String toMonthYear(Date date) {
        return date != null ? MONTH_YEAR_FORMAT.format(date) : "";
    }

    /**
     * Lấy ngày đầu tháng hiện tại.
     */
    public static Date getStartOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Lấy ngày cuối tháng hiện tại.
     */
    public static Date getEndOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
}
