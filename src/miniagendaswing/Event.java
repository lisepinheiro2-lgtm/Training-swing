package miniagendaswing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Event {

	private String id;
	private String title;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String recurringId;

	public Event(String title, LocalDate date, LocalTime startTime, LocalTime endTime) {

		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.recurringId = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecurringId() {
		return recurringId;
	}

	public void setRecurringId(String recurringId) {
		this.recurringId = recurringId;
	}

}
