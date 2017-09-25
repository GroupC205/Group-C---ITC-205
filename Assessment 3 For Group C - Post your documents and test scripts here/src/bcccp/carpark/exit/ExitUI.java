package bcccp.carpark.exit;

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
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ExitUI extends JFrame implements IExitUI {

	private JPanel contentPane;
	private JTextField displayTextField;
	private JTextField ticketReaderTextField;
	private IExitController controller;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExitUI frame = new ExitUI(100, 100);
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
	public ExitUI(int x, int y) {
		setTitle("Exit Pillar UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 340, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "LCD Display", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(5, 5, 316, 106);
		contentPane.add(panel);
		panel.setLayout(null);
		
		displayTextField = new JTextField();
		displayTextField.setHorizontalAlignment(SwingConstants.CENTER);
		//displayTextField.setText("Push Button");
		displayTextField.setFont(new Font("Tahoma", Font.PLAIN, 28));
		displayTextField.setEditable(false);
		displayTextField.setBounds(10, 15, 296, 82);
		panel.add(displayTextField);
		displayTextField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ticket Reader", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(15, 115, 306, 153);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		ticketReaderTextField = new JTextField();
		ticketReaderTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ticketReaderTextField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		ticketReaderTextField.setBounds(10, 21, 285, 53);
		panel_1.add(ticketReaderTextField);
		ticketReaderTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("Read Ticket");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readTicket();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton.setBounds(10, 85, 285, 45);
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Take Ticket");
		btnNewButton_1.setBounds(25, 279, 285, 45);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeTicket();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
	}

	
	
	@Override
	public void registerController(IExitController controller) {
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

	
	
	private void readTicket() {
		String ticketStr = ticketReaderTextField.getText();
		controller.ticketInserted(ticketStr);	
	}
	
	
	
	private void takeTicket() {
		controller.ticketTaken();
		ticketReaderTextField.setText("");
	}

	
	
	@SuppressWarnings("unused")
	private void log(String message) {
		System.out.println("EntryUI : " + message);
	}

	
	
	
	@Override
	public void discardTicket() {
		ticketReaderTextField.setText("");
	}

	
}
