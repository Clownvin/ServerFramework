package com.git.cs309.mmoserver.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.gui.ServerGUI;
import com.git.cs309.mmoserver.gui.TickReliantStatusComponent;

public abstract class TickReliant extends Observable implements Runnable {
	protected volatile boolean tickFinished = true;
	protected volatile boolean isStopped = true;
	protected final String name;
	protected final TickReliantStatusComponent component;
	protected volatile long cumulative = 0;
	protected volatile int count = 0;
	protected volatile long average = 0;
	protected volatile Thread tickReliantThread = null;
	protected final JButton restartButton = new JButton("Restart");

	public TickReliant(final String name) {
		this.name = name;
		component = new TickReliantStatusComponent(this);
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

	public long getAverageTick() {
		return average;
	}

	public TickReliantStatusComponent getComponent() {
		return component;
	}

	protected void handleTickAveraging(long thisTick) {
		cumulative += thisTick;
		count++;
		if (count == 10) {
			average = cumulative / count;
			count = 0;
			cumulative = 0;
		}
	}

	public boolean isStopped() {
		return isStopped;
	}

	@Override
	public void run() {
		final Object tickLock = Main.getTickLock();
		isStopped = false;
		while (Main.isRunning()) {
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
				tickTask();
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

	protected void start() {
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
