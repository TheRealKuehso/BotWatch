package de.kuehso.botwatch;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class BotWatchGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PlaceholderTextField textField;
	private JTextArea textArea = new JTextArea();

	/**
	 * Create the dialog.
	 */
	public BotWatchGUI() {
		setTitle("BotWatch by kuehso");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			textArea.setEditable(false);
			textArea.setToolTipText("B\u00F6se Bots!");
			textArea.setColumns(1);
			textArea.setRows(30);
			contentPanel.add(textArea);
		}
		{
			JLabel lblNewLabel = new JLabel("Folgende \"User\" sind eigentlich Bots:");
			lblNewLabel.setLabelFor(textArea);
			contentPanel.add(lblNewLabel, BorderLayout.NORTH);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			{
				textField = new PlaceholderTextField();
				textField.setPlaceholder("<channelname>");
				textField.setHorizontalAlignment(SwingConstants.LEFT);
				textField.setToolTipText("Hier den Namen des Channels eintragen");
				buttonPane.add(textField);
				textField.setColumns(35);
			}
			{
				JButton okButton = new JButton("Bots suchen");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						textArea.setText("");
						try {
							final SearchResult sr = BotChecker.check(textField.getText());
							textArea.setText(sr.toString());
						} catch (Exception e1) {
							System.err.println(e1);
						}
					}
				});
				buttonPane.add(okButton, BorderLayout.EAST);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
