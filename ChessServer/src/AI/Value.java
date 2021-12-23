package AI;

import model.Board;
import model.Piece;

public class Value {
    public Board board;
    public static int pawnValue = 100;
    public static int knightValue = 300;
    public static int bishopValue = 320;
    public static int rookValue = 500;
    public static int queenValue = 900;


    public int getValue(Board board) {
        this.board = board;
        int whiteVal = 0;
        int blackVal = 0;

        int whiteMatO = CountMaterialOther(Piece.White);
        int blackMatO = CountMaterialOther(Piece.Black);
        int whiteMatP = CountMaterialPawn(Piece.White);
        int blackMatP = CountMaterialPawn(Piece.Black);

        int whiteMat = whiteMatO + whiteMatP;
        int blackMat = blackMatO + blackMatP;

        float whiteEndgameWeight = EndGameWeight(whiteMatO);
        float blackEndgameWeight = EndGameWeight(blackMatO);

        whiteVal += whiteMat;
        blackVal += blackMat;
        whiteVal += EndGameValue(Piece.White,Piece.Black,board.kingSquare[0],board.kingSquare[1],whiteEndgameWeight);
        blackVal += EndGameValue(Piece.Black,Piece.White,board.kingSquare[1],board.kingSquare[0],blackEndgameWeight);

        int eval = whiteVal-blackVal;
        int turnValue = (board.TurnColor==Piece.White)?1:-1;
        return eval * turnValue;
    }

    private int EndGameValue(int myColor, int opColor, int myKing, int opKing, float EndgameWeight) {
        int evaluation = 0;
        int opKingRank = opKing/8;
        int opKingFile = opKing%8;

        int opKingDstFromCenterFile = Math.max(3-opKingFile,opKingFile-4);
        int opKingDstFromCenterRank = Math.max(3-opKingRank,opKingRank-4);
        evaluation += (opKingDstFromCenterFile+opKingDstFromCenterRank)*10*EndgameWeight;

        int myKingRank = myKing/8;
        int myKingFile = myKing&8;

        int dstBetweenKing = Math.abs(myKingFile-opKingFile)+Math.abs(myKingRank-opKingRank);
        evaluation += (14-dstBetweenKing)*50*EndgameWeight/2;

        return (int)(evaluation);
    }

    private float EndGameWeight(int MatO) {
        return 1 - Math.min(1,(MatO/1620));
    }

    int CountMaterialOther (int colorPiece) {
        int material = 0;
        for(int i=0;i<64;i++){
            if(Piece.isColor(board.Square[i],colorPiece)){
                int pieceType = Piece.getType(board.Square[i]);
                switch (pieceType){
                    case Piece.Rook -> material+=rookValue;
                    case Piece.Knight -> material+=knightValue;
                    case Piece.Bishop -> material+=bishopValue;
                    case Piece.Queen -> material+=queenValue;
                }
            }
        }
        return material;
    }

    int CountMaterialPawn(int colorPiece){
        int material = 0;
        for(int i=0;i<64;i++){
            if(Piece.isColor(board.Square[i],colorPiece)){
                int pieceType = Piece.getType(board.Square[i]);
                if(pieceType==Piece.Pawn){
                    material += pawnValue;
                }
            }
        }
        return material;
    }
}
