package com.git.cs309.mmoserver.gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.git.cs309.mmoserver.characters.CharacterManager;
import com.git.cs309.mmoserver.connection.ConnectionManager;
import com.git.cs309.mmoserver.cycle.CycleProcessManager;
import com.git.cs309.mmoserver.io.Logger;

public class ServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3687402786121828571L;
	private static final JList<String> consoleList = new JList<>(Logger.getListModel());
	private static final JScrollPane consolePane = new JScrollPane(consoleList);
	private static final ServerGUI SINGLETON = new ServerGUI();

	public static ServerGUI getSingleton() {
		return SINGLETON;
	}

	public static void update() {
		consolePane.validate();
		consolePane.getVerticalScrollBar().setValue(consolePane.getVerticalScrollBar().getMaximum());
	}

	private ServerGUI() {
		setSize(400, 300);
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		JPanel consolePanePanel = new JPanel();
		consolePanePanel.add(consolePane);
		consolePanePanel.setLayout(new BoxLayout(consolePanePanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("Console", null, consolePanePanel, null);
		JPanel statusPanel = new JPanel();
		statusPanel.add(CharacterManager.getSingleton().getComponent());
		statusPanel.add(ConnectionManager.getSingleton().getComponent());
		statusPanel.add(CycleProcessManager.getSingleton().getComponent());
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("Status", null, statusPanel, null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
