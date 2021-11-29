package view;

import model.Board;

import javax.swing.*;

import controller.MultiClients;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class ChessView extends JFrame implements ActionListener {
	
	private MultiClients ClientThread;
	private GridBagConstraints gbc;

    private Board board;
    private BoardPanel boardPanel;
    private PrintWriter printWriter;

    private JPanel panel;
    private JComboBox listbox;
    private JTextArea textArea;
    private JTextField message;
    private JTextField room;
    private JLabel label;
    private JLabel label2;
    private JButton exit;
  
    public ChessView(Board board){
        
        try {
        	Socket s = new Socket("localhost",3333);	
			ClientThread = new MultiClients(s,this);
			ClientThread.start();
        }  catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.board = board;
        
        JFrame frame = new JFrame("Chess");
        frame.setSize(930,530);
        frame.setLayout(new GridLayout(1,2));
        frame.setResizable(false);
        
        boardPanel = new BoardPanel(board,ClientThread);
        frame.add(boardPanel);
        
        panel = new JPanel();
        panel.setLayout(null);
        
        label2 = new JLabel("Create Room");
        label2.setBounds(35, 25, 140, 34);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panel.add(label2);
        
        room = new JTextField();
        room.setBounds(170, 30, 155, 31);
        room.setFont(new Font("Tahoma", Font.PLAIN, 20));
        room.setColumns(10);
        panel.add(room);
        
        listbox = new JComboBox();
        listbox.setBounds(330, 30, 100, 31);
        listbox.setFont(new Font("Tahoma", Font.PLAIN, 20));
        listbox.setModel(new DefaultComboBoxModel(new String[] {"Room 1", "Room 2", "Room 3", "Room 4", "Room 5"}));
        panel.add(listbox);
        
        textArea = new JTextArea();
        textArea.setBounds(40, 65, 390, 370);
        panel.add(textArea);
        
        label = new JLabel("Message");
        label.setBounds(35, 440, 89, 34);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panel.add(label);
        
        message = new JTextField();
        message.setBounds(125, 445, 230, 31);
        message.setFont(new Font("Tahoma", Font.PLAIN, 20));
        message.setColumns(10);
        panel.add(message);
        
        exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				Menu_GUI menu = new Menu_GUI();
				menu.setVisible(true);
			}
        }); 
        exit.setBackground(new Color(244, 164, 96));
        exit.setFont(new Font("Tahoma", Font.PLAIN, 20));
        exit.setBounds(360, 445, 70, 30);
        panel.add(exit);
        
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
//    private class thehandler implements ActionListener{
//    	public void actionPerformed(ActionEvent event){
//
//			String string = "";
//
//			if(event.getSource()==messageField)
//			{
//				string=String.format("%s", event.getActionCommand());
//				String text= messageField.getText();
//				if(!text.equals(""))
//				{
//					ClientThread.ClientOutServerIn("msg:"+text);
//					messageField.setText("");
//				}
//			}
//			else if(event.getSource()==usernameField) {
//				string=String.format("%s", event.getActionCommand());
//				if(string.matches("[0-9]*"))
//				{
//					JOptionPane.showMessageDialog(null,"formate not allowed");
//					usernameField.setText("");
//				}
//				else
//				{
//					ClientThread.setName(string);
//					ClientThread.SetClient("channel0",string);
//					JOptionPane.showMessageDialog(null, "name has been set: "+string);
//					usernameField.setText("");
//					usernameField.setEditable(false);
//					messageField.setEditable(true);
//					channelField.setEditable(true);
//					ClientThread.ClientOutServerIn("new user");
//					usernameField.setVisible(false);
//				}
//			}
//			else if(event.getSource()==channelField) {
//				string=String.format("%s", event.getActionCommand());
//				if(string.matches("[a-z A-Z]"))
//				{
//					JOptionPane.showMessageDialog(null,"formate not allowed");
//					channelField.setText("");
//				}
//				else
//				{
//
//					ClientThread.cd.SetChannel("channel"+string);
//					JOptionPane.showMessageDialog(null, "Channel has been set: channel"+string);
//					channelField.setText("");
//					ClientThread.ClientOutServerIn("change channel");
//				}
//			}
//			
//		}
//	}
//	public void setDisplay(String x)
//	{
//		messageArea.append(x + "\n"); 
//	}
//	public void setUserInChannel(String x)
//	{
//		userInfoArea.append(x + "\n");
//	}
//	public void ClearDisplay()
//	{
//		userInfoArea.setText("");
//	}

	public void repa(String s)
	{

		boardPanel.ReCreate(s);
		System.out.println("ok");
		
	}
	public void executeMove(int startSquare, int targetSquare) {
		board.executeMove(board.getMove(startSquare, targetSquare));
		boardPanel.repaint();
	}
}

