package com.evgateway.server.utils;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.messages.Error;
import com.evgateway.server.pojo.Time;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Utils {

	static Logger logger = LoggerFactory.getLogger(Utils.class);

	public static String getTimeFormate(Double timecon) {

		int minsinsec = (int) (timecon * 60);
		int p1 = minsinsec % 60;
		int p2 = minsinsec / 60;
		int p3 = p2 % 60;
		p2 = p2 / 60;
		String str1 = String.format("%02d", p1);
		String str2 = String.format("%02d", p2);
		String str3 = String.format("%02d", p3);
		String afterConverTion = str2 + ":" + str3 + ":" + str1;
		return afterConverTion;

	}

	public static Date getUTCDate() throws ParseException {
		Date date = new Date();
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String sysDate = DateFormat.format(date);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sysDate);
	}

	public static String getUTCDateString() throws ParseException {
		Date date = new Date();
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String sysDate = DateFormat.format(date);
		return sysDate;
	}

	public static String getUtcTime() {
		Date date = new Date();
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String sysDate = DateFormat.format(date);
		return sysDate;
	}

	public static String getDateWithTimeZone(String timeZone) {

		String timeZoneNew = timeZone.substring(9, timeZone.length());

		timeZoneNew = timeZoneNew.equals("PDT") ? "PST" : timeZoneNew;
		timeZoneNew = timeZoneNew.equals("CDT") ? "CST" : timeZoneNew;
		timeZoneNew = timeZoneNew.equals("EDT") ? "EST" : timeZoneNew;

		System.out.println(timeZoneNew);

		DateFormat utcFormat = new SimpleDateFormat("E MMM dd hh:mm:ss a zzz yyyy");
		utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		utcFormat.setTimeZone(TimeZone.getTimeZone(timeZoneNew));
		Date timestamp = new Date();
		String istTime = utcFormat.format(timestamp);

		return istTime;
	}

	public static Date getDateFormate(Date date) throws ParseException {

		// Date formatteris = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

		// String parsingdDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

		// String parsingdDate = dateFormate.format(formatteris);

		// String finalDateFormate = dateFormate.format(parsingdDate);

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
	}

	public static Date getDateFrmt(Date date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}

	public static Date browserDateFormateToUtcDateFormat(String date) throws ParseException {

		Date formatteris = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(date.replace("GMT", ""));

		String parsingdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(formatteris);

		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(parsingdDate);
	}

	public static String browserDateFormate(String date) throws ParseException {

		Date formatteris = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(date.replace("GMT", ""));

		String parsingdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(formatteris);

		return parsingdDate;
	}

	public static String getRandomNum() {
		Random rand1 = new Random();
		String randomValues = String.valueOf(Math.abs(rand1.nextLong()));
		return randomValues;

	}

	public static String getIntRandomNumber() {
		Random rand1 = new Random();
		String randomValues = String.valueOf(Math.abs(rand1.nextInt()));
		return randomValues;

	}

	public static boolean getOfflineFlag(Date startTransTime) throws ParseException {

		Date currentUtcTime = getUTCDate();
		// Date currentTime = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").parse(currentUtcTime);
		// Date startTransactionTime = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").parse(startTransTime);
		logger.info("utcTimeNow>>>" + currentUtcTime);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(currentUtcTime.getTime() - startTransTime.getTime());

		if (minutes > 10)
			return true;

		return false;
	}

	public static Map<String, Double> getTimeDifferenceInMiliSec(Date startTimeStamp, Date endTimeStamp)
			throws ParseException {

		// String startdateParsingFormate = "yyyy-MM-dd HH:mm:ss";
		//
		// String endDateParsingFormate = "yyyy-MM-dd HH:mm:ss";
		//
		// if (startTime.contains("T")) {
		// startdateParsingFormate = "yyyy-MM-dd'T'HH:mm:ss";
		// }
		// if (EndTime.contains("T")) {
		// endDateParsingFormate = "yyyy-MM-dd'T'HH:mm:ss";
		// }

		// Date startTimeStamp = new
		// SimpleDateFormat(startdateParsingFormate).parse(startTime);
		// Date endTimeStamp = new
		// SimpleDateFormat(endDateParsingFormate).parse(EndTime);

		Map<String, Double> timeConvertMap = new HashMap<String, Double>();

		// Time difference in Miliseconds
		Long timeDifference = endTimeStamp.getTime() - startTimeStamp.getTime();

		// converting to miliseconds to seconds
		double seconds = Math.abs(TimeUnit.MILLISECONDS.toSeconds(timeDifference));
		// converting to miliseconds to minutes
		// double minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
		// converting to miliseconds to Hours
		double hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
		double totalDurationHours = (seconds / 3600);
		double totalDurationInminutes = (seconds / 60);

		timeConvertMap.put("Seconds", seconds);
		timeConvertMap.put("Minutes", totalDurationInminutes);
		timeConvertMap.put("Hours", hours);
		timeConvertMap.put("durationInHours", totalDurationHours);

		timeConvertMap.put("timeDifference", Double.valueOf(timeDifference));
		// System.out.println("fianlMapData>131>>"+timeConvertMap);

		return timeConvertMap;
	}

	public Map<String, Double> getTimeInHours_duration(String startTime, String EndTime) throws ParseException {

		Date startTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(startTime);
		Date endTimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(EndTime);

		Map<String, Double> timeConvertMap = new HashMap<String, Double>();

		double MinutesForSessionElapsed = 0.0;

		long diff = endTimeStamp.getTime() - startTimeStamp.getTime();

		long diffSeconds = diff / 1000 % 60;
		// int seconds = (int) diffSeconds;
		long diffMinutes = diff / (60 * 1000) % 60;// (diff/(6000)%60)

		long diffHours = diff / (60 * 60 * 1000);

		double Mins = (double) (diffHours + ((double) diffMinutes / 60) + ((double) diffSeconds / (60 * 60)));
		double MinutesForSessionElapsedForMaxSession = (double) (diffHours * 60 + ((double) diffMinutes)
				+ ((double) diffSeconds / (60)));
		if (diffSeconds >= 30.0) {
			System.out.println("sysinside if>>>660>>>>>" + diffSeconds);
			Mins = (double) (diffHours + ((double) diffMinutes / 60) + ((double) diffSeconds / (60 * 60)));
			MinutesForSessionElapsed = (double) (diffHours * 60 + ((double) diffMinutes)
					+ ((double) diffSeconds / (60)));
		} else {
			System.out.println("sysinside else>>>663>>>>" + diffSeconds);
			Mins = (double) (diffHours + ((double) diffMinutes / 60));
			MinutesForSessionElapsed = (double) (diffHours * 60 + ((double) diffMinutes));
		}

		timeConvertMap.put("durationInHours", Mins);
		timeConvertMap.put("MinutesForSessionElapsed", MinutesForSessionElapsed);
		timeConvertMap.put("MinutesForSessionElapsedForMaxSession", MinutesForSessionElapsedForMaxSession);

		return timeConvertMap;

	}

	public static Double decimalwithtwodecimals(Double final_Cost) {
		try {
			final_Cost = Double.valueOf(new DecimalFormat("##.##").format(final_Cost));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return final_Cost;
	}

	public static <T> T jsonToObject(Object map, Class<T> type) {
		T target = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(map);
			target = objectMapper.readValue(json, type);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return target;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonArrayToObjectList(Object map, Class<T> tClass) {
		List<T> target = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
			String json = objectMapper.writeValueAsString(map);
			target = (List<T>) objectMapper.readValue(json, listType);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return target;

	}

	public static String getOTP() throws InvalidKeyException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		int time1 = Calendar.getInstance().get(Calendar.SECOND);
		int time2 = Calendar.getInstance().get(Calendar.MILLISECOND);
		if (time1 == 00 || time2 == 000) {
			time1 = 25;
			time2 = 250;
		}
		String pass = "12345678901234567890";
		byte[] code = pass.getBytes();
		return OTP.generateOTP(code, time1, 6, false, time2);

	}

	public static String addRemoveDays(String dt, String timezone) throws ParseException {
		int n = 0;
		char t = '-';
		char ct = timezone.charAt(3);
		n = (ct == t) ? 1 : -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt));
		c.add(Calendar.DATE, n); // number of days to add

		return sdf.format(c.getTime());
	}

	public static Date stringToDate(String val) {
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = dateFormat.parse(val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String stringToDate(Date date) {
		String val = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			val = dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public static boolean isPathValid(String path) {
		boolean flag = false;
		try {
			File file = new File(path);
			if (file.exists()) {
				flag = true;
			}
		} catch (Exception ex) {
			flag = false;
		}
		return flag;
	}

	public static Pair<String, String> validateParmas(String id, String filter) throws ParseException, ServerException {

		id = id != null && id.equals("undefined") ? "0" : id;

		if (filter != null) {
			filter = filter.replace("'", "''");

			if (!Pattern.matches("^[ A-Za-z0-9_@.&*',!$#+-]*$", filter))
				throw new ServerException(Error.INVALID_SEARCH.toString(),
						Integer.toString(Error.INVALID_SEARCH.getCode()));

		}

		return new Pair<>(id, filter);

	}

	public static boolean isHebrew(String s) {
		int len = s.length();
		for (int i = 0; i < len; ++i) {
			if (isHebrew(s.codePointAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isHebrew(int codePoint) {
		return Character.UnicodeBlock.HEBREW.equals(Character.UnicodeBlock.of(codePoint));
	}

	public static String getUUIDString() {
		// TODO Auto-generated method stub

		UUID uuid = UUID.randomUUID();

		return uuid.toString();

	}

	public static String decimalwithtwoZeros(Double final_Cost) {

		String finalcostString = String.valueOf(final_Cost);
		try {
			if (finalcostString.substring(finalcostString.indexOf(".")).length() == 2) {
				finalcostString = finalcostString + "0";
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return finalcostString;
	}

	public static Time subOrAddTime(String startTimes, Long hours, Long minutes) {
		// TODO Auto-generated method stub

		SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");
		Date d1 = new Date();
		sdff.format(d1);
		try {
			d1 = sdff.parse(startTimes);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		// remove next line if you're always using the current time.
		cal.setTime(d1);
		cal.add(Calendar.HOUR, hours.intValue());
		cal.add(Calendar.MINUTE, minutes.intValue());
		Date oneHourBack = cal.getTime();
		return new Time(oneHourBack.getHours(), oneHourBack.getMinutes(), 0);
	}

	public static String subOrAddTimeGet(Time time, Long hours, Long minutes) {
		// TODO Auto-generated method stub

		SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");
		Date d1 = new Date();
		sdff.format(d1);
		try {
			d1 = sdff.parse(convertTimeToString(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("date :" + d1);
		Calendar cal = Calendar.getInstance();
		// remove next line if you're always using the current time.
		cal.setTime(d1);
		cal.add(Calendar.HOUR, hours.intValue());
		cal.add(Calendar.MINUTE, minutes.intValue());
		Date oneHourBack = cal.getTime();

		String min = oneHourBack.getMinutes() <= 9 ? "0" + oneHourBack.getMinutes() : oneHourBack.getMinutes() + "";
		String hourss = oneHourBack.getHours() <= 9 ? "0" + oneHourBack.getHours() : oneHourBack.getHours() + "";

		return "" + hourss + ":" + min + "";
	}

	public static boolean isEmpty(String value) {

		return (value == null || value.trim().equals("") || value.isEmpty()) ? true : false;
	}

	public static Date convertTimeToString(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date d1 = sdf.parse(time);

			return d1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String convertTimeToString(Time time) {

//		Long hour = time.getHour();
//		Long mins = time.getMin();
//
//		LocalTime localTime = LocalTime.of(hour.intValue(), mins.intValue());
//
//		LocalDate today = LocalDate.now();
//		// then use the date and the time object to create a zone-aware datetime object
//		ZonedDateTime zdt = LocalDateTime.of(today, localTime).atZone(ZoneOffset.UTC);
//
//		String min = zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getMinute() <= 9
//				? "0" + zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getMinute()
//				: zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getMinute() + "";
//		String hours = zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getHour() <= 9
//				? "0" + zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getHour()
//				: zdt.withZoneSameInstant(ZoneId.of(userTimeZone)).getHour() + "";
		String min = time.getMin() <= 9 ? "0" + time.getMin() : time.getMin() + "";
		String hourss = time.getHour() <= 9 ? "0" + time.getHour() : time.getHour() + "";

		return "" + hourss + ":" + min + "";

	}

	public static Time convertStringToTime(String time, String userTimeZone) {

		LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
		// receive the date of today
		LocalDate today = LocalDate.now();
		// then use the date and the time object to create a zone-aware datetime object
		ZonedDateTime zdt = LocalDateTime.of(today, localTime).atZone(ZoneId.of(userTimeZone));
		// print it

		return new Time(zdt.toInstant().atZone(ZoneOffset.UTC).getHour(),
				zdt.toInstant().atZone(ZoneOffset.UTC).getMinute(), 0);

	}

	public static boolean checkTimeInterval(String firstTime, String secondTime, String betweenTime) {
		try {

			Date time1 = new SimpleDateFormat("HH:mm").parse(firstTime);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);
			calendar1.add(Calendar.DATE, 1);

			Date time2 = new SimpleDateFormat("HH:mm").parse(secondTime);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
			calendar2.add(Calendar.DATE, 1);

			Date d = new SimpleDateFormat("HH:mm").parse(betweenTime);
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(d);
			calendar3.add(Calendar.DATE, 1);

			Date x = calendar3.getTime();

			if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
				// checkes whether the current time is between 14:49:00 and 20:11:13.
				return true;
			}

//			if (x.equals(calendar1.getTime()) || x.equals(calendar2.getTime())) {
//				// checkes whether the current time is between 14:49:00 and 20:11:13.
//				return true;
//			}

		} catch (ParseException e) {
		}
		return false;
	}

	public static long isDayChangedGet(Time startTime, String userTimeZone) {
		// TODO Auto-generated method stub

		Long hour = startTime.getHour();
		Long mins = startTime.getMin();

		LocalTime localTime = LocalTime.of(hour.intValue(), mins.intValue());

		LocalDate today = LocalDate.now();
		// then use the date and the time object to create a zone-aware datetime object
		ZonedDateTime zdt = LocalDateTime.of(today, localTime).atZone(ZoneOffset.UTC);

		ZonedDateTime Userzdt = zdt.withZoneSameInstant(ZoneId.of(userTimeZone));

		System.out.println("differe " + (zdt.getDayOfMonth() - Userzdt.getDayOfMonth()));
		return zdt.getDayOfMonth() - Userzdt.getDayOfMonth();

	}

	public static long isDayChangedUpdate(Time startTime, String userTimeZone) {
		// TODO Auto-generated method stub

		Long hour = startTime.getHour();
		Long mins = startTime.getMin();

		LocalTime localTime = LocalTime.of(hour.intValue(), mins.intValue());

		LocalDate today = LocalDate.now();
		// then use the date and the time object to create a zone-aware datetime object
		ZonedDateTime zdt = LocalDateTime.of(today, localTime).atZone(ZoneId.of(userTimeZone));

		ZonedDateTime Userzdt = zdt.withZoneSameInstant(ZoneOffset.UTC);

		return Userzdt.getDayOfMonth() - zdt.getDayOfMonth();

	}

	public static boolean checkTimeIntervalForDrGroup(String StartTime1, String StartTime2, String EndTime1,
			String EndTime2) {
		try {

			Date startDate1 = new SimpleDateFormat("HH:mm").parse(StartTime1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(startDate1);
			calendar1.add(Calendar.DATE, 1);

			Date startX = calendar1.getTime();

			Date startDate2 = new SimpleDateFormat("HH:mm").parse(StartTime2);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(startDate2);
			calendar2.add(Calendar.DATE, 1);

			Date endDate1 = new SimpleDateFormat("HH:mm").parse(EndTime1);
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(endDate1);
			calendar3.add(Calendar.DATE, 1);

			Date endX = calendar3.getTime();

			Date endDate2 = new SimpleDateFormat("HH:mm").parse(EndTime2);
			Calendar calendar4 = Calendar.getInstance();
			calendar4.setTime(endDate2);
			calendar4.add(Calendar.DATE, 1);

			if (startX.equals(calendar2.getTime()) && endX.equals(calendar4.getTime()))
				return true;

		} catch (ParseException e) {
		}
		return false;
	}

	public static String addHourStrCSTss(int n, Date date) {
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(date); // sets calendar time/date
		cal.add(Calendar.HOUR, n); // adds one hour
		Date time = cal.getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
		// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(time);
	}

	public static String randomGenerateString() {
		Random random = new Random();
		long randomValue = random.nextLong() % 1000000000L;
		if (randomValue < 0)
			randomValue *= -1;
		String randomString = "BCH010" + String.format("%09d", randomValue);
		return randomString;
	}

	public static Map<String, Object> getUserCoordinates(String timeZone) {
		System.out.println("getUserCoordinates()--timeZone : " + timeZone);
		Map<String, Object> userMap = new HashMap<>();
		if (timeZone.equalsIgnoreCase("GMT 0000 CUT") || timeZone.equalsIgnoreCase("GMT 0100 G00")
				|| timeZone.equalsIgnoreCase("GMT 0100 BST") || timeZone.equalsIgnoreCase("GMT 0000 GMT")) {
			userMap.put("Hours", "+00");
			userMap.put("Minutes", "00");
			userMap.put("timeZoneFormat", "UTC");
		} else if (timeZone.equalsIgnoreCase("GMT 0530 IST")) {

			userMap.put("Hours", "+05");
			userMap.put("Minutes", "30");
			userMap.put("timeZoneFormat", "IST");
		} else {
			int length = timeZone.length();
//			String intialString = StringUtils.substringBetween(timeZone, "GMT", " ");

			String sign = (String) timeZone.subSequence(3, 4);
			String hoursb = sign + timeZone.substring(4, 6);
			String minutesb = sign + timeZone.substring(6, 8);
			String timeFormat = timeZone.substring(9, length);

//			String timeFormat = StringUtils.substringAfter(timeZone, tZone);

			userMap.put("Hours", hoursb);
			userMap.put("Minutes", minutesb);
			userMap.put("timeZoneFormat", timeFormat);

		}
		System.out.println("userMap : " + userMap);
		return userMap;
	}
	public static String convertTime(String time) {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    try {
	        Date d1 = sdf.parse(time);
	        return sdf.format(d1); // Format the Date back to a string and return it
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static String subOrAddTimeString(String startTimes, Long hours, Long minutes) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		// Parse the start time string to a LocalTime object
		LocalTime time = LocalTime.parse(startTimes, formatter);

		// Add or subtract the hours and minutes
		time = time.plusHours(hours).plusMinutes(minutes);

		// Format the time back to a string
		return time.format(formatter);
	}
	public static String subOrAddTimeToString(String time, Long hours, Long minutes) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    
	    // Parse the input time string to a LocalTime object
	    LocalTime parsedTime = LocalTime.parse(time, formatter);
	    
	    // Add the specified hours and minutes to the parsed time
	    LocalTime adjustedTime = parsedTime.plusHours(hours).plusMinutes(minutes);
	    
	    // Format the adjusted time back to a string
	    return adjustedTime.format(formatter);
	}
}
