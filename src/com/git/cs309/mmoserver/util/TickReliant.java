package com.git.cs309.mmoserver.util;

import java.util.Observable;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.gui.TickReliantStatusComponent;

public abstract class TickReliant extends Observable implements Runnable {
	protected volatile boolean tickFinished = true;
	protected volatile boolean isStopped = true;
	protected final String name;
	protected final TickReliantStatusComponent component = new TickReliantStatusComponent(this);
	
	public TickReliant(final String name) {
		this.name = name;
		Main.addTickReliant(this);
		Thread tickReliantThread = new Thread(this);
		tickReliantThread.setName(name);
		tickReliantThread.start();
	}
	
	public boolean tickFinished() {
		return tickFinished;
	}
	
	public boolean isStopped() {
		return isStopped;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public TickReliantStatusComponent getComponent() {
		return component;
	}
}
