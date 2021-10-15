package model;

import java.util.*;

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
    public GenerateMove generateMove;
    public static int TurnColor;
    public static int[] Square;
    public Board(){
        Square = new int[64];
        generateMove = null;
    }

    public void CreateBoard(String fen){
       //create Board from a fenCode
        String board = fen.split(" ")[0];
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
                    Square[rank*8+file] = pieceColor | pieceType;
                    file++;
                }
            }
        }
        //System.out.println(Square[0]);
        //set turn from fenCode
        String turn = fen.split(" ")[1];
        TurnColor = (turn.equals("w"))?8:16;
        generateMove = new GenerateMove(Square);
    }

    public void ShowBoard(){
        for(int i=0;i<Square.length;i++){
            if(i%8==0) System.out.println();
            System.out.print(Square[i]+"\t");

        }
    }

    public void MovePiece(int startSquare,int targetSquare){
        Square[targetSquare] = Square[startSquare];
        Square[startSquare] = 0;
        generateMove = new GenerateMove(Square);
    }
}


