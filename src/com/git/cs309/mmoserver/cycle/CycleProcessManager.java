package com.git.cs309.mmoserver.cycle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.util.TickReliant;

public final class CycleProcessManager extends Observable implements TickReliant, Runnable {
	private static final CycleProcessManager SINGLETON = new CycleProcessManager();
	private static final Set<CycleProcess> PROCESSES = new HashSet<>();
	private static volatile boolean isStopped = true;

	public static void addProcess(final CycleProcess process) {
		synchronized (PROCESSES) {
			PROCESSES.add(process);
		}
	}

	public static CycleProcessManager getSingleton() {
		return SINGLETON;
	}

	public static boolean isStopped() {
		return isStopped;
	}

	private volatile boolean tickFinished = true;

	//Private so that only this class can access constructor.
	private CycleProcessManager() {
		Main.addTickReliant(this);
		Thread cycleProcessManagerThread = new Thread(this);
		cycleProcessManagerThread.setName("CycleProcessManager");
		cycleProcessManagerThread.start();
	}

	private synchronized void processAllProcesses() {
		List<CycleProcess> removalList = new ArrayList<>();
		synchronized (PROCESSES) {
			for (CycleProcess process : PROCESSES) {
				process.process();
				if (process.finished()) {
					process.end();
					removalList.add(process);
				}
			}
			PROCESSES.removeAll(removalList);
		}
	}

	@Override
	public void run() {
		final Object tickObject = Main.getTickObject();
		isStopped = false;
		while (Main.isRunning()) {
			tickFinished = false;
			setChanged();
			notifyObservers();
			processAllProcesses();
			tickFinished = true;
			setChanged();
			notifyObservers();
			synchronized (tickObject) {
				try {
					tickObject.wait(); // Wait for tick notification.
				} catch (InterruptedException e) {
					// We don't care too much if it gets interrupted.
				}
			}
		}
		isStopped = true;
		setChanged();
		notifyObservers();
	}

	@Override
	public boolean tickFinished() {
		return tickFinished;
	}
}
