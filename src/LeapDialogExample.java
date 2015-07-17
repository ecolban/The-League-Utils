import org.jointheleague.util.LeapDialog;
import org.jointheleague.util.LeapDialog.Option;

public class LeapDialogExample {

	public static void main(String[] args) {

		testGetInput1();
		testGetInput2();
		testShowMessage();
		testChoose1();
		testChoose2();

	}

	private static void testGetInput1() {
		// Get some input from the user as a String:
		String name = LeapDialog.getInput("What's your first name:");
		System.out.println(String.format("Your first name is %s.", name));

	}

	private static void testGetInput2() {

		// For multi-line messages, use HTML:
		String question = "<html>Please,<br>tell me your last name:</html>";
		String name = LeapDialog.getInput(question);
		System.out.println(String.format("Your last name is %s.", name));
	}
	
	private static void testShowMessage() {
		// No input from the user is expected:
		LeapDialog.showMessage("Thanks!!");
	}

	// Let the user respond with a button click (list the options in
	// the preferred order):
	private static void testChoose1() {
		Option opt = LeapDialog.choose("Was that fun?",	Option.YES, Option.NO);
		System.out.println(opt == Option.YES ? "Yeah!" : "Boo...");
	}

	// Let the user respond with a button click (list the options in
	// the preferred order):
	private static void testChoose2() {
		
		Option.NO.setString("Definitely not!!!");
		Option.MAY_BE.setString("nah...");
		
		Option opt = LeapDialog.choose("Wasn't that cool?!",
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
	}

}
