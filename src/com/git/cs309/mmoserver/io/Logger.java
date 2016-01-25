package com.git.cs309.mmoserver.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.gui.ServerGUI;
import com.git.cs309.mmoserver.util.CycleQueue;

public class Logger {
	private static final class LoggerListModel extends AbstractListModel<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3855629851629104651L;
		private static final LoggerListModel SINGLETON = new LoggerListModel();

		public static void fireContentsChanged() {
			SINGLETON.fireContentsChanged(SINGLETON, 0, outputList.size());
			ServerGUI.update();
		}

		public static LoggerListModel getSingleton() {
			return SINGLETON;
		}

		private LoggerListModel() {
			//To prevent external instantiation.
		}

		@Override
		public String getElementAt(int index) {
			return outputList.get(index);
		}

		@Override
		public int getSize() {
			return outputList.size();
		}

	}

	private static final class LoggerPrintStream extends PrintStream {
		private static LoggerPrintStream SINGLETON; // Effectively final, just needs to not be final because of the try/catch in static.
		private static final PrintStream defaultStream;
		private static volatile String pendingMessage = "";

		static {
			defaultStream = System.out;
			try {
				SINGLETON = new LoggerPrintStream();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				SINGLETON = (LoggerPrintStream) System.out;
			}
		}

		private static String ensureFileExists() {
			File logPathFile = new File(
					Config.LOG_BASE_PATH + Calendar.getInstance().get(Calendar.YEAR) + "/" + getMonthAsString() + "/");
			logPathFile.mkdirs();
			File logFile = new File(Config.LOG_BASE_PATH + Calendar.getInstance().get(Calendar.YEAR) + "/"
					+ getMonthAsString() + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " logs - "
					+ getDayAsString() + ".log");
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return logFile.getAbsolutePath();
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

		public static LoggerPrintStream getSingleton() {
			return SINGLETON;
		}

		private LoggerPrintStream() throws FileNotFoundException {
			super(new FileOutputStream(ensureFileExists(), true));
			println();
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
		public void print(String message) {
			super.print(message);
			defaultStream.print(message);
			pendingMessage += message;
		}

		@Override
		public void println() {
			super.println();
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(boolean b) {
			super.println(Logger.getTimeStamp() + " " + b);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(char c) {
			super.println(Logger.getTimeStamp() + " " + c);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(char[] s) {
			super.println(Logger.getTimeStamp() + " " + String.valueOf(s));
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(double d) {
			super.println(Logger.getTimeStamp() + " " + d);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(float f) {
			super.println(Logger.getTimeStamp() + " " + f);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(int i) {
			super.println(Logger.getTimeStamp() + " " + i);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(long l) {
			super.println(Logger.getTimeStamp() + " " + l);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(Object o) {
			super.println(Logger.getTimeStamp() + " " + o);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}

		@Override
		public void println(String message) {
			super.println(Logger.getTimeStamp() + " " + message);
			defaultStream.print('\n');
			outputList.add(pendingMessage);
			pendingMessage = "";
			LoggerListModel.fireContentsChanged();
		}
	}

	private static final CycleQueue<String> outputList = new CycleQueue<>(200, true);

	private static final Logger SINGLETON = new Logger();

	public static ListModel<String> getListModel() {
		return LoggerListModel.getSingleton();
	}

	public static PrintStream getPrintStream() {
		return LoggerPrintStream.getSingleton();
	}

	public static Logger getSingleton() {
		return SINGLETON;
	}

	public static String getTimeStamp() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int second = Calendar.getInstance().get(Calendar.SECOND);
		return "[" + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":"
				+ (second < 10 ? "0" : "") + second + "]";
	}

	private Logger() {

	}
}
