package miniagendaswing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Agenda {

	private ArrayList<Event> events = new ArrayList<Event>();
	private final String fileName = "events.csv";

	public Agenda() {
		loadEvents();
	}

	private void loadEvents() {

		File file = new File(fileName);

		if (!file.exists()) {
			return;
		}

		try {

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] data = line.split(";", -1);

				if (data.length == 6) {

					String title = data[0];
					LocalDate date = LocalDate.parse(data[1]);
					LocalTime startTime = LocalTime.parse(data[2]);
					LocalTime endTime = LocalTime.parse(data[3]);
					String id = data[4];
					String recurringId = data[5];

					Event currentEvent = new Event(title, date, startTime, endTime);

					currentEvent.setId(id);

					if (!recurringId.isBlank()) {
						currentEvent.setRecurringId(recurringId);
					}

					events.add(currentEvent);
				}
			}

			reader.close();

		} catch (IOException e) {
			System.out.println("Erreur au chargement du fichier events");
			e.printStackTrace();
		}
	}

	private void saveEvents() {

		try {

			FileWriter writer = new FileWriter(fileName);

			for (Event event : events) {
				String recurringId = "";

				if (event.getRecurringId() != null) {
					recurringId = event.getRecurringId();
				}

				writer.write(event.getTitle() + ";" + event.getDate() + ";" + event.getStartTime() + ";"
						+ event.getEndTime() + ";" + event.getId() + ";" + recurringId + System.lineSeparator());
			}
			writer.close();

		} catch (IOException e) {
			System.out.println("Erreur pendant la sauvegarde du fichier events");
			e.printStackTrace();
		}
	}

	public boolean hasOverlap(Event eventToCheck) {

		LocalDateTime startToCheck = LocalDateTime.of(eventToCheck.getDate(), eventToCheck.getStartTime());
		LocalDateTime endToCheck = LocalDateTime.of(eventToCheck.getDate(), eventToCheck.getEndTime());

		for (Event event : events) {

			if (event == eventToCheck) {
				continue;
			}

			LocalDateTime startEvent = LocalDateTime.of(event.getDate(), event.getStartTime());
			LocalDateTime endEvent = LocalDateTime.of(event.getDate(), event.getEndTime());

			if (startToCheck.isBefore(endEvent) && endToCheck.isAfter(startEvent)) {
				return true;
			}
		}

		return false;
	}

	public boolean generateRecurringEvents(RecurringEvent recurringEvent) {

		ArrayList<Event> recurringEvents = new ArrayList<>();
		LocalDate startDate = recurringEvent.getStartDate();
		LocalDate endDate = recurringEvent.getEndDate();
		LocalDate currentDate = startDate;

		while (!currentDate.isAfter(endDate)) {

			if (currentDate.getDayOfWeek().equals(recurringEvent.getDayOfWeek())) {
				Event recurring = new Event(recurringEvent.getTitle(), currentDate, recurringEvent.getStartTime(),
						recurringEvent.getEndTime());
				recurring.setRecurringId(recurringEvent.getId());

				if (hasOverlap(recurring)) {
					return false;
				}
				recurringEvents.add(recurring);
			}
			currentDate = currentDate.plusDays(1);
		}

		events.addAll(recurringEvents);
		saveEvents();
		return true;
	}

	public void removeEventsByRecurringId(String recurringId) {

		for (int i = events.size() - 1; i >= 0; i--) {
			Event selectedEvent = events.get(i);
			String currentRecurringId = selectedEvent.getRecurringId();

			if (recurringId.equals(currentRecurringId)) {
				events.remove(i);
			}
		}

		saveEvents();
	}

	public void addEvent(Event event) {

		events.add(event);
		saveEvents();
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void removeEvent(int index) {

		events.remove(index);
		saveEvents();
	}

	public boolean isEmpty() {
		return events.isEmpty();
	}
}
