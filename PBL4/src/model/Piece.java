package model;

import java.util.stream.IntStream;

public class Piece {

    public static int None = 0;
    public static int King = 1;
    public static int Pawn = 2;
    public static int Knight = 3;
    public static int Bishop = 4;
    public static int Rook = 5;
    public static int Queen = 6;

    public static int White = 8;
    public static int Black = 16;
    public static int getColor(int piece){
        return piece/8*8;
    }
    public static boolean isColor(int piece, int turn){
        return getColor(piece)==turn;
    }
    public static int getType(int piece){
        return piece%8;
    }
    public static boolean isType(int piece,int pieceType){
        return getType(piece)==pieceType;
    }
    public static boolean isSlidingPiece(int piece){
        int[] slidingPieces = new int[]{Bishop,Rook,Queen};
        int pieceType = piece%8;
        boolean isTrue = IntStream.of(slidingPieces).anyMatch(x -> x == pieceType);
        return isTrue;
    }
}
