package com.git.cs309.mmoserver.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import com.git.cs309.mmoserver.util.TickProcess;

public class TickProcessStatusComponent extends Container implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6968306919146587028L;

	private volatile TickProcess subject;
	private volatile JLabel subjectName;
	private volatile JLabel runningLabel;
	private volatile JLabel averageTimeLabel;

	public TickProcessStatusComponent(final TickProcess subject) {
		subject.addObserver(this);
		this.subject = subject;
		setLayout(new GridLayout(0, 4));
		subjectName = new JLabel(subject.toString() + ": ");
		runningLabel = new JLabel("Starting...");
		averageTimeLabel = new JLabel("N/A");
		add(subjectName);
		add(runningLabel);
		add(averageTimeLabel);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	@Override
	public void update(Observable o, Object arg) {
		setBackground(subject.isStopped() ? Color.RED : (subject.tickFinished() ? Color.GREEN : Color.YELLOW));
		runningLabel.setText(subject.isStopped() ? "Stopped" : (subject.tickFinished() ? "Dormant" : "Running"));
		averageTimeLabel.setText(String.format("%.5f", (subject.getAverageTick() / 1000000.0f)) + "ms");
		this.repaint();
	}
}
