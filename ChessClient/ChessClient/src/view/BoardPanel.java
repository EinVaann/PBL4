package view;

import model.Board;
import model.ImageData;
import model.Move;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final Board board;

    private final controller.ClientThread ClientThread;

    private Map<Integer, Image> keyNameValueImage = new HashMap<Integer, Image>();

    private String RGB = chooseColor();
    private String[] rgb = RGB.split("  ");

    private String Switch = tunrOnOff();
    private String[] onOff = Switch.split(" ");
    private boolean turnOnSound = Boolean.parseBoolean(onOff[0]);
    private boolean turnOnShowMoves = Boolean.parseBoolean(onOff[1]);

    Color brown =  new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    Color littleWhite = new Color(Integer.parseInt(rgb[3]), Integer.parseInt(rgb[4]), Integer.parseInt(rgb[5].trim()));
    Color highlight = new Color(252, 153, 111, 127);
    private int originX = -1;
    private int originY = -1;
    private int cellSize = -1;

    private int fromCol = -1;
    private int fromRow = -1;

    private boolean isDrag;
    private int dragX;
    private int dragY;
    private int dragImgName;

    private String turn;
    private List<Move> possibleMove = new ArrayList<Move>();

    public BoardPanel(Board board, controller.ClientThread ClientThread) {
        this.board = board;
        this.ClientThread = ClientThread;
        turn = board.TurnColor == 16 ? "Black" : "White";
        this.keyNameValueImage = ImageData.getInstance().keyNameValueImage;

        addMouseListener(this);
        addMouseMotionListener(this);
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e->{
            ClientThread.sendToServer("/newGame");
        });
        add(newGame);
    }

    private String tunrOnOff() {
        String onOff = "";
        try {
            File file = new File("resource\\Sound.txt");
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()) {
                String name = myReader.next() + myReader.next() + myReader.next();
                String onAndOff = myReader.next();
                onOff += onAndOff + " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onOff;
    }

    private String chooseColor() {
        String RGB = "";
        try {
            File file = new File("resource\\Color.txt");
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()) {
                String colorName = myReader.next();
                String specialChar = myReader.next();
                RGB += myReader.nextLine().replaceAll("[^a-zA-Z0-9]", " ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RGB;
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        int smaller = Math.min(getSize().width, getSize().height);
        cellSize = (int) (((double) smaller) * 0.9 / 8);
        originX = (getSize().width - 8 * cellSize) / 2;
        originY = (getSize().height - 8 * cellSize) / 2;
        turn = board.TurnColor == 16 ? "Black" : "White";
        g.drawString(turn, 10, 10);

        Graphics2D g2 = (Graphics2D) g;
        drawBoard(g2);
        drawPieces(g2);
        drawPossibleMove(g2);
        if (isDrag) {
            int hiddenSquare = fromRow * 8 + fromCol;
            if ((hiddenSquare + hiddenSquare / 8) % 2 == 0) drawSquare(g2, hiddenSquare / 8, hiddenSquare % 8, brown);
            else drawSquare(g2, hiddenSquare / 8, hiddenSquare % 8, littleWhite);
            drawImage(g2, dragX, dragY, dragImgName);
        }
    }

    private void drawPieces(Graphics2D g2) {
        for (int index = 0; index < 64; index++) {
            if (board.Square[index] != 0) {
                int row = index / 8;
                int col = index % 8;
                drawImage(g2, originX + col * cellSize + cellSize / 2 + 5, originY + row * cellSize + cellSize / 2 + 5, board.Square[index]);
            }
        }
    }

    private void drawImage(Graphics2D g2, int x, int y, int imgName) {
        Image img = keyNameValueImage.get(imgName);
        g2.drawImage(img, x - cellSize / 2, y - cellSize / 2, cellSize - 10, cellSize - 10, null);
    }

    private void drawBoard(Graphics2D g2) {
        for (int i = 0; i < 64; i++) {
            if ((i + i / 8) % 2 == 0) drawSquare(g2, i / 8, i % 8, brown);
            else drawSquare(g2, i / 8, i % 8, littleWhite);
        }
    }

    private void drawSquare(Graphics2D g2, int row, int column, Color color) {
        g2.setColor(color);
        g2.fillRect(originX + column * cellSize, originY + row * cellSize, cellSize, cellSize);
    }

    private void drawPossibleMove(Graphics2D g2) {
        if (board.moves.size() != 0 && turnOnShowMoves == true) {
            for (Move move : possibleMove) {
                int targetRow = move.TargetSquare / 8;
                int targetCol = move.TargetSquare % 8;
                //out.println(targetRow+"-"+targetCol);
                drawSquare(g2, targetRow, targetCol, highlight);
            }
        }
    }


    private void showPossibleMove(int row, int column) {
        int start = row * 8 + column;
        possibleMove = new ArrayList<Move>();
        for (Move move : board.moves) {
            if (move.StartSquare == start) {
                possibleMove.add(move);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragX = e.getX();
        dragY = e.getY();
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        fromCol = (e.getPoint().x - originX) / cellSize;
        fromRow = (e.getPoint().y - originY) / cellSize;
        showPossibleMove(fromRow, fromCol);
        dragX = e.getX();
        dragY = e.getY();
        dragImgName = board.Square[fromCol + fromRow * 8];
        isDrag = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = (e.getPoint().x - originX) / cellSize;
        int row = (e.getPoint().y - originY) / cellSize;
        if (fromCol != col || fromRow != row) {
            int startSquare = fromRow * 8 + fromCol;
            int targetSquare = row * 8 + col;
            Move move = board.getMove(startSquare, targetSquare);
            if(move!=null) {
                board.executeMove(move);
                turn = board.TurnColor == 16 ? "Black" : "White";
                playSound();
                String nuocdi = startSquare + "-" + targetSquare +"-" + move.Flag;
                this.ClientThread.sendToServer("/code " + nuocdi);
            }
        }
        possibleMove = new ArrayList<Move>();
        isDrag = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void ReCreate(String s) {
        this.board.CreateBoard(s);
        repaint();
    }

    public void playSound() {

        try {
            File moveSound = new File("resource\\move.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(moveSound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (turnOnSound == true) {
                clip.start();
            } else {
                clip.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

