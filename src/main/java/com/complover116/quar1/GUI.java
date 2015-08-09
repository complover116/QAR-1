package com.complover116.quar1;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

public class GUI {
	public static JFrame mainFrame;
	public static ProgressMonitor progressMonitor;
	public static void init() {
		System.out.println("INITIALIZING THE GUI");
		mainFrame = new JFrame("QAR-1 "+Config.version);
		mainFrame.setPreferredSize(new Dimension(900,800));
		mainFrame.setResizable(true);
		Render pn = new Render();
		pn.addKeyListener(pn);
		pn.setFocusable(true);
		pn.requestFocusInWindow();
		mainFrame.add(pn);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void progressMonitor(int min, int max, String message) {
		progressMonitor = new ProgressMonitor(mainFrame,
                message,
                "", min, max);
		progressMonitor.setMillisToPopup(0);
		progressMonitor.setMillisToDecideToPopup(1);
	}
	public static void updateProgress(int progress, String message) {
		progressMonitor.setProgress(progress);
		progressMonitor.setNote(message);
	}
	public static void closeProgressMonitor(){
		progressMonitor.close();
	}
	public static String askString(String title, String desc) {
		Object[] possibleValues = { "host", null };
		Object selectedValue = JOptionPane.showInputDialog(mainFrame,
		desc, title,
		JOptionPane.INFORMATION_MESSAGE, null,
		null, possibleValues[0]);
		if(selectedValue == null) System.exit(0);
		return (String)selectedValue;
	}
	public static void askBots() {
		int reply = JOptionPane.showConfirmDialog(null, "Should we enable bots for this game?", "AI Setup", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			Config.botDifficulty = 5;
		} else {
			Config.botDifficulty = -1;
		}
	}
}
