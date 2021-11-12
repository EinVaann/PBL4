package view;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Board;
import model.Calculate;

public class PreviewChess extends JFrame {
    
	private Board board;
	
    public PreviewChess(Board board) {
    	 this.board = board;
 
         JFrame frame = new JFrame("Chess");
         frame.setSize(600,600);
         frame.setLayout(new GridLayout(1,2));
         frame.setResizable(false);

         PreviewBoardPanel PreviewboardPanel = new PreviewBoardPanel(board);
         frame.add(PreviewboardPanel);

         frame.setVisible(true);
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
