package model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Calculate {
    //calculate number of squares from square to edge
    public static int[] DirectionOffset = {8, -8, -1, 1, -9, -7, 7, 9};
    public static int[][] NumberSquareToEdge = new int[64][8];
    public static int[][] KnightMove = new int[64][];
    public static int[][] KingMove = new int[64][];

    public Calculate() {
        for(int squareIndex = 0;squareIndex<64;squareIndex++){
            int y = squareIndex/8;
            int x = squareIndex%8;
            //calculate sliding moves
            int numSouth = 7 - y;
            int numNorth = y;
            int numWest = x;
            int numEast = 7 - x;
            NumberSquareToEdge[squareIndex]=new int[]{
                    numSouth,numNorth,numWest,numEast,
                    Math.min(numNorth,numWest),Math.min(numNorth,numEast),
                    Math.min(numSouth,numWest),Math.min(numSouth,numEast)
            };

            //calculate Knight's moves
            int[] allKnightJumps = { 15, 17, -17, -15, 10, -6, 6, -10 };
            List<Integer> legalKnightJumps = new ArrayList<Integer>();
            for(int jump: allKnightJumps){
                int targetSquare = squareIndex + jump;
                if(targetSquare<0 || targetSquare>63) continue;
                int delta_x = targetSquare%8 - x;
                int delta_y = targetSquare/8 - y;
                //System.out.println(delta_x+"-"+ delta_y);
                if(Math.max(Math.abs(delta_x),Math.abs(delta_y))==2){
                    legalKnightJumps.add(targetSquare);
                    //System.out.println(squareIndex+"->"+ targetSquare);
                }
            }
            KnightMove[squareIndex] =legalKnightJumps.stream().mapToInt(i->i).toArray();

            //Genenate legal king move
            List<Integer> legalKingMoves = new ArrayList<Integer>();
            for(int direction: DirectionOffset){
                int targetSquare = squareIndex + direction;
                if(targetSquare<0 || targetSquare>63) continue;
                int delta_x = targetSquare%8 - x;
                int delta_y = targetSquare/8 - y;
                //System.out.println(delta_x+"-"+ delta_y);
                if(Math.max(Math.abs(delta_x),Math.abs(delta_y))==1){
                    legalKingMoves.add(targetSquare);
                    //System.out.println(squareIndex+"->"+ targetSquare);
                }
            }
            KingMove[squareIndex] =legalKingMoves.stream().mapToInt(i->i).toArray();
        }
    }

}
