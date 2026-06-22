package com.cms.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateAndTime {

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm a");
	
	public static String format(LocalDateTime dateTime)
	{
		if(dateTime == null)
		{
			return "";
		}
		return dateTime.format(dateTimeFormatter);
	}
}
