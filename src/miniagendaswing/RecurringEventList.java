package miniagendaswing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RecurringEventList {

	private ArrayList<RecurringEvent> recurringEvents = new ArrayList<>();

	private final String fileName = "recurringEvents.csv";

	public RecurringEventList() {

		loadRecurringEvents();
	}

	private void loadRecurringEvents() {

		File file = new File(fileName);

		if (!file.exists()) {
			return;
		}

		try {

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] data = line.split(";", -1);

				if (data.length == 7) {

					String title = data[0];
					LocalDate startDate = LocalDate.parse(data[1]);
					LocalDate endDate = LocalDate.parse(data[2]);
					DayOfWeek dayOfWeek = DayOfWeek.valueOf(data[3]);
					LocalTime startTime = LocalTime.parse(data[4]);
					LocalTime endTime = LocalTime.parse(data[5]);
					String id = data[6];

					RecurringEvent recurringEvent = new RecurringEvent(title, startDate, endDate, dayOfWeek, startTime,
							endTime);

					recurringEvent.setId(id);
					recurringEvents.add(recurringEvent);
				}
			}

			reader.close();

		} catch (IOException e) {
			System.out.println("Erreur au chargement du fichier recurring events");
			e.printStackTrace();
		}
	}

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

	public void removeRecurringEventById(String recurringId) {

		for (int i = recurringEvents.size() - 1; i >= 0; i--) {
			RecurringEvent selectedEvent = recurringEvents.get(i);
			String currentRecurringId = selectedEvent.getId();

			if (recurringId.equals(currentRecurringId)) {
				recurringEvents.remove(i);	
				break;
			}
		}
		
		saveRecurringEvents();
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
