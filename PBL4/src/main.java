import model.Board;
import model.Calculate;
import view.ChessView;

import java.io.IOException;

public class main {


    public static void main(String[] args) throws IOException {
        Board board = new Board();
        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Calculate c = new Calculate();
        board.CreateBoard(fenCode);
        board.ShowBoard();
        ChessView chessView = new ChessView(board);
    }

}
