package com.evgateway.server.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserTimeZoneConvertion {

	public Map<String, Object> getUserCoordinates(String timeZone) {
		System.out.println("TTTTTTTTTTTTTT:"+timeZone);
		Map<String, Object> userMap = new HashMap<>();
		if(timeZone.equalsIgnoreCase("GMT 0000 CUT") || timeZone.equalsIgnoreCase("GMT 0100 G00")
		|| timeZone.equalsIgnoreCase("GMT 0100 BST")|| timeZone.equalsIgnoreCase("GMT 0000 GMT")) {
			userMap.put("Hours", "+00");
			userMap.put("Minutes", "00");
			userMap.put("timeZoneFormat", "UTC");
		}else if(timeZone.equalsIgnoreCase("GMT 0530 IST") || timeZone == "GMT 0530 IST") {
			userMap.put("Hours", "+05");
			userMap.put("Minutes", "30");
			userMap.put("timeZoneFormat", "IST");
		}else {
			String intialString = StringUtils.substringBetween(timeZone, "GMT", " ");
			String sign = (String) intialString.subSequence(0, 1);
			String hoursb = sign + intialString.substring(1, 3);
			String minutesb = sign + intialString.substring(3, 5);
			String timeFormat = StringUtils.substringAfter(timeZone, " ");
		
			userMap.put("Hours", hoursb);
			userMap.put("Minutes", minutesb);
			userMap.put("timeZoneFormat", timeFormat);
		}
		return userMap;
	}

	public Map<String, Object> getUserCoordinatesWithSapce(String timeZone) {
		Map<String, Object> userMap = new HashMap<>();
		System.out.println("timeZone +++++++++++ "+timeZone);
		if(timeZone.equalsIgnoreCase("GMT 0000 CUT") || timeZone == "GMT 0000 CUT") {
			userMap.put("Hours", "+00");
			userMap.put("Minutes", "00");
			userMap.put("timeZoneFormat", "UTC");
		}else {
			String timeZoneNew = timeZone.substring(0, timeZone.indexOf(' ')) + "+"
					+ timeZone.substring(timeZone.indexOf(' ') + 1);
			String intialString = StringUtils.substringBetween(timeZoneNew, "GMT", " ");
			String sign = (String) intialString.subSequence(0, 1);
			String hoursb = sign + intialString.substring(1, 3);
			String minutesb = sign + intialString.substring(3, 5);
			String timeFormat = StringUtils.substringAfter(timeZoneNew, " ");

			userMap.put("Hours", hoursb);
			userMap.put("Minutes", minutesb);
			userMap.put("timeZoneFormat", timeFormat);
		}
		return userMap;
	}

	public String timeZoneToUTC(String timeZone) {

		timeZone = timeZone.equals("PDT") ? "PST" : timeZone;

		Date date = new Date();

		DateFormat cstFormat = new SimpleDateFormat("'GMT'Z");
		TimeZone cstTime = TimeZone.getTimeZone(timeZone);
		cstFormat.setTimeZone(cstTime);

		return cstFormat.format(date) + " " + timeZone;

	}

}
