package miniagendaswing;

public class Main {

	public static void main(String[] args) {

		Agenda agenda = new Agenda();

		RecurringEventList recurringEventList = new RecurringEventList();

		new MainUI(agenda, recurringEventList);

	}
}
