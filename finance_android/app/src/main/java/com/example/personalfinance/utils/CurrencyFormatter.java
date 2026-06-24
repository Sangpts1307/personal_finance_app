package com.example.personalfinance.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Tiện ích định dạng tiền tệ VND.
 */
public class CurrencyFormatter {

    private static final DecimalFormat VND_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,###", symbols);
    }

    /**
     * Format số tiền sang dạng VND: 1.500.000đ
     */
    public static String format(double amount) {
        return VND_FORMAT.format(amount) + "đ";
    }

    /**
     * Format có dấu +/- phía trước.
     * Income: +1.500.000đ (màu xanh)
     * Expense: -1.500.000đ (màu đỏ)
     */
    public static String formatSigned(double amount, boolean isExpense) {
        String sign = isExpense ? "-" : "+";
        return sign + VND_FORMAT.format(Math.abs(amount)) + "đ";
    }
}
