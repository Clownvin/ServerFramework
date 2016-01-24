package com.git.cs309.mmoserver.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import com.git.cs309.mmoserver.util.TickReliant;

public class TickReliantStatusComponent extends Component implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6968306919146587028L;
	
	private volatile TickReliant subject;
	
	public TickReliantStatusComponent(final TickReliant subject) {
		setSize(300, 20);
		setMinimumSize(new Dimension(300, 20));
		subject.addObserver(this);
		this.subject = subject;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		FontMetrics fm = g.getFontMetrics();
	    int x = (getWidth() - fm.stringWidth(subject.toString())) / 2;
	    int y = (fm.getAscent() + (getHeight() - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(subject.toString(), x, y);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setBackground(subject.isStopped() ? Color.RED : (subject.tickFinished() ? Color.GREEN : Color.YELLOW));
		this.repaint();
	}
}
