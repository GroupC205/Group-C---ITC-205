package bcccp.carpark.paystation;

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
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class PaystationUI extends JFrame implements IPaystationUI {

	private JPanel contentPane;
	private JTextField displayTextField;
	private JTextField barcodeTextField;
	private IPaystationController controller;
	private JTextArea ticketPrinterTextArea;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PaystationUI frame = new PaystationUI(100, 100);
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
	public PaystationUI(int x, int y) {
		setTitle("PayStation UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 350, 710);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "LCD Display", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(5, 5, 320, 106);
		contentPane.add(panel);
		panel.setLayout(null);
		
		displayTextField = new JTextField();
		displayTextField.setHorizontalAlignment(SwingConstants.CENTER);
		//displayTextField.setText("Push Button");
		displayTextField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		displayTextField.setEditable(false);
		displayTextField.setBounds(10, 15, 298, 82);
		panel.add(displayTextField);
		displayTextField.setColumns(10);
		
		JButton issueAdhocTicketButton = new JButton("Pay");
		issueAdhocTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pay();
			}

		});
		issueAdhocTicketButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		issueAdhocTicketButton.setBounds(25, 283, 287, 46);
		contentPane.add(issueAdhocTicketButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ticket Reader", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(15, 122, 310, 153);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		barcodeTextField = new JTextField();
		barcodeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		barcodeTextField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		barcodeTextField.setBounds(10, 21, 285, 53);
		panel_1.add(barcodeTextField);
		barcodeTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("Read Ticket");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ticketInserted();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton.setBounds(10, 85, 285, 45);
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Paystation Ticket Printer", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(5, 340, 320, 321);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		ticketPrinterTextArea = new JTextArea();
		ticketPrinterTextArea.setBackground(Color.LIGHT_GRAY);
		ticketPrinterTextArea.setText("");
		ticketPrinterTextArea.setRows(10);
		ticketPrinterTextArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ticketPrinterTextArea.setEditable(false);
		ticketPrinterTextArea.setBounds(10, 22, 295, 230);
		panel_2.add(ticketPrinterTextArea);
		
		JButton btnNewButton_1 = new JButton("Take Ticket");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeTicket();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton_1.setBounds(20, 263, 285, 45);
		panel_2.add(btnNewButton_1);
	}

	
	
	@Override
	public void registerController(IPaystationController controller) {
		this.controller = controller;
	}

	
	
	@Override
	public void deregisterController() {
		this.controller = null;	
	}
	
	
	
	private void ticketInserted() {
		String ticketStr = barcodeTextField.getText();
		controller.ticketInserted(ticketStr);	
	}
	
	
	
	@Override
	public void display(String message) {
		displayTextField.setText(message);	
	}

	
	
	@Override
	public void beep() {
		Toolkit.getDefaultToolkit().beep();	
	}
	
	
	
	private void pay() {
		log("pay : calling ticketPaid");
		controller.ticketPaid();	
	}

	
	
	private void takeTicket() {
		controller.ticketTaken();
		ticketPrinterTextArea.setText("");
		barcodeTextField.setText("");
	}

	
	
	private void log(String message) {
		System.out.println("EntryUI : " + message);
	}

	
	
	@Override
	public void printTicket(String carparkId, int tNo, long entryTime, long paidTime, float charge, String barcode) {
		Date entryDate = new Date(entryTime);
		Date paidDate = new Date(paidTime);
		StringBuilder builder = new StringBuilder();
		builder.append("Carpark    : " + carparkId + "\n");
		builder.append("Ticket No  : " + tNo + "\n");
		builder.append("Entry Time : " + entryDate + "\n");
		builder.append("Paid  Time : " + paidDate + "\n");
		builder.append("Charge     : " + String.format("%.2f", charge) + "\n");
		builder.append("Barcode    : " + barcode + "\n");
		
		ticketPrinterTextArea.setText(builder.toString());			
	}

	
	
}
