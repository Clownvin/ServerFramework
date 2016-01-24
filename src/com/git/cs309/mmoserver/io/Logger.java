package com.git.cs309.mmoserver.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.util.CycleQueue;

public class Logger extends PrintStream {
	private static Logger SINGLETON; // Effectively final, just needs to not be final because of the try/catch in static.
	private static final PrintStream defaultStream;
	private static final CycleQueue<String> outputList = new CycleQueue<>(1000, true);
	private static volatile String pendingMessage = "";

	static {
		defaultStream = System.out;
		try {
			SINGLETON = new Logger();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			SINGLETON = (Logger) System.out;
		}
	}
	
	public static Logger getLogger() {
		return SINGLETON;
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
	
	private static String ensureFileExists() {
		File logPathFile = new File(Config.LOG_BASE_PATH + Calendar.getInstance().get(Calendar.YEAR) + "/" + getMonthAsString() + "/");
		logPathFile.mkdirs();
		File logFile = new File(Config.LOG_BASE_PATH + Calendar.getInstance().get(Calendar.YEAR) + "/" + getMonthAsString() + "/"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " logs - " + getDayAsString() + ".log");
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logFile.getAbsolutePath();
	}

	private Logger() throws FileNotFoundException {
		super(ensureFileExists());
	}

	@Override
	public void print(String message) {
		super.print(message);
		defaultStream.print(message);
		pendingMessage += message;
	}
	
	@Override
	public void print(boolean b) {
		super.print(b);
		defaultStream.print(b);
		pendingMessage += b;
	}
	
	@Override
	public void print(char c) {
		super.print(c);
		defaultStream.print(c);
		pendingMessage += c;
	}
	
	@Override
	public void print(char[] s) {
		super.print(s);
		defaultStream.print(s);
		pendingMessage += String.valueOf(s);
	}
	
	@Override
	public void print(double d) {
		super.print(d);
		defaultStream.print(d);
		pendingMessage += d;
	}
	
	@Override
	public void print(float f) {
		super.print(f);
		defaultStream.print(f);
		pendingMessage += f;
	}
	
	@Override
	public void print(int i) {
		super.print(i);
		defaultStream.print(i);
		pendingMessage += i;
	}
	
	@Override
	public void print(long l) {
		super.print(l);
		defaultStream.print(l);
		pendingMessage += l;
	}
	
	@Override
	public void print(Object o) {
		super.print(o);
		defaultStream.print(o);
		pendingMessage += o;
	}

	@Override
	public void println(String message) {
		super.println(message);
		defaultStream.print('\n');
		pendingMessage += message;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println() {
		super.println();
		defaultStream.print('\n');
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(boolean b) {
		super.println(b);
		defaultStream.print('\n');
		pendingMessage += b;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(char c) {
		super.println(c);
		defaultStream.print('\n');
		pendingMessage += c;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(char[] s) {
		super.println(s);
		defaultStream.print('\n');
		pendingMessage += String.valueOf(s);
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(double d) {
		super.println(d);
		defaultStream.print('\n');
		pendingMessage += d;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(float f) {
		super.println(f);
		defaultStream.print('\n');
		pendingMessage += f;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(int i) {
		super.println(i);
		defaultStream.print('\n');
		pendingMessage += i;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(long l) {
		super.println(l);
		defaultStream.print('\n');
		pendingMessage += l;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
	
	@Override
	public void println(Object o) {
		super.println(o);
		defaultStream.print('\n');
		pendingMessage += o;
		outputList.add(pendingMessage);
		pendingMessage = "";
	}
}
