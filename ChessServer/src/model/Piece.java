package model;

import java.util.stream.IntStream;

public class Piece {

    public static final int None = 0;
    public static final int King = 1;
    public static final int Pawn = 2;
    public static final int Knight = 3;
    public static final int Bishop = 4;
    public static final int Rook = 5;
    public static final int Queen = 6;

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
        return IntStream.of(slidingPieces).anyMatch(x -> x == pieceType);
    }
}
