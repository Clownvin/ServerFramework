package com.git.cs309.mmoserver.cycle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.util.TickReliant;

public final class CycleProcessManager extends TickReliant {
	private static final CycleProcessManager SINGLETON = new CycleProcessManager();
	private static final Set<CycleProcess> PROCESSES = new HashSet<>();

	public static void addProcess(final CycleProcess process) {
		synchronized (PROCESSES) {
			PROCESSES.add(process);
		}
	}

	public static CycleProcessManager getSingleton() {
		return SINGLETON;
	}

	//Private so that only this class can access constructor.
	private CycleProcessManager() {
		super ("CycleProcessManager");
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
				tickFinished = false;
			}
		}
		isStopped = true;
		setChanged();
		notifyObservers();
	}
}
