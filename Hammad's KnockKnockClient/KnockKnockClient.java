import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class KnockKnockClient extends JFrame {

	private JPanel contentPane;
	private JTextField txtIn;
	private JTextField textField_1;
	private JTextField textField;
	public boolean connected = false;
	private Socket socket = null; 


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KnockKnockClient frame = new KnockKnockClient();
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
	public KnockKnockClient() {
		setTitle("Knock Knock Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		txtIn = new JTextField();
		txtIn.setText("constance.cs.rutgers.edu");
		txtIn.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("5520");
		textField_1.setColumns(10);
		
				
		JTextArea textArea = new JTextArea();

		
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!connected)
				{
					try
					{
				        socket = new Socket(txtIn.getText(), Integer.valueOf(textField_1.getText()));
				        textArea.append("Connected to Server\n");
				        connectButton.setText("Disconnect");
				        connected = !connected;
				    } 
					catch (IOException e1)
					{
						textArea.append("Could not connect to server\n");
					}
				}
				else
				{
					try
					{
						socket.close();
						textArea.append("Disconnected!\n");
						connectButton.setText("Connect");
						connected = !connected;
					}
					catch(IOException e2)
					{
						textArea.append("Could not disconnect from server\n");
					}
				}
			}
		});
		
		JLabel lblIpAddress = new JLabel("IP Address");
		
		JLabel lblPort = new JLabel("Port Number");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblMessageToServer = new JLabel("Message to Server");
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!connected)
					textArea.append("Not connected to server \n");
				else
				{
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out.println(textField.getText());
						textArea.append("Client: " + textField.getText() + "\n");
						textArea.append("Server: " + in.readLine() + "\n");
						if(textField.getText().equalsIgnoreCase("quit"))
						{
							socket.close();
							connected = !connected;
							connectButton.setText("Connect");
							textArea.append("Disconnected!\n");
						}
					}
					catch(UnknownHostException e3)
					{
						textArea.append("Not connected to server \n");
					}
					catch(IOException e4)
					{
						textArea.append("Could not send \n");
					}
					textField.setText("");
				}
			}
		});
		
		
		JLabel lblClientserverCommunication = new JLabel("Client/Server Communication");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPort)
								.addComponent(lblIpAddress))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(txtIn, 184, 184, 184)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(connectButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(lblMessageToServer)))
					.addGap(16))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(sendButton)
							.addGap(40))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(6)
									.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
									.addGap(10))
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
							.addGap(21))))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblClientserverCommunication, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(350, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIpAddress)
						.addComponent(txtIn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(connectButton)
						.addComponent(lblPort)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblMessageToServer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sendButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblClientserverCommunication)
					.addGap(18)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
