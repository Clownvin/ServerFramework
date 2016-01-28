package com.git.cs309.mmoserver.cycle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.cs309.mmoserver.util.TickProcess;

/**
 * 
 * @author Clownvin
 * 
 *         Handles all CycleProcesses. A CycleProcess is simply a task that
 *         needs to be executed per tick, but doesn't need or can't implement
 *         TickReliant themselves. Good uses for CycleProcesses are: Events,
 *         Walking, Buffs, things like that. CycleProcesses can also terminate
 *         if a condition is met.
 */
public final class CycleProcessManager extends TickProcess {
	private static final CycleProcessManager SINGLETON = new CycleProcessManager();
	private static final Set<CycleProcess> PROCESSES = new HashSet<>(); // Set of processes.

	/**
	 * Add a new process for execution.
	 * 
	 * @param process
	 *            new process
	 */
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
		super("CycleProcessManager");
	}

	@Override
	protected synchronized void tickTask() {
		List<CycleProcess> removalList = new ArrayList<>(); // List of objects to remove after finished processing. Because of for-each loop, concurrent modification would occur if they were removed during loop.
		synchronized (PROCESSES) {
			for (CycleProcess process : PROCESSES) {
				process.process();
				if (process.finished()) { // Check if process is finished.
					process.end();
					removalList.add(process);
				}
			}
			PROCESSES.removeAll(removalList);
		}
	}
}
