package miniagendaswing;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		
		frame.add(mainPanel);
		frame.setVisible(true);
	}
	
	private void loadTable() {
		
	}
}
