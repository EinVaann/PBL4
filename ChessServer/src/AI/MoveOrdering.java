package AI;

import model.Board;
import model.GenerateMove;
import model.Move;
import model.Piece;

import java.util.List;

public class MoveOrdering {
    int[] moveScore = new int[218];
    int badMovePenalty = 400;
    int captureMultiplier = 10;

    GenerateMove generateMove;

    public MoveOrdering(GenerateMove generateMove) {
        this.generateMove = generateMove;
    }

    public void OrderMove(List<Move> moves,Board board){
        for(int i=0;i<moves.size();i++){
            int score = 0;
            int movePieceType = Piece.getType(board.Square[moves.get(i).StartSquare]);
            int capturePieceType = Piece.getType(board.Square[moves.get(i).TargetSquare]);
            int flag = moves.get(i).Flag;
            if (capturePieceType != Piece.None) {
                score = captureMultiplier * GetPieceValue (capturePieceType) - GetPieceValue (movePieceType);
            }
            if (movePieceType == Piece.Pawn && flag == 5) {
                score += Value.queenValue*2;
            }
            else{
               if( ((generateMove.PawnAttackMap >> moves.get(i).TargetSquare) & 1) == 0 &&
                       Piece.isType(board.Square[moves.get(i).StartSquare],Piece.King)){
                   score -= badMovePenalty;
               }
            }
            moveScore[i] = score;
        }
        Sort(moves);
    }

    static int GetPieceValue(int pieceType) {
        return switch (pieceType) {
            case Piece.Queen -> Value.queenValue;
            case Piece.Rook -> Value.rookValue;
            case Piece.Knight -> Value.knightValue;
            case Piece.Bishop -> Value.bishopValue;
            case Piece.Pawn -> Value.pawnValue;
            default -> 0;
        };
    }

    void Sort (List<Move> moves) {
        for (int i = 0; i < moves.size() - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                int swapIndex = j - 1;
                if (moveScore[swapIndex] < moveScore[j]) {
                    //swap move
                    Move temp;
                    temp = moves.get(j);
                    moves.set(j,moves.get(swapIndex));
                    moves.set(swapIndex,temp);
                    //swap score
                    int tempo;
                    tempo = moveScore[j];
                    moveScore[j] = moveScore[swapIndex];
                    moveScore[swapIndex] = tempo;
                }
            }
        }
    }
}
