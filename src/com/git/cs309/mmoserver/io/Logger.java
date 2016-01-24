package com.git.cs309.mmoserver.io;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.util.CycleQueue;

public class Logger extends PrintStream {
	private static Logger SINGLETON; // Effectively final, just needs to not be final because of the try/catch in static.

	private static final CycleQueue<String> outputList = new CycleQueue<>(1000, true);
	private static volatile String pendingMessage = "";

	static {
		try {
			SINGLETON = new Logger();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			SINGLETON = (Logger) System.out;
		}
	}

	private static String getDayAsString() {
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			return "January";
		case Calendar.MONDAY:
			return "Monday";
		case Calendar.TUESDAY:
			return "Tuesday";
		case Calendar.WEDNESDAY:
			return "Wednesday";
		case Calendar.THURSDAY:
			return "Thursday";
		case Calendar.FRIDAY:
			return "Friday";
		case Calendar.SATURDAY:
			return "Saturday";
		}
		return "Null";
	}

	private static String getMonthAsString() {
		switch (Calendar.getInstance().get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			return "January";
		case Calendar.FEBRUARY:
			return "February";
		case Calendar.MARCH:
			return "March";
		case Calendar.APRIL:
			return "April";
		case Calendar.MAY:
			return "May";
		case Calendar.JUNE:
			return "June";
		case Calendar.JULY:
			return "July";
		case Calendar.AUGUST:
			return "August";
		case Calendar.SEPTEMBER:
			return "September";
		case Calendar.OCTOBER:
			return "October";
		case Calendar.NOVEMBER:
			return "November";
		case Calendar.DECEMBER:
			return "December";
		}
		return "Null";
	}

	public static Logger getSingleton() {
		return SINGLETON;
	}

	private Logger() throws FileNotFoundException {
		super(Config.LOG_BASE_PATH + Calendar.getInstance().get(Calendar.YEAR) + "/" + getMonthAsString() + "/"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " logs - " + getDayAsString() + ".log");
	}

	@Override
	public void print(String message) {
		super.print(message);
		pendingMessage += message;
	}

	@Override
	public void println(String message) {
		super.println(message);
		pendingMessage += message;
		outputList.add(message);
	}
}
