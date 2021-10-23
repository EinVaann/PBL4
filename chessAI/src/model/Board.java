package model;

import java.util.*;
import java.util.stream.Collectors;

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
    Map<Integer,Character> pieceTypeSymbolB = new HashMap<>(){
        {
            put(Piece.King,'k');
            put(Piece.Pawn,'p');
            put(Piece.Knight,'n');
            put(Piece.Bishop,'b');
            put(Piece.Rook,'r');
            put(Piece.Queen,'q');
        }
    };
    public GenerateMove generateMove;
    public int TurnColor;
    public int[] Square;
    public int EnPassant;
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
        generateMove = new GenerateMove(Square,TurnColor);

        String enPassant = fen.split(" ")[4];
        if(!enPassant.equals("-")){
            
        }
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
        TurnColor = TurnColor==16?8:16;
        generateMove = new GenerateMove(Square,TurnColor);
    }

    public String getFenCode(){
        String fenCode = "";
        int emptyCount = 0;
        int endrow = 0;
        for(int piece: this.Square){
            if (endrow==8){
                if(emptyCount!=0){
                    fenCode = fenCode+emptyCount;
                }
                fenCode=fenCode+"/";
                endrow = 0;
                emptyCount = 0;
            }
            int color = piece/8*8;
            int type = piece%8;
            if(type!=0) {
                if(emptyCount!=0){
                    fenCode = fenCode+emptyCount;
                }
                emptyCount = 0;
                char chessSymbol = pieceTypeSymbolB.get(type);
                if (color == Piece.Black) chessSymbol = Character.toUpperCase(chessSymbol);
                fenCode = fenCode + chessSymbol;
            }
            else emptyCount++;
            endrow++;
        }

        String turn = TurnColor==Piece.Black?"b":"w";
        fenCode = fenCode+" "+turn;
        return fenCode;
    }
}


