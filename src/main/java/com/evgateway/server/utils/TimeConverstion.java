package com.evgateway.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.form.FilterForm;
import com.evgateway.server.messages.Error;

public class TimeConverstion {

	public static String getTimeConversion(Double timecon) {

		int minsinsec = (int) (timecon * 60);
		int p1 = minsinsec % 60;
		int p2 = minsinsec / 60;
		int p3 = p2 % 60;
		p2 = p2 / 60;
		String str1 = String.format("%02d", p1);
		String str2 = String.format("%02d", p2);
		String str3 = String.format("%02d", p3);
		String afterTime = str2 + ":" + str3 + ":" + str1;
		return afterTime;

	}

	public static String getCurrentUTCDateTime() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utctime = dateFormat.format(date);
		return utctime;
	}

	public static String convertUTCDateTime(Date selectedDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utctime = dateFormat.format(selectedDate);
		return utctime;
	}

	public static String getDurationTimeFormat(long time) {
		String DurationHrsStr = null;
		String DurationMinsStr = null;
		String DurationSecsStr = null;

		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(time);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(time);
		// long diffInHours = TimeUnit.MILLISECONDS.toHours(time);
		// long diffInDays = TimeUnit.MILLISECONDS.toDays(time);

		long DurationHrs = diffInSeconds / 3600;
		long DurationMins = diffInMinutes % 60;
		long DurationSecs = diffInSeconds % 60;

		if (DurationHrs < 10) {
			DurationHrsStr = "0" + Long.toString(DurationHrs);
		} else {
			DurationHrsStr = Long.toString(DurationHrs);
		}

		if (DurationMins < 10) {
			DurationMinsStr = "0" + Long.toString(DurationMins);
		} else {
			DurationMinsStr = Long.toString(DurationMins);
		}

		if (DurationSecs < 10) {
			DurationSecsStr = "0" + Long.toString(DurationSecs);
		} else {
			DurationSecsStr = Long.toString(DurationSecs);
		}

		return DurationHrsStr + ":" + DurationMinsStr + ":" + DurationSecsStr;
	}

	public static void presentAndLastDate(FilterForm filterForm) throws ServerException, ParseException {

		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		filterForm.setEndDate(outputFormat.format(calendar.getTime()));
		calendar.add(Calendar.DATE, 1);
		if (filterForm.getRange().equals("1")) {
			calendar.add(Calendar.DATE, -1);
			filterForm.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filterForm.getRange().equals("7")) {
			calendar.add(Calendar.DATE, -7);
			filterForm.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filterForm.getRange().equals("14")) {
			calendar.add(Calendar.DATE, -14);
			filterForm.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filterForm.getRange().equals("30")) {
			calendar.add(Calendar.DATE, -30);
			filterForm.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filterForm.getRange().equals("365")) {
			calendar.add(Calendar.DATE, -365);
			filterForm.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filterForm.getRange().equals("custom")) {
			if (filterForm.getStartDate() == null || filterForm.getStartDate() == "") {
				throw new ServerException(Error.PRESENT_AND_LAST_TIME.toString(),
						Integer.toString(Error.PRESENT_AND_LAST_TIME.getCode()));
			} else if (filterForm.getEndDate() == null || filterForm.getEndDate() == "") {
				throw new ServerException(Error.PRESENT_AND_LAST_TIME1.toString(),
						Integer.toString(Error.PRESENT_AND_LAST_TIME1.getCode()));
			} else {
				filterForm.setStartDate(outputFormat.format(outputFormat.parse(filterForm.getStartDate())));
				filterForm.setEndDate(outputFormat.format(outputFormat.parse(filterForm.getEndDate())));
			}
		}

	}

	public static String calculateDurationBWDates(String startDate, String endDate, String dateWithTimeFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateWithTimeFormat);
		String diffTimeBetweenDates = "";
		if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {

			Date starttime = null, endtime = null;
			try {
				starttime = sdf.parse(startDate.replace(".0", ""));
				endtime = sdf.parse(endDate.replace(".0", ""));

				/* Duration Calculation Starts */
				long diff = endtime.getTime() - starttime.getTime();
				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;

				String hrs = String.valueOf(diffHours);
				String mins = String.valueOf(diffMinutes);
				String secs = String.valueOf(diffSeconds);

				hrs = hrs.length() == 1 ? "0" + hrs : hrs;
				mins = mins.length() == 1 ? "0" + mins : mins;
				secs = secs.length() == 1 ? "0" + secs : secs;

				diffTimeBetweenDates = hrs + ":" + mins + ":" + secs;

			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return diffTimeBetweenDates == "" ? "00:00:00" : diffTimeBetweenDates;
	}

	public static List<String> lastMonth(int period) {

		List<String> allDates = new ArrayList<>();
		try {
			SimpleDateFormat monthDate = new SimpleDateFormat("MMMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthDate.parse(monthDate.format(new Date())));
			for (int i = 1; i <= period; i++) {
				String month_name1 = monthDate.format(cal.getTime());
				allDates.add(month_name1);
				cal.add(Calendar.MONTH, -1);
			}
			Collections.reverse(allDates);
			return allDates;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}

		return allDates;
	}

	public static String convertHHMMSS(double min) {

		double temp = min * 60;

		double hours = Math.floor(temp / 3600);
		temp %= 3600;
		double minutes = Math.floor(temp / 60);
		double seconds = temp % 60;

		String hrs = String.valueOf((int) hours);
		String mins = String.valueOf((int) minutes);
		String secs = String.valueOf((int) seconds);

		hrs = hrs.length() == 1 ? "0" + hrs : hrs;
		mins = mins.length() == 1 ? "0" + mins : mins;
		secs = secs.length() == 1 ? "0" + secs : secs;

		String diffTimeBetweenDates = hrs + ":" + mins + ":" + secs;

		return diffTimeBetweenDates;
	}

	public static double decimal3digit(double value) {

		//BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
		//double salary = bd.doubleValue();
		double salary = Math.round(value * 100.0) / 100.0;

		return salary;
	}
}
