package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Board;
import model.Calculate;

public class PreviewChess extends JFrame {
    
	private Board board;
	private PreviewBoardPanel boardPanel;
	private JPanel contentPane;
	private JButton exit;
	
    public PreviewChess(Board board) {
    	 this.board = board;
 
    	 setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setSize(600,600);
         setResizable(false);
         setLayout(new GridLayout(1,2));
         
         boardPanel = new PreviewBoardPanel(board);
         add(boardPanel);
         
         contentPane = new JPanel();
         contentPane.setLayout(null);
         
         exit = new JButton("Exit Game");
         exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Menu_GUI menu = new Menu_GUI();
				menu.setVisible(true);
			}
         }); 
         exit.setBackground(new Color(244, 164, 96));
         exit.setFont(new Font("Tahoma", Font.PLAIN, 20));
         exit.setBounds(117, 442, 170, 43);
         contentPane.add(exit);
         add(contentPane);
         
         setVisible(true);
    }
	
    public static void main(String[] args) {
    	Board board = new Board();
        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Calculate c = new Calculate();
        board.CreateBoard(fenCode);
        board.ShowBoard();
        PreviewChess pc = new PreviewChess(board);
	}
} 
