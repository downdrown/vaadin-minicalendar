package org.vaadin.addons.minicalendar;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static com.vaadin.flow.internal.LocaleUtil.getLocale;

/**
 * Encapsulates available configurations for a {@link MiniCalendar} instance.
 *
 * @author Manfred Huber
 */
final class MiniCalendarConfiguration {

    private DayOfWeek firstDayOfWeek = getFirstDayOfWeekByLocale(getLocale());
    private TextStyle dayTextStyle = TextStyle.SHORT_STANDALONE;
    private TextStyle monthTextStyle = TextStyle.FULL;
    private boolean allowDeselection = true;
    private boolean showWeekNumbers = false;

    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public TextStyle getDayTextStyle() {
        return dayTextStyle;
    }

    public void setDayTextStyle(TextStyle dayTextStyle) {
        this.dayTextStyle = dayTextStyle;
    }

    public TextStyle getMonthTextStyle() {
        return monthTextStyle;
    }

    public void setMonthTextStyle(TextStyle monthTextStyle) {
        this.monthTextStyle = monthTextStyle;
    }

    public boolean isAllowDeselection() {
        return allowDeselection;
    }

    public void setAllowDeselection(boolean allowDeselection) {
        this.allowDeselection = allowDeselection;
    }

    public boolean isShowWeekNumbers() {
        return showWeekNumbers;
    }

    public void setShowWeekNumbers(boolean showWeekNumbers) {
        this.showWeekNumbers = showWeekNumbers;
    }

    private static DayOfWeek getFirstDayOfWeekByLocale(Locale locale) {
        return WeekFields.of(locale).getFirstDayOfWeek();
    }
}
