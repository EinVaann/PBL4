package model;

import java.util.*;
import java.util.List;

public class Board {
    Map<Character,Integer> pieceTypeSymbol = new HashMap<>() {
        {
            put('k',Piece.King);
            put('p',Piece.Pawn);
            put('n',Piece.Knight);
            put('b',Piece.Bishop);
            put('r',Piece.Rook);
            put('q',Piece.Queen);
        }
    };

    private static final int WhiteIndex = 0;
    private static final int BlackIndex = 1;

    public int[] Square;
    public int TurnColor;


    public int[] kingSquare;
    public List<Move> moves;
    public Board(){
        Square = new int[64];
    }

    public void CreateBoard(String fen){
        //create Board from a fenCode
        String board = fen.split(" ")[0];
        LoadPosition(board);
        //System.out.println(Square[0]);
        //set turn from fenCode
        String turn = fen.split(" ")[1];
        TurnColor = (turn.equals("w"))?8:16;
        moves = new ArrayList<>();
    }

    public void LoadPosition(String board){
        Square = new int[64];
        kingSquare = new int[2];
        int file=0,rank=0;

        for(char symbol: board.toCharArray()){
            if(symbol=='/'){
                file = 0;
                rank++;
            }else{
                if(Character.isDigit(symbol)){
                    file+= Character.getNumericValue(symbol);
                }else{
                    int pieceColor = (Character.isUpperCase(symbol))? Piece.White:Piece.Black;
                    int pieceType = pieceTypeSymbol.get(Character.toLowerCase(symbol));
                    //.out.println(rank*8+file+"-"+symbol+"-"+pieceColor);
                    int SquareIndex = rank*8+file;
                    Square[SquareIndex] = pieceColor | pieceType;
                    if(pieceType!=Piece.None){
                        int pieceColorIndex = pieceColor==Piece.White?WhiteIndex:BlackIndex;
                        if(pieceType==Piece.King){
                            kingSquare[pieceColorIndex] = SquareIndex;
                        }
                    }
                    file++;
                }
            }
        }
    }
    public void ShowBoard(){
        for(int i=0;i<Square.length;i++){
            if(i%8==0) System.out.println();
            System.out.print(Square[i]+"\t");

        }
    }

    public void executeMove(Move move){
        int colorIndex = TurnColor/8-1;

        int startSquare = move.StartSquare;
        int targetSquare = move.TargetSquare;
        int flag = move.Flag;

        int capturePiece = Square[targetSquare];
        int capturePieceType = Piece.getType(Square[targetSquare]);
        int movePiece = Square[startSquare];
        int movePieceType = Piece.getType(movePiece);

        if (movePieceType == Piece.King) {
            kingSquare[colorIndex] = targetSquare;
        }

        Square[targetSquare] = Square[startSquare];
        Square[startSquare] = 0;
        if(flag!=0){
            if(flag==1){
                int rookCastling = targetSquare+1;
                int rookSquareAfter = startSquare+1;
                Square[rookSquareAfter] = Square[rookCastling];
                Square[rookCastling] = 0;
            }
            if(flag==2){
                int rookCastling = targetSquare-2;
                int rookSquareAfter = startSquare-1;
                Square[rookSquareAfter] = Square[rookCastling];
                Square[rookCastling] = 0;
            }
            if(flag==4){
                int enpCaptureSquare;
                if(TurnColor==Piece.White){
                    enpCaptureSquare = targetSquare+8;
                }else  enpCaptureSquare = targetSquare-8;
                capturePiece = Square[enpCaptureSquare];
                Square[enpCaptureSquare] = Piece.None;
            }
            if(flag==5){
                Square[targetSquare] = Piece.Queen + TurnColor;
            }
        }
        TurnColor = TurnColor==16?8:16;
        moves = new ArrayList<>();
    }

    public boolean validMove(int startSquare, int targetSquare){
        for(Move move:moves){
            if(move.StartSquare==startSquare && move.TargetSquare==targetSquare){
                return true;
            }
        }
        return false;
    }

    public Move getMove(int startSquare, int targetSquare) {
        for(Move move:moves){
            if(move.StartSquare==startSquare && move.TargetSquare==targetSquare){
                return move;
            }
        }
        return null;
    }
}


