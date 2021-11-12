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

    private Board board;
    private BoardPanel boardPanel;
    private PrintWriter printWriter;

    private JTextField messageField;
    private JTextField usernameField;
    private JTextField channelField;
    private JTextArea messageArea;
    private JTextArea userInfoArea;

    private JPanel panel;
    private JLabel message;
    private JLabel username;
    private JLabel channel;
  
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
        //Khoi tao giao dien
        JFrame frame = new JFrame("Chess");
        frame.setSize(1200,600);
        frame.setLayout(new GridLayout(1,2));
        //Khong cho phep thay doi kich thuoc
        frame.setResizable(false);

        boardPanel = new BoardPanel(board,ClientThread);
        frame.add(boardPanel);

        panel = new JPanel();
        messageArea = new JTextArea(30, 40);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        messageArea.setEditable(false);
        panel.add(scrollPane);

        userInfoArea = new JTextArea(30, 10);
        JScrollPane scrollPane3 = new JScrollPane(userInfoArea);
        userInfoArea.setEditable(false);

        panel.add(scrollPane3);

        channel=new JLabel("Channel");
        username=new JLabel("Name");
        message = new JLabel("Message");

        messageField = new JTextField(20);
        messageField.setEditable(false);

        panel.setLayout(new FlowLayout());
        panel.add(message);
        panel.add(messageField);

        usernameField = new JTextField(20);
        usernameField.setEditable(true);

        panel.setLayout(new FlowLayout());
        panel.add(username);
        panel.add(usernameField);

        channelField = new JTextField(20);
        channelField.setEditable(false);

        panel.setLayout(new FlowLayout());
        panel.add(channel);
        panel.add(channelField);

        JButton b1 = new JButton("New game");
        panel.add(b1);
        
        
		thehandler handler = new thehandler();
		messageField.addActionListener(handler);
		usernameField.addActionListener(handler); 
		channelField.addActionListener(handler);

        frame.add(panel);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    private class thehandler implements ActionListener{
    	public void actionPerformed(ActionEvent event){

			String string = "";

			if(event.getSource()==messageField)
			{
				string=String.format("%s", event.getActionCommand());
				String text= messageField.getText();
				if(!text.equals(""))
				{
					ClientThread.ClientOutServerIn("msg:"+text);
					messageField.setText("");
				}
			}
			else if(event.getSource()==usernameField) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[0-9]*"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					usernameField.setText("");
				}
				else
				{
					ClientThread.setName(string);
					ClientThread.SetClient("channel0",string);
					JOptionPane.showMessageDialog(null, "name has been set: "+string);
					usernameField.setText("");
					usernameField.setEditable(false);
					messageField.setEditable(true);
					channelField.setEditable(true);
					ClientThread.ClientOutServerIn("new user");
					usernameField.setVisible(false);
				}
			}
			else if(event.getSource()==channelField) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[a-z A-Z]"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					channelField.setText("");
				}
				else
				{

					ClientThread.cd.SetChannel("channel"+string);
					JOptionPane.showMessageDialog(null, "Channel has been set: channel"+string);
					channelField.setText("");
					ClientThread.ClientOutServerIn("change channel");
				}
			}
			
		}
	}
	public void setDisplay(String x)
	{
		messageArea.append(x + "\n"); 
	}
	public void setUserInChannel(String x)
	{
		userInfoArea.append(x + "\n");
	}
	public void ClearDisplay()
	{
		userInfoArea.setText("");
	}

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

