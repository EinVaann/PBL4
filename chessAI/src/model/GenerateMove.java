package model;

import java.util.ArrayList;
import java.util.List;

public class GenerateMove {
    public List<Move> moves;
    //Get number of possible moves from every Square
    private int[] board = new int[64];
    public GenerateMove(int[] board){
        //System.out.println("creating");
        this.board = board;
        moves = GenerateMove();
    }

    public List<Move> GenerateMove(){
        moves = new ArrayList<>();
        for(int startSquare = 0; startSquare < 64 ; startSquare++){

            int piece = board[startSquare];
            if(Piece.isColor(piece,Board.TurnColor)){
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
        //System.out.print("knight");
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;
       // System.out.println(Calculate.KnightMove[startSquare].length);
        for(int moveIndex=0; moveIndex < Calculate.KnightMove[startSquare].length;moveIndex++){
            int targetSquare =  Calculate.KnightMove[startSquare][moveIndex];
           // System.out.println(targetSquare);
            int pieceOnTargetSquare = board[targetSquare];

            //check if block by allie pieces
            if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                break; // not look in this direction anymore
            }

            moves.add(new Move(startSquare,targetSquare));
            //System.out.println(startSquare+"->"+targetSquare);
            //check if block by opponent pieces
            if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                break; // not look in this direction anymore
            }
        }
    }

    private void GenerateSingleMove(int startSquare, int piece) {
        int CurrentColor = Piece.getColor(piece);
        int OpponentColor = CurrentColor==8?16:8;
        int[] possibleMove = new int[0];
        if(Piece.isType(piece,Piece.King)){
            possibleMove = new int[]{1,7,8,9,-1,-7,-8,-9};
        }
        if(Piece.isType(piece,Piece.Pawn)){
            //if it black move let it goes down, and another way around
            int move = Piece.isColor(piece,Piece.Black)?8:-8;
            //check if pawn has move or not. if not it can move 2 squares
            if((startSquare>=8 && startSquare <= 15)||(startSquare>=48 && startSquare <= 55)){
                possibleMove = new int[]{move,move*2};
            }else possibleMove  = new int[]{move};

        }
        for(int possible: possibleMove){
            int targetSquare = startSquare + possible;
            int pieceOnTargetSquare = board[targetSquare];
            //check if block by allie pieces
            if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                break; // not look in this direction anymore
            }

            //check if block by opponent pieces
            if(!Piece.isType(piece,Piece.Pawn)) {
                //if block and it's not pawn, add to move list as a capture move
                moves.add(new Move(startSquare,targetSquare));
                if (Piece.isColor(pieceOnTargetSquare, OpponentColor)) {
                    break; // not look in this direction anymore
                }
            }
            //Check pawn condition of capturing opponent pieces
            else{
                if (Piece.isColor(pieceOnTargetSquare, OpponentColor)) {
                    break; // not look in this direction anymore
                }
                moves.add(new Move(startSquare,targetSquare));
                int[] blackPawnCap = new int[]{7,9};
                int[] whitePawnCap = new int[]{-7,-9};
                int[] captureMove = (Piece.isColor(piece,Piece.Black))?blackPawnCap:whitePawnCap;
                for(int capture: captureMove){
                    int captureSquare = startSquare + capture;
                    if(captureSquare<0 || capture>63) break;
                    int pieceOnCaptureSquare = board[captureSquare];
                    if (Piece.isColor(pieceOnCaptureSquare, OpponentColor)) {
                        break; // not look in this direction anymore
                    }
                }
            }
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

    public boolean validMove(int startSquare, int TargetSquare){
        for(Move m: moves){
            if(m.StartSquare==startSquare && m.TargetSquare==TargetSquare){
                return true;
            }
        }
        return false;
    }


}
