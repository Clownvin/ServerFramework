package com.git.cs309.mmoserver;

import java.util.ArrayList;
import java.util.List;

import com.git.cs309.mmoserver.characters.user.UserManager;
import com.git.cs309.mmoserver.connection.ConnectionAcceptor;
import com.git.cs309.mmoserver.gui.ServerGUI;
import com.git.cs309.mmoserver.io.Logger;
import com.git.cs309.mmoserver.util.TickProcess;

/**
 * 
 * @author Clownvin
 *
 *         Main is the main class and entry point of the MMOServer. I also
 *         handles the "tick" mechanics.
 *
 *         <p>
 *         A tick in this server framework is simply a notification to all the
 *         threads running TickReliant objects telling them to wake up from
 *         their wait call. All tick reliants invoke <code>wait()</code> on the
 *         TICK_LOCK object, which they retrieve from this class. When a "tick"
 *         is sent out, <code>notifyAll()</code> is called on TICK_LOCK, which
 *         in turn allows threads to exit wait and begin execution of their tick
 *         procedures. Once a tick begins, the main thread (the one used during
 *         entry into this class, which also handles ticking) waits until all
 *         TickReliant objects have finished their tick procedures. The main
 *         thread then waits out any remaining time, then notifies all the
 *         threads waiting on TICK_LOCK to start a new tick cycle, infinitely
 *         repeating.
 *         </p>
 * 
 *         <p>
 *         Another neat feature is that whenever a TickReliant thread fails
 *         because of an uncaught exception, the whole server pauses (after the
 *         current tick) so that debugging may commence. From there, the thread
 *         can actually be restarted from a button in the GUI.
 *         </p>
 */
public final class Main {

	//List of TickReliant objects currently running. They add themselves to this list when instantiated.
	private static final List<TickProcess> TICK_RELIANT_LIST = new ArrayList<>();

	//Is server running.
	private static volatile boolean running = true;
	//Is server paused.
	private static volatile boolean paused = false;
	//Pause timer remaing. (Mainly exists so that Connection objects wont disconnect their clients due to the rapid influx of packets from being paused.)
	private static volatile int pauseTimerRemaining = 0;
	//Object that all TickReliant objects wait on for tick notification.
	private static final Object TICK_LOCK = new Object(); // To notify threads of new tick.
	//Object that Main thread waits on for notification of error resolution.
	private static final Object FAILURE_RESOLUTION_LOCK = new Object();
	//Current server ticks count.
	private static volatile long tickCount = 0; // Tick count.

	/**
	 * Can be used to register tick reliants so that server will know to wait
	 * until theyre finished. Automatically invoked from TickReliant contructor.
	 * 
	 * @param tickReliant
	 *            new object to register in list.
	 */
	public static void addTickReliant(final TickProcess tickReliant) {
		synchronized (TICK_RELIANT_LIST) { // Synchronize block to obtain TICK_RELIANT_LIST lock
			TICK_RELIANT_LIST.add(tickReliant);
		}
	}

	/**
	 * Getter method for tickCount.
	 * 
	 * @return current tickCount
	 */
	public static long getTickCount() {
		return tickCount;
	}

	/**
	 * Getter method for TICK_LOCK.
	 * 
	 * @return the TICK_LOCK object.
	 */
	public static Object getTickLock() {
		return TICK_LOCK;
	}

	/**
	 * Getter for running state.
	 * 
	 * @return running state
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * Getter for wasPaused state.
	 * 
	 * @return wasPaused state
	 */
	public static boolean wasPaused() {
		return paused;
	}

	/**
	 * Main method, duh.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerGUI.getSingleton().setVisible(true); //Display server gui
		System.setOut(Logger.getPrintStream()); // Set System out to logger out
		System.setErr(Logger.getPrintStream());
		Runtime.getRuntime().addShutdownHook(new Thread() { // Add shutdown hook that autosaves users.
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
		while (running) { // As long as server is running...
			if (wasPaused() && pauseTimerRemaining-- == 0) { // Check if it's time to set paused to false.
				paused = false;
			}
			long start = System.currentTimeMillis();
			synchronized (TICK_LOCK) { // Obtain intrinsic lock and notify all waiting threads. This starts the tick.
				TICK_LOCK.notifyAll();
			}
			boolean allFinished;
			do { // This block keeps looping until all tick reliant threads are finished with their tick.
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Don't really care too much if it gets interrupted.
				}
				allFinished = true;
				for (TickProcess t : TICK_RELIANT_LIST) {
					if (t.isStopped()) { // Uncaught exception or SOMETHING caused thread to stop.
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
			long timeLeft = Config.TICK_DELAY - (System.currentTimeMillis() - start); // Calculate remaining time.
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

	/**
	 * Notify all threads waiting on the FAILURE_RESOLUTION_LOCK
	 */
	public static void notifyFailureResolution() {
		synchronized (FAILURE_RESOLUTION_LOCK) {
			FAILURE_RESOLUTION_LOCK.notifyAll();
		}
	}

	/**
	 * Requests program termination.
	 */
	public static void requestExit() {
		//For now we can just have it set running to false, but later on it should check to see which part failed, and recover if it can.
		running = false;
	}
}
