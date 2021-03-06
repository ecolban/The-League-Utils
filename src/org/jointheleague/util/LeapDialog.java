package org.jointheleague.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This class is a simpler version of and substitute for JOptionPane. It allows
 * for simple user interactions often required in beginner level programming
 * assignments.
 * 
 * @author Erik Colban
 * 
 */
public class LeapDialog {

	/**
	 * Possible responses that a user may give by clicking a button.
	 */
	public enum Option {
		YES, NO, MAY_BE, OK, SKIP, CANCEL;

		private String string;

		/**
		 * Sets the string returned by the enum instance's {@link #toString()}
		 * method. If the argument is null, resets the instance to have its
		 * default string representation, i.e., the option name.
		 * 
		 * @param string
		 *            the string representation of this instance.
		 */
		public synchronized void setString(String string) {
			this.string = string;
		}

		@Override
		public synchronized String toString() {
			if (string == null) {
				return super.toString();
			} else {
				return string;
			}
		}

	}

	private JTextField inputField;
	private String input;
	private Option returnOption;

	private LeapDialog(final String message, final boolean hasInput)
	        throws InterruptedException {
		final BlockingQueue<Option> q = new SynchronousQueue<>();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (hasInput) {
					buildInputGUI(q, message);
				} else {
					buildMessageGUI(q, message);
				}
			}
		});
		q.take(); // blocks until user clicks a button
	}

	private LeapDialog(final String message, final Option... options)
	        throws InterruptedException {
		final BlockingQueue<Option> q = new SynchronousQueue<>();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				buildChoiceGUI(q, message, options);
			}
		});
		returnOption = q.take(); // blocks until user clicks a button
	}

	/**
	 * This method is used to convey a message to the user. No answer is
	 * expected from the user. The method blocks until the user clicks on the OK
	 * button.
	 * 
	 * @param msg
	 *            The message that is presented to the user.
	 * 
	 */
	public static void showMessage(final String msg) {
		try {
			new LeapDialog(msg, false);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * This method is used to ask the user for input. The method blocks until
	 * the user clicks on the OK button. If interrupted, returns null.
	 * 
	 * @param msg
	 *            A message asking the user for input.
	 * @param options
	 *            Options that the user may select, such as OK, SKIP or CANCEL
	 * @return The answer provided by the user.
	 */
	public static String getInput(final String msg) {
		try {
			return new LeapDialog(msg, true).input;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * This method is used to request the user to make a choice. Possible
	 * answers are provided in the Option enum. The method blocks until the user
	 * clicks on the OK button. If interrupted, returns null.
	 * 
	 * @param msg
	 *            The message asking the user to select an option
	 * @param options
	 *            The possible options that the user may choose from in the
	 *            order that they shall appear in the dialog window. There must
	 *            be at least one option.
	 * @return The option that the user chose. It is one of the options provided
	 *         as argument to this method.
	 *         
	 * @throws IllegalArgumentException
	 *             if no options are provided.
	 */
	public static Option choose(final String msg, final Option... options) {
		if (options.length == 0) {
			throw new IllegalArgumentException(
			        "There must be at least one option.");
		}
		try {
	        return new LeapDialog(msg, options).returnOption;
        } catch (InterruptedException e) {
        	return null;
        }
	}

	private void buildMessageGUI(final BlockingQueue<Option> queue,
	        String message) {
		final JDialog d = new JDialog();
		d.setMinimumSize(new Dimension(400, 66));
		d.setLocationRelativeTo(null);
		d.setLayout(new BorderLayout());
		JPanel msgPanel = new JPanel();
		msgPanel.add(new JLabel(message));
		d.add(msgPanel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton(Option.OK.toString());
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
				d.dispose();
				queue.add(Option.OK);
			}
		});
		buttonPanel.add(okButton);
		d.add(buttonPanel, BorderLayout.SOUTH);
		d.pack();
		d.setVisible(true);
	}

	private void buildInputGUI(final BlockingQueue<Option> queue,
	        String message) {
		final JDialog d = new JDialog();
		d.setMinimumSize(new Dimension(400, 100));
		d.setLocationRelativeTo(null);
		d.setLayout(new BorderLayout());
		JPanel msgPanel = new JPanel();
		msgPanel.add(new JLabel(message));
		d.add(msgPanel, BorderLayout.NORTH);
		JPanel inputPanel = new JPanel();
		inputField = new JTextField(40);
		inputPanel.add(inputField);
		d.add(inputPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		JButton b = new JButton(Option.OK.toString());
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input = inputField.getText();
				d.setVisible(false);
				d.dispose();
				queue.offer(Option.OK);
			}
		});
		buttonPanel.add(b);
		d.add(buttonPanel, BorderLayout.SOUTH);
		d.pack();
		d.setVisible(true);
	}

	private void buildChoiceGUI(final BlockingQueue<Option> queue,
	        String message, Option... options) {
		final JDialog d = new JDialog();
		d.setMinimumSize(new Dimension(400, 66));
		d.setLocationRelativeTo(null);
		d.setLayout(new BorderLayout());
		JPanel msgPanel = new JPanel();
		msgPanel.add(new JLabel(message));
		d.add(msgPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		for (final Option opt : options) {
			JButton b = new JButton(opt.toString());
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					d.setVisible(false);
					d.dispose();
					queue.offer(opt);
				}
			});
			buttonPanel.add(b);
		}
		d.add(buttonPanel, BorderLayout.SOUTH);
		d.pack();
		d.setVisible(true);
	}
}
