package com.git.cs309.mmoserver;

import java.util.ArrayList;
import java.util.List;

import com.git.cs309.mmoserver.characters.user.UserManager;
import com.git.cs309.mmoserver.connection.ConnectionAcceptor;
import com.git.cs309.mmoserver.gui.ServerGUI;
import com.git.cs309.mmoserver.io.Logger;
import com.git.cs309.mmoserver.util.TickReliant;

public final class Main {

	private static final List<TickReliant> TICK_RELIANT_LIST = new ArrayList<>();

	private static volatile boolean running = true;
	private static volatile boolean paused = false;
	private static volatile int pauseTimerRemaining = 0;
	private static final Object TICK_LOCK = new Object(); // To notify threads of new tick.
	private static final Object FAILURE_RESOLUTION_LOCK = new Object();
	private static volatile long tickCount = 0; // Tick count.

	public static void addTickReliant(final TickReliant tickReliant) {
		synchronized (TICK_RELIANT_LIST) {
			TICK_RELIANT_LIST.add(tickReliant);
		}
	}

	public static long getTickCount() {
		return tickCount;
	}

	public static Object getTickLock() {
		return TICK_LOCK;
	}

	public static boolean isRunning() {
		return running;
	}
	
	public static boolean wasPaused() {
		return paused;
	}

	public static void main(String[] args) {
		ServerGUI.getSingleton().setVisible(true);
		System.setOut(Logger.getPrintStream());
		System.setErr(Logger.getPrintStream());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				UserManager.saveAllUsers();
				System.out.println("Saved all users before going down.");
			}
		});
		System.out.println("Starting server...");
		ConnectionAcceptor.startAcceptor(6667); // TODO Replace with actual port.
		int ticks = 0;
		long tickTimes = 0L;
		while (running) {
			if (wasPaused() && pauseTimerRemaining-- == 0) {
				paused = false;
			}
			long start = System.currentTimeMillis();
			synchronized (TICK_LOCK) {
				TICK_LOCK.notifyAll();
			}
			boolean allFinished;
			do {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Don't really care too much if it gets interrupted.
				}
				allFinished = true;
				for (TickReliant t : TICK_RELIANT_LIST) {
					if (t.isStopped()) {
						paused = true;
						pauseTimerRemaining = Config.PAUSE_TIMER_TICKS;
						synchronized (FAILURE_RESOLUTION_LOCK) {
							try {
								FAILURE_RESOLUTION_LOCK.wait(); // Wait for debugging or error resolution.
							} catch (InterruptedException e) {
							}
						}
						break;
					}
					if (!t.tickFinished()) {
						allFinished = false;
						break;
					}
				}
			} while (!allFinished);
			long timeLeft = Config.TICK_DELAY - (System.currentTimeMillis() - start);
			tickTimes += (System.currentTimeMillis() - start);
			ticks++;
			tickCount++;
			if (ticks == Config.TICKS_PER_AUTO_SAVE) { // 5min / 400ms = 750 ticks
				System.out.println("Average tick time since last autosave: " + (tickTimes / ticks) + "ms.");
				ticks = 0;
				tickTimes = 0L;
			}
			if (timeLeft < 0) {
				System.err.println("Warning: Server is lagging behind desired tick time " + (-timeLeft) + "ms.");
				timeLeft = 5; // It must wait at least a little bit, to allow TickReliants to ready themselves for new tick.
			}
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				// Don't really care too much if it gets interrupted.
			}
		}
		System.out.println("Server going down...");
	}

	public static void notifyFailureResolution() {
		synchronized (FAILURE_RESOLUTION_LOCK) {
			FAILURE_RESOLUTION_LOCK.notifyAll();
		}
	}

	public static void requestExit() {
		//For now we can just have it set running to false, but later on it should check to see which part failed, and recover if it can.
		running = false;
	}
}
