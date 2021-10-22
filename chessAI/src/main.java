import backup.ChessController;
import model.Board;
import model.Calculate;
import model.GenerateMove;
import model.Move;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        Board board = new Board();
        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";
        Calculate c = new Calculate();
        board.CreateBoard(fenCode);
        board.ShowBoard();
        ChessController chessController = new ChessController(board);
        /*
        System.out.println(board.getFenCode());
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.print("\nStart Square: ");
            int start = input.nextInt();
            if(start<0 || start>63) break;
            for (Move move : board.generateMove.moves) {
                if(start==move.StartSquare)
                    System.out.println(move.StartSquare + "->" + move.TargetSquare);
            }
            System.out.print("Target Square: ");
            int target = input.nextInt();
            if(start<0 || start>63) break;
            if(board.generateMove.validMove(start,target)){
                board.MovePiece(start,target);
                board.ShowBoard();

            }else System.out.println("invalid move");
        }*/

        /*if (startSquare >= 0 && startSquare < 64) {
            List<Move> possibleMove = board.generateMove.GetPossibleMove(startSquare);
            for (Move move : possibleMove) {
                System.out.println(move.StartSquare + "->" + move.TargetSquare);
            }
        }*/


        /*for(int i=0;i<64;i++){
            for(int j=0;j<8;j++){
                System.out.print(Calculate.NumberSquareToEdge[i][j]);
            }
            System.out.println();
        }*/
       // System.out.println(PreCaculateMove.NumberSquareToEdge[1].length);
    }
}
