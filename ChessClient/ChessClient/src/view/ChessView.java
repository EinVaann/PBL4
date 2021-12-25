package view;

import model.Board;
import model.Move;

import javax.swing.*;

import controller.ClientThread;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.*;

public class ChessView extends JFrame implements ActionListener{

	public controller.ClientThread ClientThread;

    public Board board;
    public BoardPanel boardPanel;
    public ChatPanel chat;
    

    public ChessView(Board board,String name, boolean isMultiplayers){
        
        try {
        	Socket s = new Socket("localhost",3333);	
			ClientThread = new ClientThread(s,this);
			ClientThread.NameClient=name;
			ClientThread.start();
        }  catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.board = board;
        
        JFrame frame = new JFrame("Chess");
        frame.setResizable(false);
        
        if (isMultiplayers) {
        	frame.setSize(1060,570);
            frame.setLayout(new GridLayout(1,2));
            boardPanel = new BoardPanel(board,ClientThread);
            frame.add(boardPanel);
            chat = new ChatPanel(ClientThread);
            frame.add(chat);
            chat.Exit.addActionListener(this);
        } else {
        	frame.setSize(530,570);
            frame.setLayout(new GridLayout(1,1));
            boardPanel = new BoardPanel(board,ClientThread);
            frame.add(boardPanel);
            ClientThread.sendToServer("/channelsp");
        }
        frame.setVisible(true);
		
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                    dispose();
                    Menu_GUI menu = new Menu_GUI();
                    ClientThread.sendToServer("/leave");
                    menu.setVisible(true);
            }
        });
    }
	
	public void executeMove(int startSquare, int targetSquare) {
        board.executeMove(new Move(startSquare, targetSquare));
        boardPanel.repaint();
    }

	public void addMove(Move move){
		board.moves.add(move);
	}

	public void setChannelView() {
		chat.setChannelView();
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==chat.Exit){
            chat.Exit.setVisible(false);
            chat.NameChannel.setVisible(false);
            chat.lblNewLabel.setVisible(true);
            chat.channelField.setText("");
            chat.channelField.setVisible(true);
            chat.messageArea.setText("");
            ClientThread.sendToServer("/leave");
            board.CreateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 1111 - 0 1");
            System.out.println("???");
            boardPanel.repaint();
           // ClientThread.sendToServer("/listChannel");
        }
    }

    public void showMessage(String s){
        JOptionPane.showMessageDialog(null,s);
    }
}

