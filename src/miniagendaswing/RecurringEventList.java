package miniagendaswing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecurringEventList {

	private ArrayList<RecurringEvent> recurringEvents = new ArrayList<>();
	private final String fileName = "recurringEvents";

	private void saveRecurringEvents() {

		try {

			FileWriter writer = new FileWriter(fileName);

			for (RecurringEvent recurringEvent : recurringEvents) {

				writer.write(recurringEvent.getTitle() + ";" + recurringEvent.getStartDate() + ";"
						+ recurringEvent.getEndDate() + ";" + recurringEvent.getDayOfWeek() + ";"
						+ recurringEvent.getStartTime() + ";" + recurringEvent.getEndTime() + ";"
						+ recurringEvent.getId() + System.lineSeparator());
			}

			writer.close();

		} catch (IOException e) {
			System.out.println("Erreur pendant la sauvegarde du fichier recurring events");
			e.printStackTrace();
		}
	}

	public void addRecurringEvent(RecurringEvent recurringEvent) {

		recurringEvents.add(recurringEvent);
		saveRecurringEvents();
	}

	public ArrayList<RecurringEvent> getRecurringEvents() {
		return recurringEvents;
	}

	public void removeRecurringEvent(int index) {

		recurringEvents.remove(index);
		saveRecurringEvents();
	}

	public boolean isEmpty() {
		return recurringEvents.isEmpty();
	}
}
