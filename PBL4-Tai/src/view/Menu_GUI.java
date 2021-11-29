package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Board;
import model.Calculate;

import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Menu_GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu_GUI frame = new Menu_GUI();
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
	public Menu_GUI() {
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 419, 535);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnQuitGame = new JButton("Quit Game");
		btnQuitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}); 
		btnQuitGame.setBackground(new Color(244, 164, 96));
		btnQuitGame.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnQuitGame.setBounds(117, 442, 170, 43);
		contentPane.add(btnQuitGame);
		
		JButton btnOption = new JButton("Options");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Options_GUI option = new Options_GUI();
				option.setVisible(true);
			}
		});
		btnOption.setBackground(new Color(244, 164, 96));
		btnOption.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnOption.setBounds(117, 388, 170, 43);
		contentPane.add(btnOption);
		
		JButton btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Board board = new Board();
		        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		        Calculate c = new Calculate();
		        board.CreateBoard(fenCode);
		        board.ShowBoard();
		        PreviewChess pc = new PreviewChess(board); 
			}
		});
		btnSinglePlayer.setForeground(new Color(0, 0, 0));
		btnSinglePlayer.setBackground(new Color(244, 164, 96));
		btnSinglePlayer.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSinglePlayer.setBounds(117, 280, 170, 43);
		contentPane.add(btnSinglePlayer);
		
		JButton btnMultiPlayers = new JButton("MultiPlayers");
		btnMultiPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Board board = new Board();
		        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		        Calculate c = new Calculate();
		        board.CreateBoard(fenCode);
		        board.ShowBoard();
		        ChessView cv = new ChessView(board);
			}
		});
		btnMultiPlayers.setBackground(new Color(244, 164, 96));
		btnMultiPlayers.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnMultiPlayers.setBounds(117, 334, 170, 43);
		contentPane.add(btnMultiPlayers);
		
		JLabel lbLogo_Chess = new JLabel();
		lbLogo_Chess.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lbLogo_Chess.setBounds(10, 11, 383, 199);
		contentPane.add(lbLogo_Chess);
		
		File imgFile = new File("resource\\logo_Chess.jpg");
		Image logo = null;
		try {
			logo = ImageIO.read(imgFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Image logoScale = logo.getScaledInstance(lbLogo_Chess.getWidth(), lbLogo_Chess.getHeight(), Image.SCALE_SMOOTH);
		lbLogo_Chess.setIcon(new ImageIcon(logoScale));
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtName.setBounds(185, 238, 208, 31);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter Your Name");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 238, 162, 29);
		contentPane.add(lblNewLabel);
	}
}
