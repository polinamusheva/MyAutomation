package com.example.my_automation.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;
	
	public static Date getEndOfTodayWithOffset() {
        LocalDate localDate = LocalDate.now();

		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.systemDefault());
		int localTimezoneOffset = timeZone.getOffset(new Date().getTime()) / 1000 / 60 / 60;
		
        LocalTime localTime = LocalTime.of(0, 0, 0, 0).minusHours(localTimezoneOffset);
        localTime = localTime.minusSeconds(1);
        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
        
        return localDateTimeToDate(dateTime);
	}
	
	public static Date getStartOfTodayWithOffset() {
		LocalDate localDate = LocalDate.now();
		
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.systemDefault());
		int localTimezoneOffset = timeZone.getOffset(new Date().getTime()) / 1000 / 60 / 60;
		
		LocalTime localTime = LocalTime.of(0, 0, 0, 0).minusHours(localTimezoneOffset);
		LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
		
		return localDateTimeToDate(dateTime);
	}
	
	public static Date atStartOfDay(Date date) {
	    LocalDateTime localDateTime = dateToLocalDateTime(date);
	    LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
	    return localDateTimeToDate(startOfDay);
	}

	public static Date atEndOfDay(Date date) {
	    LocalDateTime localDateTime = dateToLocalDateTime(date);
	    LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
	    return localDateTimeToDate(endOfDay);
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
	    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
	    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDate dateToLocalDate(Date date) {
	    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date localDateToDate(LocalDate localDate) {
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static String getFormatedDate(String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return date;
	}
}