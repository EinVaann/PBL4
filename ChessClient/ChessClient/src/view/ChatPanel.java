package view;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel implements ActionListener {
	public JTextField messageField;
	public JTextField channelField;
	public JTextArea messageArea;
	public controller.ClientThread ClientThread;
	public JLabel lblNewLabel;
	public JLabel NameChannel;
	public JButton Exit;
	
	

	public ChatPanel(controller.ClientThread clientThread) {
		ClientThread = clientThread;
		setLayout(null);
		setBounds(700,700,444,514);

		lblNewLabel = new JLabel("Create Channel");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(25, 30, 150, 19);
		add(lblNewLabel);

		
		channelField = new JTextField();
		channelField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		channelField.setColumns(10);
		channelField.setBounds(180, 28, 162, 23);
		add(channelField);

		NameChannel = new JLabel("Channel");
		NameChannel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		NameChannel.setBounds(25, 30, 140, 19);
		add(NameChannel);
		
		Exit = new JButton("Leave");
		Exit.setBounds(321, 28, 99, 24);	
		Exit.setVisible(false);
		NameChannel.setVisible(false);
		add(Exit);

		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMessage.setBounds(25, 451, 83, 23);
		add(lblMessage);
		
		messageField = new JTextField();
		messageField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		messageField.setBounds(107, 451, 313, 23);
		add(messageField);
		messageField.setColumns(10);

		messageField.addActionListener(this);
		channelField.addActionListener(this);

		
		messageArea = new JTextArea(30, 40);

	    JScrollPane scrollPane = new JScrollPane(messageArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(25, 63, 395, 380);
	    messageArea.setEditable(false);
	    add(scrollPane);

	}
	
	
	public void setDisplay(String x)
	{
		messageArea.append(x + "\n"); 
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String s = "";

		if(event.getSource()==messageField)
		{
			s = String.format("%s", event.getActionCommand());
			String text= messageField.getText();
			if(!text.equals(""))
			{
				ClientThread.sendToServer("/msg "+ClientThread.getNameClient()+" "+text);
				messageField.setText("");
			}
		}
		else if(event.getSource()==channelField) {
			s = String.format("%s", event.getActionCommand());
			if(s.matches("[a-z A-Z]"))
			{
				JOptionPane.showMessageDialog(null,"formate not allowed, please type in number");
				channelField.setText("");
			}
			else
			{
				channelField.setText("");
				NameChannel.setText("Channel "+s);
				ClientThread.sendToServer("/channel "+s);
			}
		}
		
	}

	public void setChannelView() {
		Exit.setVisible(true);
		NameChannel.setVisible(true);
		lblNewLabel.setVisible(false);
		channelField.setVisible(false);
	}
}

 
