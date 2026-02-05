package com.example.my_automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TestUtils {
	
	private static final Logger log = LoggerFactory.getLogger(TestUtils.class);
	
	public static void sleep(int milliseconds) {
		try {
			log.debug("Sleep for {} seconds", (float)milliseconds/1000);
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
}