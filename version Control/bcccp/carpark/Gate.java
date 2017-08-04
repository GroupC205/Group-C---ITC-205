package bcccp.carpark;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

@SuppressWarnings("serial")
public class Gate extends JFrame implements IGate {

	private JPanel contentPane;
	private JTextField gateStatusTextField;
	private boolean raised;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Gate frame = new Gate(100, 100);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			Thread.sleep(2000);
			frame.raise();
			Thread.sleep(2000);
			frame.lower();			
		}
		catch (InterruptedException e) {}
	}

	/**
	 * Create the frame.
	 */
	public Gate(int x, int y) {
		setTitle("Gate");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 310, 116);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		gateStatusTextField = new JTextField();
		gateStatusTextField.setBackground(Color.RED);
		gateStatusTextField.setEditable(false);
		gateStatusTextField.setHorizontalAlignment(SwingConstants.CENTER);
		gateStatusTextField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gateStatusTextField.setText("Gate Down");
		gateStatusTextField.setBounds(5, 5, 279, 64);
		contentPane.add(gateStatusTextField);
		gateStatusTextField.setColumns(10);
	}

	
	
	@Override
	public void raise() {
		gateStatusTextField.setBackground(Color.GREEN);
		gateStatusTextField.setText("Gate Up");
		raised = true;		
	}

	
	
	@Override
	public void lower() {
		gateStatusTextField.setBackground(Color.RED);
		gateStatusTextField.setText("Gate Down");
		raised = false;		
	}

	
	
	@Override
	public boolean isRaised() {
		return raised;
	}

	
}
