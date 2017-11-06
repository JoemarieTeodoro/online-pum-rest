package com.ph.ibm.util;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import com.ph.ibm.opum.exception.OpumException;

public class CalendarUtils {
	
	/**
	 * Get the month string based on a given index
	 * 
	 * @param monthIndex 
	 * @param textStyle see {@link java.time.format.TextStyle}
	 * @throws OpumException
	 */
	public static String getMonthString(int monthIndex, TextStyle textStyle) throws IllegalArgumentException {
		if (!isValidMonthIndex(monthIndex)) {
			throw new IllegalArgumentException("Invalid month value");
		}
		Month month = Month.of(monthIndex);
		return month.getDisplayName(textStyle, Locale.getDefault()).toUpperCase();
	}
	
	/**
	 * Java 8's enum for the first day of the week is Monday to Sunday, day
	 * 
	 * @param dayIndex Monday = 1, Sunday = 7
	 * @param textStyle see {@link java.time.format.TextStyle}
	 * @throws OpumException
	 */
	public static String getDayString(int dayIndex, TextStyle textStyle) throws IllegalArgumentException {
		if (!isValidDayIndex(dayIndex)) {
			throw new IllegalArgumentException("Invalid day value from JSON");
		}
		DayOfWeek day = DayOfWeek.of(dayIndex);
		return day.getDisplayName(textStyle, Locale.getDefault());
	}
	
	static boolean isValidDayIndex(int dayIndex) {
		return 1 <= dayIndex && dayIndex <= 7;
	}

	static boolean isValidMonthIndex(int monthIndex) {
		return 1 <= monthIndex && monthIndex <= 12;
	}
	
}
