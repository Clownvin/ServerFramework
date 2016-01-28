package com.git.cs309.mmoserver.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.gui.ServerGUI;
import com.git.cs309.mmoserver.gui.TickProcessStatusComponent;

/**
 * 
 * @author Clownvin
 * 
 *         <p>
 *         TickProcess is the skeleton of the Server, or rather it's
 *         implementations are. TickReliant is a class which executes every tick
 *         and performs a task. Ideally, this class is to be used for processes
 *         which will happen throughout the duration of the server's lifetime.
 *         If not, consider using CycleProcesses, as they're much less resource
 *         intensive.
 *         </p>
 */
public abstract class TickProcess extends Observable implements Runnable {
	protected volatile boolean tickFinished = true;
	protected volatile boolean isStopped = true;
	protected final String name;
	protected final TickProcessStatusComponent component;
	protected volatile long cumulative = 0;
	protected volatile int count = 0;
	protected volatile long average = 0;
	protected volatile Thread tickReliantThread = null;
	protected final JButton restartButton = new JButton("Restart");

	public TickProcess(final String name) {
		this.name = name;
		component = new TickProcessStatusComponent(this);
		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start();
				Main.notifyFailureResolution();
			}

		});
		component.add(restartButton);
		ServerGUI.addComponentToStatusPanel(component);
		Main.addTickReliant(this);
		start();
	}

	/**
	 * Allows access to the average time per tick of this object.
	 * 
	 * @return the average tick time.
	 */
	public long getAverageTick() {
		return average;
	}

	/**
	 * FOR GUI ONLY
	 * 
	 * @return the component representing this object.
	 */
	public TickProcessStatusComponent getComponent() {
		return component;
	}

	/**
	 * Handles tick averaging.
	 * 
	 * @param thisTick
	 *            time this tick
	 */
	protected void handleTickAveraging(long thisTick) {
		cumulative += thisTick;
		count++;
		if (count == 10) {
			average = cumulative / count;
			count = 0;
			cumulative = 0;
		}
	}

	/**
	 * Is this process stopped?
	 * 
	 * @return
	 */
	public boolean isStopped() {
		return isStopped;
	}

	@Override
	public final void run() { // Final to ensure that this can't be overriden, to ensure that all extending classes follow the rules.
		final Object tickLock = Main.getTickLock(); // Acquire the tickLock object from Main.
		isStopped = false;
		while (Main.isRunning()) { // While server is running...
			try {
				synchronized (tickLock) {
					try {
						tickLock.wait(); // Wait for tick notification.
					} catch (InterruptedException e) {
						// We don't care too much if it gets interrupted.
					}
					tickFinished = false;
				}
				setChanged();
				notifyObservers();
				long start = System.nanoTime();
				tickTask(); // Perform task
				handleTickAveraging(System.nanoTime() - start);
				tickFinished = true;
				setChanged();
				notifyObservers();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		tickFinished = true;
		isStopped = true;
		setChanged();
		notifyObservers();
	}

	protected final void start() {
		if (tickReliantThread == null || !tickReliantThread.isAlive()) {
			tickReliantThread = new Thread(this);
			tickReliantThread.setName(name);
			tickReliantThread.start();
		}
	}

	public boolean tickFinished() {
		return tickFinished;
	}

	protected abstract void tickTask();

	@Override
	public String toString() {
		return name;
	}
}
