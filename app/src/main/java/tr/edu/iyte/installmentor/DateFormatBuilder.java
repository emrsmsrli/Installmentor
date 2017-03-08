package tr.edu.iyte.installmentor;

import android.text.TextUtils;

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
        initFormat();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TextUtils.isEmpty(format) ? DEFAULT_FORMAT : format.trim(), Locale.getDefault());
        return dateFormat.format(date);
    }

    public void includeTime(boolean includeSeconds) {
        isTimeIncluded = true;
        areSecondsIncluded = includeSeconds;
    }

    public void includeDate(boolean includeDayInWeek) {
        isDateIncluded = true;
        isDayInWeekIncluded = includeDayInWeek;
    }

    public static DateFormatBuilder getBuilder() {
        return new DateFormatBuilder();
    }
}
