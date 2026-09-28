package miniagendaswing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

import javax.swing.JTextField;

public class FormatUtils {

	public static LocalDate parseDate(String date) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
					.withResolverStyle(ResolverStyle.STRICT);

			LocalDate parsedDate = LocalDate.parse(date, formatter);

			return parsedDate;

		} catch (DateTimeParseException e) {
			DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("dd/MM/uu")
					.withResolverStyle(ResolverStyle.STRICT);

			LocalDate parseDate = LocalDate.parse(date, shortFormatter);

			return parseDate;
		}

	}

	public static LocalTime parseTime(String hour) {

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("H'h'").optionalStart()
				.appendPattern("mm").optionalEnd().parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).toFormatter()
				.withResolverStyle(ResolverStyle.STRICT);

		return LocalTime.parse(hour, formatter);
	}

	public static String formatDate(LocalDate date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

		return date.format(formatter);
	}

	public static String formatTime(LocalTime hour) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm");

		return hour.format(formatter);
	}

	public static void addPlaceHolder(JTextField field, String placeholder) {

		field.setText(placeholder);
		field.setForeground(Color.GRAY);

		field.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {

				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (field.getText().isBlank()) {
					field.setText(placeholder);
					field.setForeground(Color.GRAY);
				}
			}

		});
	}
}
