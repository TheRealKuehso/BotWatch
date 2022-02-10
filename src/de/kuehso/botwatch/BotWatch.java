package de.kuehso.botwatch;

import javax.swing.JDialog;

public class BotWatch {
	public static void main(String[] args) throws Exception {
		BotWatchGUI dialog = new BotWatchGUI();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
}
