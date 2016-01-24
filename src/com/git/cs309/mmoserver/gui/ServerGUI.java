package com.git.cs309.mmoserver.gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.git.cs309.mmoserver.io.Logger;

public class ServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3687402786121828571L;
	private static final JList<String> list = new JList<>(Logger.getListModel());
	private static final JScrollPane scrollPane = new JScrollPane(list);
	private static final ServerGUI SINGLETON = new ServerGUI();

	public static ServerGUI getSingleton() {
		return SINGLETON;
	}

	public static void update() {
		scrollPane.validate();
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	private ServerGUI() {
		setSize(400, 300);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		JPanel scrollPanePanel = new JPanel();
		scrollPanePanel.add(scrollPane);
		scrollPanePanel.setLayout(new BoxLayout(scrollPanePanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("Console", null, scrollPanePanel, null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
