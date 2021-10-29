package backup;

import model.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ServerAndClient.MultiClients;

@SuppressWarnings("serial")
public class ChessController extends JFrame implements ActionListener {

    //private ChessModel chessModel = new ChessModel();
    private Board board;
    private ChessView panel;
    private PrintWriter printWriter;
    
    
	private JTextField item1;
	private JTextField username;
	private JTextField item3;
	private JTextArea display;
	private JTextArea UserNames;
	private JPanel c;
	MultiClients ClientThread;
	private JLabel label;
	private JLabel label1;
	private JLabel label2;
	private final static String newline = "\n";

    public ChessController(Board board) {
        //chessModel.reset();
		try {
			Socket s = new Socket("localhost",3333);
			ClientThread = new MultiClients(s,this);
			ClientThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.board = board;
        JFrame frame = new JFrame("Chess");
        frame.setSize(1200, 600);
        frame.setLayout(new GridLayout(1,2));
        

        
        frame.setResizable(false);
        
        panel = new ChessView(board,ClientThread);

        
        frame.add(panel);
        c = new JPanel();
		display = new JTextArea(30, 40);
		JScrollPane scrollPane = new JScrollPane(display); 
		display.setEditable(false);
        c.add(scrollPane);
        
		UserNames = new JTextArea(30, 10);
		JScrollPane scrollPane3 = new JScrollPane(UserNames); 
		UserNames.setEditable(false);
		
		c.add(scrollPane3);
		
		label2=new JLabel("Message");
		c.add(label2);
		item1 = new JTextField(20);
		item1.setEditable(false);
		c.add(item1);
        
        username = new JTextField(20);
        username.setEditable(true);
		c.add(username);

		label=new JLabel("channel number");
		label1=new JLabel("Name");
		
	
		c.setLayout(new FlowLayout());
		c.add(label1);
		c.add(username);
		
		item3 = new JTextField(20);
		item3.setEditable(false);
			
		c.setLayout(new FlowLayout());
		c.add(label);
		c.add(item3);
		JButton b1 = new JButton("New game");
		c.add(b1);
		
		thehandler handler = new thehandler();
		item1.addActionListener(handler);
		username.addActionListener(handler); 
		item3.addActionListener(handler);
		

		
		
		frame.add(c);
		
        frame.setVisible(true);      
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

   /* public static void main(String[] args) {
        new ChessController();
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

   /* @Override
    public ChessPiece pieceAt(int col, int row) {
        return chessModel.pieceAt(col, row);
    }

    @Override
    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow);
        //panel.repaint();
        if(printWriter != null) {
            printWriter.println(fromCol + "," + fromRow + "," + toCol + "," + toRow);
        }
    }*/
    private class thehandler implements ActionListener{
		public void actionPerformed(ActionEvent event){

			String string = "";

			if(event.getSource()==item1)
			{
				string=String.format("%s", event.getActionCommand());
				String text= item1.getText();
				ClientThread.ClientOutServerIn(text);
				item1.setText("");
			}
			else if(event.getSource()==username) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[0-9]*"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					username.setText("");
				}
				else
				{
					ClientThread.setName(string);
					ClientThread.SetClient("channel0",string);
					JOptionPane.showMessageDialog(null, "name has been set: "+string);
					username.setText("");
					username.setEditable(false);
					item1.setEditable(true);
					item3.setEditable(true);
					ClientThread.ClientOutServerIn("new user");
					label1.setVisible(false);
				}
			}
			else if(event.getSource()==item3) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[a-z A-Z]"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					item3.setText("");
				}
				else
				{

					ClientThread.cd.SetChannel("channel"+string);
					JOptionPane.showMessageDialog(null, "Channel has been set: channel"+string);
					item3.setText("");
					ClientThread.ClientOutServerIn("change channel");
				}
			}
			//JOptionPane.showMessageDialog(null, string);
		}
	}
	public void setDisplay(String x)
	{
		display.append(x + newline); 
	}
	public void setUserInChannel(String x)
	{
		UserNames.append(x + newline);
	}
	public void ClearDisplay()
	{
		UserNames.setText("");
	}
	public void StopChangeChannel() {
		item3.setEditable(false);
	}
	public void repa(String s)
	{
		panel.ReCreate(s);
	}
}
