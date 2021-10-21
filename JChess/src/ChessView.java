import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import static java.lang.System.*;

public class ChessView extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;

    private Map<String, Image> keyNameValueImage = new HashMap<String, Image>();

//	private Map<Integer, Image> keyNameValueImage = new HashMap<Integer, Image>();

    private ChessDelegate chessDelegate;

    Color brown = new Color(212, 101, 4);
    Color littleWhite = new Color(245, 220, 198);

    private double scaleFactor = 0.9;
    private int originX = -1;
    private int originY = -1;
    private int cellSize = -1;

    private int fromCol = -1;
    private int fromRow = -1;
    private ChessPiece movingPiece;
    private Point movingPiecePoint;

    public ChessView() {}

    public ChessView(ChessDelegate chessDelegate) {
        this.chessDelegate = chessDelegate;

        String[] imageNames = {
                ChessConstants.bBishop,
                ChessConstants.wBishop,
                ChessConstants.bKing,
                ChessConstants.wKing,
                ChessConstants.bKnight,
                ChessConstants.wKnight,
                ChessConstants.bPawn,
                ChessConstants.wPawn,
                ChessConstants.bQueen,
                ChessConstants.wQueen,
                ChessConstants.bRook,
                ChessConstants.wRook,
        };
        try {
            for (String imageName : imageNames) {
                Image img = loadImage(imageName + ".png");
                keyNameValueImage.put(imageName, img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Graphics2D g2 = (Graphics2D)g;
        drawBoard(g2);
        drawPieces(g2);
    }

    private void drawPieces(Graphics2D g2) {
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                ChessPiece piece = chessDelegate.pieceAt(col, row);
                if(piece != null) {
                    drawImage(g2, col, row, piece.getImgName());
                }
            }
        }
    }

    private void drawImage(Graphics2D g2, int col, int row, String imgName) {
        Image img = keyNameValueImage.get(imgName);
        g2.drawImage(img, originX + col * cellSize, originY + (7 - row) * cellSize, cellSize, cellSize, null);
    }

    private Image loadImage(String imgFileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resURL = classLoader.getResource(imgFileName);
        if (resURL == null) {
            return null;
        } else {
            File imgFile = new File(resURL.toURI());
            return ImageIO.read(imgFile);
        }
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


    @Override
    public void mouseDragged(MouseEvent e) {
        movingPiecePoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        fromCol = (e.getPoint().x - originX) / cellSize;
        fromRow = 7 - (e.getPoint().y - originY) / cellSize;
        movingPiece = chessDelegate.pieceAt(fromCol, fromRow);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = (e.getPoint().x - originX) / cellSize;
        int row = 7 - (e.getPoint().y - originY) / cellSize;

        if (fromCol != col || fromRow != row) {
            chessDelegate.movePiece(fromCol, fromRow, col, row);
        }

        movingPiece = null;
        movingPiecePoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
