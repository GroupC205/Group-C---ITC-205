package bcccp.carpark.entry;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Color;

@SuppressWarnings("serial")
public class EntryUI extends JFrame implements IEntryUI {

	private JPanel contentPane;
	private JTextField displayTextField;
	private JTextField seasonTicketTextField;
	private IEntryController controller;
	private JTextArea ticketPrinterTextArea;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EntryUI frame = new EntryUI(100, 100);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the frame.
	 */
	public EntryUI(int x, int y) {
		setTitle("Entry Pillar UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 340, 710);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "LCD Display", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 306, 106);
		contentPane.add(panel);
		panel.setLayout(null);
		
		displayTextField = new JTextField();
		displayTextField.setHorizontalAlignment(SwingConstants.CENTER);
		//displayTextField.setText("Push Button");
		displayTextField.setFont(new Font("Tahoma", Font.PLAIN, 32));
		displayTextField.setEditable(false);
		displayTextField.setBounds(10, 15, 288, 82);
		panel.add(displayTextField);
		displayTextField.setColumns(10);
		
		JButton issueAdhocTicketButton = new JButton("Issue Adhoc Ticket");
		issueAdhocTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushButton();
			}

		});
		issueAdhocTicketButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		issueAdhocTicketButton.setBounds(15, 119, 287, 46);
		contentPane.add(issueAdhocTicketButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Season Ticket Reader", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 176, 306, 153);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		seasonTicketTextField = new JTextField();
		seasonTicketTextField.setHorizontalAlignment(SwingConstants.CENTER);
		seasonTicketTextField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		seasonTicketTextField.setBounds(10, 21, 285, 53);
		panel_1.add(seasonTicketTextField);
		seasonTicketTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("Validate Season Ticket");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				insertTicket();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton.setBounds(10, 85, 285, 45);
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Entry Ticket Printer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(5, 340, 311, 321);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		ticketPrinterTextArea = new JTextArea();
		ticketPrinterTextArea.setBackground(Color.LIGHT_GRAY);
		ticketPrinterTextArea.setText("");
		ticketPrinterTextArea.setRows(10);
		ticketPrinterTextArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ticketPrinterTextArea.setEditable(false);
		ticketPrinterTextArea.setBounds(10, 22, 285, 230);
		panel_2.add(ticketPrinterTextArea);
		
		JButton btnNewButton_1 = new JButton("Take Ticket");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeTicket();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton_1.setBounds(10, 263, 285, 45);
		panel_2.add(btnNewButton_1);
	}

	
	
	@Override
	public void registerController(IEntryController controller) {
		this.controller = controller;
	}

	
	
	@Override
	public void deregisterController() {
		this.controller = null;	
	}
	
	
	
	@Override
	public void display(String message) {
		displayTextField.setText(message);	
	}

	
	
	@Override
	public void beep() {
		Toolkit.getDefaultToolkit().beep();	
	}
	
	
	
	private void pushButton() {
		log("pushButton : calling button pushed");
		controller.buttonPushed();	
	}

	
	
	private void insertTicket() {
		String ticketStr = seasonTicketTextField.getText();
		controller.ticketInserted(ticketStr);	
	}
	
	
	
	private void takeTicket() {
		controller.ticketTaken();
		ticketPrinterTextArea.setText("");
		seasonTicketTextField.setText("");
	}

	
	
	private void log(String message) {
		System.out.println("EntryUI : " + message);
	}

	
	
	@Override
	public void printTicket(String carparkId, int tNo, long datetime, String barcode) {
		Date entryDate = new Date(datetime);
		StringBuilder builder = new StringBuilder();
		builder.append("Carpark    : " + carparkId + "\n");
		builder.append("Ticket No  : " + tNo + "\n");
		builder.append("Entry Time : " + entryDate + "\n");
		builder.append("Barcode    : " + barcode + "\n");
		
		ticketPrinterTextArea.setText(builder.toString());			
	}

	
	
	@Override
	public void discardTicket() {
		seasonTicketTextField.setText("");
		ticketPrinterTextArea.setText("");	
	}

	
	
	@Override
	public boolean ticketPrinted() {
		return ticketPrinterTextArea.getText().length() != 0;
	}


}
