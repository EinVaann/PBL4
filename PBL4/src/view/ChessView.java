package view;

import model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class ChessView extends JFrame implements ActionListener {

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
        this.board = board;

        //Khoi tao giao dien
        JFrame frame = new JFrame("Chess");
        frame.setSize(1200,600);
        frame.setLayout(new GridLayout(1,2));
        //Khong cho phep thay doi kich thuoc
        frame.setResizable(false);

        boardPanel = new BoardPanel(board);
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


        frame.add(panel);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
