package com.openpeer.sample.util;

import java.text.DateFormat;

import android.text.format.DateUtils;

public class DateFormatUtils {
	public static CharSequence getSameDayTime(Long time) {
		return DateUtils.formatSameDayTime(time, System.currentTimeMillis(), DateFormat.SHORT, DateFormat.SHORT);
	}

}
