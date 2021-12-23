package AI;

import model.Board;
import model.GenerateMove;
import model.Move;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Search {
    public GenerateMove generateMove;
    public Board board;
    public Value value;
    public Move bestPossibleMove;

    public Search(GenerateMove generateMove, Board board) {
        this.generateMove = generateMove;
       // this.board = new Board(board.Square, board.TurnColor, board.kingSquare, board.preMove, board.gameState, board.stateHistory);
        this.board = board;
        value = new Value();
    }

    public int SearchMove(int depth, int alpha, int beta){

        if(depth == 0){
            return value.getValue(board);
        }
        generateMove = new GenerateMove(board);
        List<Move> moves = generateMove.moves;
        if(moves.size()==0){
//            System.out.println("check"+generateMove.isInCheck());
//            if(generateMove.isInCheck())
//            {
                return -99999;
//            }
//            else {
//                return 0;
//            }
        }
        MoveOrdering moveOrdering = new MoveOrdering(generateMove);
        moveOrdering.OrderMove(moves,board);
        Move bestMove = moves.get(0);
        for(Move move:moves){
            board.executeMove(move);
            int evaluation = -SearchMove(depth-1,-beta,-alpha);

            board.UndoMove(move);

            if(evaluation >= beta){
                return beta;
            }

            if(evaluation>alpha){
                bestMove = move;
                alpha = evaluation;
            }
        }
        bestPossibleMove = bestMove;
        return alpha;
    }
}
