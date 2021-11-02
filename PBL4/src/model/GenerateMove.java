package model;

import java.util.ArrayList;
import java.util.List;

public class GenerateMove {
    public List<Move> moves;
    //Get number of possible moves from every Square
    private int[] board = new int[64];
    private int turnColor;
    public GenerateMove(int[] board, int turnColor){
        //System.out.println("creating");
        this.board = board;
        this.turnColor = turnColor;
        moves = GenerateMove();
    }

    public List<Move> GenerateMove(){
        moves = new ArrayList<>();
        for(int startSquare = 0; startSquare < 64 ; startSquare++){

            int piece = board[startSquare];
            if(Piece.isColor(piece,turnColor)){
                if(Piece.isSlidingPiece(piece)){
                    GenerateSlidingMove(startSquare,piece);
                }
                if(Piece.isType(piece,Piece.Knight)){
                    GenerateKnightMove(startSquare,piece);
                }
                if(Piece.isType(piece,Piece.King)){
                    GenerateKingMove(startSquare,piece);
                }
                if(Piece.isType(piece,Piece.Pawn)){
                    GeneratePawnMove(startSquare,piece);
                }
            }
        }
        return moves;
    }

    private void GeneratePawnMove(int startSquare, int piece) {
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;

        int pawnOffset = CurrentColor == Piece.Black?8:-8;
        //pawn move
        int targetSquare = startSquare + pawnOffset;
        int pieceOnTargetSquare = board[targetSquare];
        if(pieceOnTargetSquare==0){//There aren't any piece piece on target square
            moves.add(new Move(startSquare,targetSquare));

            //pawn double move at the start
            //only check double move if single move is possible
            int pawnStartRank = CurrentColor == Piece.Black?1:6;
            if(startSquare/8==pawnStartRank){
                //check if square is free
                int doubleMoveSquare = startSquare + 2*pawnOffset;
                int pieceOnSquare = board[doubleMoveSquare];
                if(pieceOnSquare==0){//There aren't any pieces on target square
                    moves.add(new Move(startSquare,doubleMoveSquare));
                }
            }
        }
        //pawn capture
        int[] pawnCaptureOffset;
        if(CurrentColor == Piece.Black){
            pawnCaptureOffset = new int[]{7,9};
        }else pawnCaptureOffset = new int[]{-7,-9};
        for(int offsetIndex: pawnCaptureOffset){
            int captureSquare = startSquare + offsetIndex;
            //check if it is a diagonal move
            int dx = Math.abs(captureSquare%8 - startSquare%8);
            int dy = Math.abs(captureSquare/8 - startSquare/8);
            if(dx!=1 || dy!=1) continue;
            //check if there is an opponent pieces on capture square
            int pieceOnCaptureSquare = board[captureSquare];
            if ( Piece.isColor(pieceOnCaptureSquare,OpponentColor)){
                moves.add(new Move(startSquare,captureSquare));
            }
        }
    }

    private void GenerateKingMove(int startSquare, int piece) {
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;
        for(int moveIndex=0; moveIndex < Calculate.KingMove[startSquare].length;moveIndex++) {
            //System.out.println(startSquare+"->"+Calculate.KnightMove[startSquare][moveIndex]);
            int targetSquare =  Calculate.KingMove[startSquare][moveIndex];

            int pieceOnTargetSquare = board[targetSquare];

            //check if block by allie pieces
            if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                continue; // not look in this direction anymore
            }

            moves.add(new Move(startSquare,targetSquare));

            //check if block by opponent pieces
            if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                continue; // not look in this direction anymore
            }
        }
    }

    private void GenerateKnightMove(int startSquare, int piece) {
        //System.out.println("Knight--");
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;

       // System.out.println(Calculate.KnightMove[startSquare].length);
        for(int moveIndex=0; moveIndex < Calculate.KnightMove[startSquare].length;moveIndex++){

            int targetSquare =  Calculate.KnightMove[startSquare][moveIndex];
            int pieceOnTargetSquare = board[targetSquare];

            //check if block by allie pieces
            if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                continue; // not look in this direction anymore
            }

            moves.add(new Move(startSquare,targetSquare));

            //System.out.println(startSquare+"->"+targetSquare);
            //check if block by opponent pieces
            /*if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                break; // not look in this direction anymore
            }*/
        }
    }

    //generate moves for sliding pieces a.k.a rook, bishop, queen
    private void GenerateSlidingMove(int startSquare, int piece) {
        //System.out.print("other");
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;

        //set option direction for different piece
        int startIndex= Piece.isType(piece,Piece.Bishop)?4:0;
        int endIndex = Piece.isType(piece,Piece.Rook)?4:8;
        //Check 8 direction
        for(int directionIndex = startIndex;directionIndex<endIndex;directionIndex++){
            //count number of square to edge from that direction
            for(int n = 0; n< Calculate.NumberSquareToEdge[startSquare][directionIndex]; n++){
                //get a targetSquare n square away from startSquare in directionIndex direction
                int targetSquare = startSquare+ Calculate.DirectionOffset[directionIndex]*(n+1);
                int pieceOnTargetSquare = board[targetSquare];

                //check if block by allie pieces
                if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                    break; // not look in this direction anymore
                }

                moves.add(new Move(startSquare,targetSquare));

                //check if block by opponent pieces
                if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                    break; // not look in this direction anymore
                }
                //System.out.println("next");


            }
        }
    }

    public List<Move> GetPossibleMove(int startSquare){
        List<Move> possibleMoves = new ArrayList<Move>();
        for(Move move:moves){
            if(move.StartSquare==startSquare){
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    public boolean validMove(int startSquare, int targetSquare){
        for(Move m: moves){
            if(m.StartSquare==startSquare && m.TargetSquare==targetSquare){
                return true;
            }
        }
        return false;
    }


}
