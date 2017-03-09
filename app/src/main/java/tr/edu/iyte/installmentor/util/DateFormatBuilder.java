package tr.edu.iyte.installmentor.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class DateFormatBuilder {
    private static final String DAY_IN_WEEK = "EEE, ";
    private static final String DAY = "dd";
    private static final String MONTH = "MM";
    private static final String YEAR = "yyyy";
    private static final String HOUR = "HH";
    private static final String MINUTES = "mm";
    private static final String SECONDS = ":ss ";
    private static final String DATE = String.format(Locale.getDefault(), "%s.%s.%s ", DAY, MONTH, YEAR);
    private static final String TIME = String.format(Locale.getDefault(), "%s:%s ", HOUR, MINUTES);
    private static final String DEFAULT_FORMAT = String.format(Locale.getDefault(), "%s%s", DATE, TIME);

    private String format;
    private boolean isTimeIncluded;
    private boolean areSecondsIncluded;
    private boolean isDateIncluded;
    private boolean isDayInWeekIncluded;

    private DateFormatBuilder() {
        format = "";
        isTimeIncluded = false;
        areSecondsIncluded = false;
        isDateIncluded = false;
        isDayInWeekIncluded = false;
    }

    private void initFormat() {
        if(isDayInWeekIncluded)
            format += DAY_IN_WEEK;
        if(isDateIncluded)
            format += DATE;
        if(isTimeIncluded) {
            format += TIME;
            if(areSecondsIncluded)
                format += SECONDS;
        }
    }

    public String buildFormat(Date date) {
        //initFormat();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TextUtils.isEmpty(format) ? DEFAULT_FORMAT : format.trim(), Locale.getDefault());
        return dateFormat.format(date);
    }

    public DateFormatBuilder includeTime(boolean includeSeconds) {
        isTimeIncluded = true;
        areSecondsIncluded = includeSeconds;
        return this;
    }

    public DateFormatBuilder includeDate(boolean includeDayInWeek) {
        isDateIncluded = true;
        isDayInWeekIncluded = includeDayInWeek;
        return this;
    }

    @Nullable
    public static Date parse(String date) {
        try {
            //TODO implement custom format parsing
            SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT , Locale.getDefault());
            return dateFormat.parse(date);
        } catch(ParseException e) {
            return null;
        }
    }

    public static DateFormatBuilder getBuilder() {
        return new DateFormatBuilder();
    }
}
