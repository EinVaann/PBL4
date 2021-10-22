package backup;

import model.Board;
import model.ImageData;
import model.Move;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import static java.lang.System.*;

public class ChessView extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Image> keyNameValueImage = new HashMap<Integer, Image>();

//	private Map<Integer, Image> keyNameValueImage = new HashMap<Integer, Image>();

    private Board board;

    Color brown = new Color(212, 101, 4);
    Color littleWhite = new Color(245, 220, 198);
    Color highlight = new Color(252, 153, 111, 127);

    private double scaleFactor = 0.9;
    private int originX = -1;
    private int originY = -1;
    private int cellSize = -1;

    private int fromCol = -1;
    private int fromRow = -1;
    private ChessPiece movingPiece;
    private Point movingPiecePoint;

    private String turn;
    private List<Move> possibleMove = new ArrayList<Move>();
    public ChessView() {}

    public ChessView(Board board) {
        this.board = board;
        turn = board.TurnColor==16?"Black":"White";
        this.keyNameValueImage = ImageData.getInstance().keyNameValueImage;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        int smaller = Math.min(getSize().width, getSize().height);
        cellSize = (int) (((double)smaller) * scaleFactor / 8);
        originX = (getSize().width - 8 * cellSize) / 2;
        originY = (getSize().height - 8 * cellSize) / 2;

        g.drawString(turn,10,10);
        Graphics2D g2 = (Graphics2D)g;
        drawBoard(g2);
        drawPieces(g2);

        if(possibleMove.size()!=0){
            for(Move move: possibleMove){
                int targetRow = move.TargetSquare/8;
                int targetCol = move.TargetSquare%8;
                out.println(targetRow+"-"+targetCol);
                drawPossibleMove(g2,targetCol,targetRow);
            }
        }
    }

    private void drawPieces(Graphics2D g2) {
        /*for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                ChessPiece piece = chessDelegate.pieceAt(col, row);
                if(piece != null) {
                    drawImage(g2, col, row, piece.getImgName());
                }
            }
        }*/
        //Image image = ImageIO.read(new File("resource\\Bishop-white.png"));
        for(int index = 0;index<64;index++){
            if(board.Square[index]!=0) {
                int row = index / 8;
                int col = index % 8;
                drawImage(g2, col, row, board.Square[index]);
            }
        }
    }

    private void drawImage(Graphics2D g2, int col, int row, int imgName) {
        Image img = keyNameValueImage.get(imgName);
        //out.println(img.getSource());
        g2.drawImage(img, originX + col * cellSize + 5, originY + row * cellSize + 5, cellSize-10, cellSize-10, null);
    }



    private void drawBoard(Graphics2D g2) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                drawSquare(g2, 2*i, 2*j, true);
                drawSquare(g2, 1+2*i, 1+2*j, true);

                drawSquare(g2, 1+2*i, 2*j, false);
                drawSquare(g2, 2*i, 1+2*j, false);
            }
        }
    }

    private void drawSquare(Graphics2D g2, int row, int column, boolean light) {
        g2.setColor(light ? littleWhite : brown);
        g2.fillRect(originX + row * cellSize, originY + column * cellSize, cellSize, cellSize);
    }

    private void drawPossibleMove(Graphics2D g2,int row, int column){
        g2.setColor(highlight);
        g2.fillRect(originX + row * cellSize, originY + column * cellSize, cellSize, cellSize);

    }

    private void showPossibleMove(int row, int column){
        int start = row*8+column;
        possibleMove = new ArrayList<Move>();
        for(Move move: board.generateMove.moves){
            if(move.StartSquare==start){
                possibleMove.add(move);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //movingPiecePoint = e.getPoint();
        //repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        fromCol = (e.getPoint().x - originX) / cellSize;
        fromRow = (e.getPoint().y - originY) / cellSize;
        showPossibleMove(fromRow,fromCol);
        repaint();
       // movingPiece = chessDelegate.pieceAt(fromCol, fromRow);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = (e.getPoint().x - originX) / cellSize;
        int row = (e.getPoint().y - originY) / cellSize;
        int start = fromRow*8+fromCol;
        int target = row*8+col;
        out.println(start+"->"+target);
        out.println(board.generateMove.validMove(start,target));
        if (fromCol != col || fromRow != row) {
            //chessDelegate.movePiece(fromCol, fromRow, col, row);
            if(board.generateMove.validMove(start,target)) {
                board.MovePiece(fromRow * 8 + fromCol, row * 8 + col);
                turn = board.TurnColor==16?"Black":"White";
            }
        }
        possibleMove = new ArrayList<Move>();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
