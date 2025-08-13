package com.transporthc.utils;

import com.transporthc.enums.IDKey;
import com.transporthc.enums.PeriodFormatter;
import com.transporthc.exception.define.InvalidParameterException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class utils {
    public static String genID(IDKey key) {
        if (key == null) return null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddyyMMmmssHH");
        String code = now.format(formatter);
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return key.label + code + randomPart;
    }

    static Date toNextMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    static Date convertStringToDate(String dateString, String pattern, boolean isOffset) {
        if (dateString == null) return null;
        try {
            if (PeriodFormatter.YM_FORMATTER.getPattern().equals(pattern)) {
                dateString += "-01";
                pattern = PeriodFormatter.YMD_FORMATTER.getPattern();
            }
            LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
            if (isOffset) localDate = localDate.plusDays(1);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("Định dạng yêu cầu không hợp lệ! Yêu cầu dạng: " + pattern);
        }
    }

    public static Date[] createDateRange(String fromDateStr, String toDateStr) {
        Date[] range = new Date[2];
        range[0] = convertStringToDate(fromDateStr, PeriodFormatter.YMD_FORMATTER.getPattern(), false);
        range[1] = convertStringToDate(toDateStr, PeriodFormatter.YMD_FORMATTER.getPattern(), true);
        return range;
    }

    public static Date[] createDateRange(String periodStr) {
        Date[] range = new Date[2];
        range[0] = convertStringToDate(periodStr, PeriodFormatter.YM_FORMATTER.getPattern(), false);
        range[1] = toNextMonth(range[0]);
        return range;
    }
}