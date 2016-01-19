package com.git.cs309.mmoserver;

import java.util.ArrayList;
import java.util.List;

import com.git.cs309.mmoserver.connection.ConnectionAcceptor;
import com.git.cs309.mmoserver.user.UserManager;
import com.git.cs309.mmoserver.util.TickReliant;

public final class Main {
	private static volatile boolean running = false;
	private static final Object TICK_OBJECT = new Object(); // To notify threads of new tick.
	private static final List<TickReliant> TICK_RELIANT_LIST = new ArrayList<>();
	private static volatile long tickCount = 0; // Tick count.

	public static void addTickReliant(final TickReliant tickReliant) {
		synchronized (TICK_RELIANT_LIST) {
			TICK_RELIANT_LIST.add(tickReliant);
		}
	}

	public static long getTickCount() {
		return tickCount;
	}

	public static Object getTickObject() {
		return TICK_OBJECT;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				UserManager.saveAllUsers();
				System.out.println("Saved all users.");
			}
		});
		System.out.println("Starting server...");
		running = true;
		ConnectionAcceptor.startAcceptor(6667); // TODO Replace with actual port.
		int ticks = 0;
		long tickTimes = 0L;
		while (running) {
			long start = System.currentTimeMillis();
			synchronized (TICK_OBJECT) {
				TICK_OBJECT.notifyAll();
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
			if (timeLeft < 1) {
				System.err.println("Warning: Server is lagging behind desired tick time " + (-timeLeft) + "ms.");
			} else {
				try {
					Thread.sleep(timeLeft);
				} catch (InterruptedException e) {
					// Don't really care too much if it gets interrupted.
				}
			}
		}
		System.out.println("Server going down...");
	}

	public static void requestExit() {
		//For now we can just have it set running to false, but later on it should check to see which part failed, and recover if it can.
		running = false;
	}
}
