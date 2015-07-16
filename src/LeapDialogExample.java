import org.jointheleague.util.LeapDialog;
import org.jointheleague.util.LeapDialog.Option;

public class LeapDialogExample {

	public static void main(String[] args) {

		testRun();

	}

	private static void testRun() {
		try {
			// Get some input from the user as a String:

			String name = LeapDialog.getInput("What's your first name:");
			System.out.println(String.format("Your first name is %s.", name));

			// For multi-line messages, use HTML:
			name = LeapDialog
			        .getInput("<html>Please,<br>tell me your last name:</html>");
			System.out.println(String.format("Your last name is %s.", name));

			// When no input from the user is expected:
			LeapDialog.showMessage("Thanks!!");

			// Let the user respond with a button click (list the options in
			// the preferred order):
			Option.YES.setString("YEAH");
			Option.NO.setString("DEFINITELY NOT!!!");
			Option.MAY_BE.setString("NAH...");
			Option.CANCEL.setString("LATER");
			Option opt = LeapDialog.choose("Was that fun?",
			        Option.YES, Option.MAY_BE, Option.NO, Option.CANCEL);
			switch (opt) {
			case YES:
				System.out.println("I like positive people!");
				break;
			case NO:
				System.out.println("Don't be so negative!");
				break;
			default:
				System.out.println("Indecisive today?");
			}

		} catch (InterruptedException e) {
			System.out.println("Our dialog was interrupted.");
		}
	}
}
