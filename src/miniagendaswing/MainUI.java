package miniagendaswing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MainUI {

	private Agenda agenda;
	private RecurringEventList recurringEventList;
	private JFrame frame;
	private DefaultTableModel tableModel;
	private JTable table;

	public MainUI(Agenda agenda, RecurringEventList recurringEventList) {

		this.agenda = agenda;
		this.recurringEventList = recurringEventList;

		createWindow();
	}

	private void createWindow() {

		frame = new JFrame("Mini Agenda");
		frame.setSize(900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		JButton addEvent = new JButton("Ajouter un événement");
		JButton addRecurring = new JButton("Ajouter une récurrence");
		JButton removeEvent = new JButton("Supprimer l'événement sélectionné");
		JButton removeRecurring = new JButton("Supprimer une récurrence");
		buttonPanel.add(addEvent);
		buttonPanel.add(addRecurring);
		buttonPanel.add(removeEvent);
		buttonPanel.add(removeRecurring);

		String[] columnsNames = { "Titre", "Date", "Début", "Fin", "Type" };
		tableModel = new DefaultTableModel(columnsNames, 0);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		addEvent.addActionListener(event -> openAddEvent());
		addRecurring.addActionListener(recurringEvent -> openAddRecurringEvent());
		removeEvent.addActionListener(event -> openRemoveEvent());
		removeRecurring.addActionListener(recurringEvent -> removeRecurringEvent());

		loadTable();
		frame.add(mainPanel);
		frame.setVisible(true);
	}

	private void removeRecurringEvent() {

		int index = table.getSelectedRow();

		if (index == -1) {
			JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un événement.");
			return;
		}

		Event selectedEvent = agenda.getEvents().get(index);
		String recurringId = selectedEvent.getRecurringId();

		if (recurringId == null || recurringId.isBlank()) {
			JOptionPane.showMessageDialog(frame, "Cet événement est unique, il n’appartient à aucune récurrence.");
			return;
		}

		agenda.removeEventsByRecurringId(recurringId);
		recurringEventList.removeRecurringEventById(recurringId);
		loadTable();
	}

	private void openRemoveEvent() {

		int index = table.getSelectedRow();

		if (index == -1) {
			JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un événement.");
			return;
		}

		agenda.removeEvent(index);
		loadTable();
	}

	private void openAddEvent() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));

		JLabel title = new JLabel("Titre :");
		JTextField titleField = new JTextField();
		JLabel date = new JLabel("Date :");
		JTextField dateField = new JTextField();
		FormatUtils.addPlaceHolder(dateField, "jj/MM/aaaa");
		JLabel startHour = new JLabel("Heure début :");
		JTextField startHourField = new JTextField();
		FormatUtils.addPlaceHolder(startHourField, "8h ou 8h30");
		JLabel endHour = new JLabel("Heure fin :");
		JTextField endHourField = new JTextField();
		FormatUtils.addPlaceHolder(endHourField, "10h ou 10h30");

		panel.add(title);
		panel.add(titleField);
		panel.add(date);
		panel.add(dateField);
		panel.add(startHour);
		panel.add(startHourField);
		panel.add(endHour);
		panel.add(endHourField);

		int result = JOptionPane.showConfirmDialog(frame, panel, "Ajouter un événement", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		if (titleField.getText().isBlank() || dateField.getText().isBlank() || dateField.getText().equals("jj/MM/aaaa")
				|| startHourField.getText().isBlank() || startHourField.getText().equals("8h ou 8h30")
				|| endHourField.getText().isBlank() || endHourField.getText().equals("10h ou 10h30")) {
			JOptionPane.showMessageDialog(frame,
					"Veuillez remplir tous les champs pour enregistrer un nouvel événement");
			return;
		}

		LocalTime startTime;
		LocalTime endTime;

		try {
			startTime = FormatUtils.parseTime(startHourField.getText());
			endTime = FormatUtils.parseTime(endHourField.getText());

			if (!endTime.isAfter(startTime)) {
				JOptionPane.showMessageDialog(frame, "L'heure de début doit précéder l'heure de fin");
				return;
			}

		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(frame, "Le format de l'heure est invalide.");
			return;
		}

		LocalDate eventDate;

		try {
			eventDate = FormatUtils.parseDate(dateField.getText());
			Event event = new Event(titleField.getText(), eventDate, startTime, endTime);

			if (agenda.hasOverlap(event)) {
				JOptionPane.showMessageDialog(frame, "Cet événement chevauche un événement existant");
				return;
			}

			agenda.addEvent(event);
			loadTable();

		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(frame, "Format date invalide");
		}

	}

	private void openAddRecurringEvent() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));

		String[] days = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" };

		JLabel title = new JLabel("Titre :");
		JTextField titleField = new JTextField();
		JLabel startDate = new JLabel("Date de début:");
		JTextField startDateField = new JTextField();
		FormatUtils.addPlaceHolder(startDateField, "jj/MM/aaaa");
		JLabel endDate = new JLabel("Date de fin :");
		JTextField endDateField = new JTextField();
		FormatUtils.addPlaceHolder(endDateField, "jj/MM/aaaa");
		JLabel dayOfWeek = new JLabel("Jour de récurrence :");
		JComboBox<String> dayBox = new JComboBox<>(days);
		JLabel startHour = new JLabel("Heure début :");
		JTextField startHourField = new JTextField();
		FormatUtils.addPlaceHolder(startHourField, "8h ou 8h30");
		JLabel endHour = new JLabel("Heure fin :");
		JTextField endHourField = new JTextField();
		FormatUtils.addPlaceHolder(endHourField, "10h ou 10h30");

		panel.add(title);
		panel.add(titleField);
		panel.add(startDate);
		panel.add(startDateField);
		panel.add(endDate);
		panel.add(endDateField);
		panel.add(dayOfWeek);
		panel.add(dayBox);
		panel.add(startHour);
		panel.add(startHourField);
		panel.add(endHour);
		panel.add(endHourField);

		int result = JOptionPane.showConfirmDialog(frame, panel, "Ajouter un événement récurrent",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		if (titleField.getText().isBlank() || startDateField.getText().isBlank()
				|| startDateField.getText().equals("jj/MM/aaaa") || endDateField.getText().isBlank()
				|| endDateField.getText().equals("jj/MM/aaaa") || startHourField.getText().isBlank()
				|| startHourField.getText().equals("8h ou 8h30") || endHourField.getText().isBlank()
				|| endHourField.getText().equals("10h ou 10h30")) {
			JOptionPane.showMessageDialog(frame,
					"Veuillez remplir tous les champs pour enregistrer un nouvel événement récurrent");
			return;
		}

		LocalTime startTime;
		LocalTime endTime;

		try {
			startTime = FormatUtils.parseTime(startHourField.getText());
			endTime = FormatUtils.parseTime(endHourField.getText());

			if (!endTime.isAfter(startTime)) {
				JOptionPane.showMessageDialog(frame, "L'heure de début doit précéder l'heure de fin");
				return;
			}

		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(frame, "Le format de l'heure est invalide.");
			return;
		}

		RecurringEvent recurringEvent;
		try {

			int selectedIndex = dayBox.getSelectedIndex();

			if (selectedIndex != -1) {

				int dayIndex = selectedIndex + 1;

				DayOfWeek dayOfWeekParse = DayOfWeek.of(dayIndex);

				LocalDate startDateParse = FormatUtils.parseDate(startDateField.getText());
				LocalDate endDateParse = FormatUtils.parseDate(endDateField.getText());

				recurringEvent = new RecurringEvent(titleField.getText(), startDateParse, endDateParse, dayOfWeekParse,
						startTime, endTime);

				if (endDateParse.isBefore(startDateParse)) {
					JOptionPane.showMessageDialog(frame, "La date de fin doit être après la date de début");
					return;
				}

				if (!agenda.generateRecurringEvents(recurringEvent)) {
					JOptionPane.showMessageDialog(frame, "Conflit avec un événement existant");
					return;
				}

			} else {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un jour de récurrence");
				return;
			}

			recurringEventList.addRecurringEvent(recurringEvent);
			loadTable();

		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(frame, "Format date invalide");
		}
	}

	private void loadTable() {

		tableModel.setRowCount(0);

		for (Event event : agenda.getEvents()) {

			String title = event.getTitle();
			String startDate = FormatUtils.formatDate(event.getDate());
			String startTime = FormatUtils.formatTime(event.getStartTime());
			String endTime = FormatUtils.formatTime(event.getEndTime());
			String type = "";

			if (event.getRecurringId() == null) {
				type = "Unique";
			} else {
				type = "Récurrent";
			}

			Object[] row = { title, startDate, startTime, endTime, type };

			tableModel.addRow(row);
		}
	}
}
