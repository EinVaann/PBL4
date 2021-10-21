package backup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.JFrame;

public class ChessController implements ChessDelegate, ActionListener {

    private ChessModel chessModel = new ChessModel();
    private ChessView panel;
    private PrintWriter printWriter;

    ChessController() {
        chessModel.reset();

        JFrame frame = new JFrame("Chess");
        frame.setSize(600, 600);
        frame.setResizable(false);
        panel = new ChessView(this);

        frame.add(panel);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new ChessController();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
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
    }
}
