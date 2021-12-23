package model;

import java.util.ArrayList;
import java.util.List;

public class GenerateMove {
    public List<Move> moves;
    //Get number of possible moves from every Square
    private int[] Square = new int[64];
    private final int CurrentColor;
    private final int OpponentColor;
    public long AttackMap;
    public long SlidingAttackMap;
    public long KnightAttackMap;
    public long PawnAttackMap;
    private boolean pinExist;
    private long pinRayBitMask;
    private long checkRayBitMask;
    private boolean inCheck;
    private boolean inDoubleCheck;
    private final int friendlyKingSquare;
    private final int opponentKingSquare;
    private Board board;

    public GenerateMove(Board board){
        //System.out.println("creating");
        this.board = board;
        this.Square = board.Square;
        this.CurrentColor = board.TurnColor;
        this.OpponentColor = CurrentColor==8?16:8;
        int[] kingPiece = board.kingSquare;
        pinRayBitMask = 0;
        checkRayBitMask = 0;
        pinExist = false;
        inCheck = false;
        inDoubleCheck = false;
        int pieceColorIndex = CurrentColor==Piece.White?0:1;
        friendlyKingSquare = kingPiece[pieceColorIndex];
        opponentKingSquare = kingPiece[1-pieceColorIndex];
        moves = GeneratePossibleMove();
    }

    public List<Move> GeneratePossibleMove(){
        moves = new ArrayList<>();
        CalculateAttackData ();

        for(int startSquare = 0; startSquare < 64 ; startSquare++){
            int piece = Square[startSquare];
            if (Piece.isColor(piece, CurrentColor)) {
                if (Piece.isType(piece, Piece.King)) {
                    GenerateKingMove(startSquare, piece);
                }
                //if in double check only king can move
                if (inDoubleCheck) {
                    return moves;
                }
                if (Piece.isSlidingPiece(piece)) {
                    GenerateSlidingMove(startSquare, piece);
                }
                if (Piece.isType(piece, Piece.Knight)) {
                    GenerateKnightMove(startSquare, piece);
                }
                if (Piece.isType(piece, Piece.Pawn)) {
                    GeneratePawnMove(startSquare, piece);
                }
            }
        }
        return moves;
    }

    private void GeneratePawnMove(int startSquare, int piece) {
        int pawnOffset = CurrentColor == Piece.Black?8:-8;
        //pawn move
        int targetSquare = startSquare + pawnOffset;
        int pieceOnTargetSquare = Square[targetSquare];
        if(pieceOnTargetSquare==0){//There aren't any piece piece on target square
            //check if promote
            if (!IsPinned(startSquare) || IsMovingAlongRay(pawnOffset, startSquare, friendlyKingSquare)) {
                    int CurrentColorPromoteRank = CurrentColor == Piece.Black ? 7 : 0;
                    int promoteFlag = targetSquare / 8 == CurrentColorPromoteRank ? 5 : 0;
                    if(!inCheck || SquareIsInCheckRay(targetSquare)) {
                        moves.add(new Move(startSquare, targetSquare, promoteFlag));
                    }
                    //pawn double move at the start
                    //only check double move if single move is possible
                    int pawnStartRank = CurrentColor == Piece.Black ? 1 : 6;
                    if (startSquare / 8 == pawnStartRank) {
                        //check if square is free
                        int doubleMoveSquare = startSquare + 2 * pawnOffset;
                        int pieceOnSquare = Square[doubleMoveSquare];
                        if (pieceOnSquare == 0) {//There aren't any pieces on target square
                            if(!inCheck || SquareIsInCheckRay(doubleMoveSquare)) {
                                moves.add(new Move(startSquare, doubleMoveSquare, 3));
                            }
                        }
                }
            }
        }
        //pawn capture
        int[] pawnCaptureOffset;
        if(CurrentColor == Piece.Black){
            pawnCaptureOffset = new int[]{7,9};
        }else pawnCaptureOffset = new int[]{-7,-9};
        for(int offsetIndex: pawnCaptureOffset){
            if (!IsPinned(startSquare) || IsMovingAlongRay(offsetIndex, startSquare, friendlyKingSquare)) {
                int captureSquare = startSquare + offsetIndex;
                if (captureSquare < 0 || captureSquare > 63) continue;
                //check if it is a diagonal move
                int dx = Math.abs(captureSquare % 8 - startSquare % 8);
                int dy = Math.abs(captureSquare / 8 - startSquare / 8);
                if (dx != 1 || dy != 1) continue;
                //check if there is an opponent pieces on capture square, or it is en passant
                int pieceOnCaptureSquare = Square[captureSquare];
                if (Piece.isColor(pieceOnCaptureSquare, OpponentColor)) {
                    int CurrentColorPromoteRank = CurrentColor == Piece.Black ? 7 : 0;
                    int promoteFlag = targetSquare / 8 == CurrentColorPromoteRank ? 5 : 0;
                    moves.add(new Move(startSquare, captureSquare, promoteFlag));
                }
                if (captureSquare == ((board.gameState >> 4) & 63)) {
                    moves.add(new Move(startSquare, captureSquare, 4));
                }
            }
        }
    }

    private void GenerateKingMove(int startSquare, int piece) {
        //get castle right on either side
        int CastleRight = board.gameState & 15;
        int sideCastleRight =(CurrentColor==Piece.White? CastleRight&3:(CastleRight>>2)&3);

        boolean hasKingCastleRight = (sideCastleRight&1) == 1;
        boolean hasQueenCastleRight = (sideCastleRight&2) == 2;
        for(int moveIndex=0; moveIndex < Calculate.KingMove[startSquare].length;moveIndex++) {
            //System.out.println(startSquare+"->"+Calculate.KnightMove[startSquare][moveIndex]);
            int targetSquare =  Calculate.KingMove[startSquare][moveIndex];

            int pieceOnTargetSquare = Square[targetSquare];

            //check if block by allie pieces
            if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                continue; // not look in this direction anymore
            }
            //check if it's safe to move king there
            if(!SquareIsAttacked(targetSquare)){
                moves.add(new Move(startSquare,targetSquare));
            }


            //check if block by opponent pieces
            if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                continue; // not look in this direction anymore
            }

            //castling
            if(!inCheck) {
                if ((targetSquare == 5 || targetSquare == 61) && hasKingCastleRight) {
                    int castleKingsideSquare = targetSquare + 1;
                    //System.out.println("check king");
                    if (Square[castleKingsideSquare] == Piece.None) {
                        //System.out.println(startSquare+"-"+castleKingsideSquare);
                        moves.add(new Move(startSquare, castleKingsideSquare, 1));
                    }
                }
                if ((targetSquare == 3 || targetSquare == 59) && hasQueenCastleRight) {
                    int castleQueenSquare = targetSquare - 1;
                    //System.out.println("check queen");
                    if (Square[castleQueenSquare] == Piece.None) {
                        //System.out.println(startSquare+"-"+castleQueenSquare);
                        moves.add(new Move(startSquare, castleQueenSquare, 2));
                    }
                }
            }
        }
    }

    private void GenerateKnightMove(int startSquare, int piece) {
        //System.out.println("Knight--");
        boolean isPinned = IsPinned(startSquare);
        if(!isPinned) {
            // System.out.println(Calculate.KnightMove[startSquare].length);
            for (int moveIndex = 0; moveIndex < Calculate.KnightMove[startSquare].length; moveIndex++) {

                int targetSquare = Calculate.KnightMove[startSquare][moveIndex];
                int pieceOnTargetSquare = Square[targetSquare];

                //check if block by allie pieces
                if (Piece.isColor(pieceOnTargetSquare, CurrentColor) || (inCheck && !SquareIsInCheckRay (targetSquare))) {
                    continue; // not look in this direction anymore
                }

                moves.add(new Move(startSquare, targetSquare));
            }
        }
    }

    //generate moves for sliding pieces a.k.a rook, bishop, queen
    private void GenerateSlidingMove(int startSquare, int piece) {
        //System.out.print("other");
        boolean isPinned = IsPinned(startSquare);
        if(inCheck && isPinned){//if in check and this piece is pinned then it can't move
            return;
        }

        //set option direction for different piece
        int startIndex= Piece.isType(piece,Piece.Bishop)?4:0;
        int endIndex = Piece.isType(piece,Piece.Rook)?4:8;
        //Check 8 direction
        for(int directionIndex = startIndex;directionIndex<endIndex;directionIndex++){

            int dirOffset = Calculate.DirectionOffset[directionIndex];
            if(isPinned && !IsMovingAlongRay(dirOffset,startSquare,friendlyKingSquare)){
                continue;
            }

            //count number of square to edge from that direction
            for(int n = 0; n< Calculate.NumberSquareToEdge[startSquare][directionIndex]; n++){

                //get a targetSquare n square away from startSquare in directionIndex direction
                int targetSquare = startSquare+ dirOffset*(n+1);
                int pieceOnTargetSquare = Square[targetSquare];

                //check if block by allie pieces
                if(Piece.isColor(pieceOnTargetSquare,CurrentColor)){
                    break; // not look in this direction anymore
                }
                boolean movePreventsCheck = SquareIsInCheckRay (targetSquare);
                boolean isCapture = pieceOnTargetSquare != Piece.None;
                if(movePreventsCheck || !inCheck){
                    moves.add(new Move(startSquare,targetSquare));
                }
                if(isCapture || movePreventsCheck){
                    break;
                }

                //check if block by opponent pieces
                /*if(Piece.isColor(pieceOnTargetSquare,OpponentColor)){
                    break; // not look in this direction anymore
                }*/
                //System.out.println("next");


            }
        }
    }



    public void GenerateSlidingAttackMap(){
        SlidingAttackMap = 0;
        for(int startSquare = 0; startSquare < 64 ; startSquare++){
            int piece = Square[startSquare];
            if (Piece.isColor(piece,OpponentColor)){
                if(Piece.isSlidingPiece(piece))
                    UpdatingSlidingAttackMap(startSquare,piece);
            }
        }
    }
    public void GenerateKnightAttackMap(){
        KnightAttackMap = 0;
        boolean isKnightCheck = false;
        for(int startSquare = 0; startSquare < 64 ; startSquare++){
            int piece = Square[startSquare];
            if (Piece.isColor(piece,OpponentColor)){
                if(Piece.isType(piece,Piece.Knight)){
                    for(int targetKnightSquare : Calculate.KnightMove[startSquare]){
                        KnightAttackMap |= (1L << targetKnightSquare);
                    }
                }
                if(!isKnightCheck && ((KnightAttackMap>>friendlyKingSquare)&1)!=0){
                    isKnightCheck = true;
                    inDoubleCheck = inCheck;
                    inCheck = true;
                    checkRayBitMask |= 1L << startSquare;
                }
            }
        }


    }
    public void GeneratePawnAttackMap(){
        PawnAttackMap = 0;
        boolean isPawnCheck = false;

        for(int startSquare = 0; startSquare < 64 ; startSquare++){
            int piece = Square[startSquare];
            if(Piece.isColor(piece,OpponentColor)){
                if(Piece.isType(piece,Piece.Pawn)){
                    int[] pawnCaptureOffset;
                    if(OpponentColor == Piece.Black){
                        pawnCaptureOffset = new int[]{7,9};
                    }else pawnCaptureOffset = new int[]{-7,-9};
                    for(int captureOffset : pawnCaptureOffset){
                        int targetSquare = startSquare + captureOffset;
                        int dx = Math.abs(targetSquare%8 - startSquare%8);
                        int dy = Math.abs(targetSquare/8 - startSquare/8);
                        if(dx==1 && dy==1) {
                           // System.out.println("pawn can attack: " + targetSquare);
                            PawnAttackMap |= 1L << targetSquare;
                        }
                    }
                    if(!isPawnCheck && ((PawnAttackMap>>friendlyKingSquare)&1)!=0){
                        isPawnCheck = true;
                        inDoubleCheck = inCheck;
                        inCheck = true;
                        checkRayBitMask |= 1L << startSquare;
                    }
                }
            }
        }
    }

    public void UpdatingSlidingAttackMap(int startSquare,int piece){
        int startIndex= Piece.isType(piece,Piece.Bishop)?4:0;
        int endIndex = Piece.isType(piece,Piece.Rook)?4:8;
        //Check 8 direction
        for(int directionIndex = startIndex;directionIndex<endIndex;directionIndex++){

            //count number of square to edge from that direction
            for(int n = 0; n< Calculate.NumberSquareToEdge[startSquare][directionIndex]; n++){

                //get a targetSquare n square away from startSquare in directionIndex direction
                int targetSquare = startSquare+ Calculate.DirectionOffset[directionIndex]*(n+1);
                int pieceOnTargetSquare = Square[targetSquare];

                SlidingAttackMap |= 1L << targetSquare;

                //check if block by any pieces other than king
                if(pieceOnTargetSquare != CurrentColor+Piece.King) {
                    if (!Piece.isType(pieceOnTargetSquare, Piece.None)) {
                        break; // not look in this direction anymore
                    }
                }
            }
        }
    }

    public void CalculateAttackData() {
        GenerateSlidingAttackMap();
        // Search squares in all directions around friendly king for checks/pins by enemy sliding pieces (queen, rook, bishop)
        int startDirIndex = 0;
        int endDirIndex = 8;
        for (int dir = startDirIndex; dir < endDirIndex; dir++) {
            boolean isDiagonal = dir > 3;


            int n = Calculate.NumberSquareToEdge[friendlyKingSquare][dir];
            int directionOffset = Calculate.DirectionOffset[dir];
            boolean isFriendlyPieceAlongArray = false;
            long rayMask = 0;
            //Start searching
            for (int i = 0; i < n; i++) {
                int squareIndex = friendlyKingSquare + directionOffset * (i + 1);
                rayMask |= 1L << squareIndex;
                int piece = Square[squareIndex];

                //This square contain a piece
                if (piece != Piece.None) {
                    //if is friendly Piece
                    if (Piece.isColor(piece, CurrentColor)) {
                        //it might be pinned if it's the first piece we found
                        if (!isFriendlyPieceAlongArray) {
                            isFriendlyPieceAlongArray = true;
                        } else {
                            //if it's the second piece we found then its not possible to be pinned
                            break;
                        }

                    }
                    //if contain an opponent's piece
                    else {
                        int pieceType = Piece.getType(piece);
                        //check if piece is in bitmask of piece that are able to move in this direction
                        if ((isDiagonal && (pieceType == Piece.Bishop)) || (!isDiagonal && (pieceType == Piece.Rook)) || pieceType == Piece.Queen) {
                            if (isFriendlyPieceAlongArray) {
                                pinExist = true;
                                pinRayBitMask |= rayMask;
                            }
                            //no friendly piece so this is a check
                            else {
                                checkRayBitMask |= rayMask;
                                //if already in check then it is double check
                                inDoubleCheck = inCheck;
                                inCheck = true;
                            }
                            break;
                        } else {
                            //if no piece can move this direction continue searching in another direction
                            break;
                        }
                    }
                }
            }
            if (inDoubleCheck) {//if already in double check don't have to look for more pin
                break;
            }
        }

        long KingAttackMap = 0;
        for (int moveIndex = 0; moveIndex < Calculate.KingMove[opponentKingSquare].length; moveIndex++) {
            //System.out.println(startSquare+"->"+Calculate.KingMove[startSquare][moveIndex]);
            int targetSquare = Calculate.KingMove[opponentKingSquare][moveIndex];
            KingAttackMap |= 1L << targetSquare;
        }

        GenerateKnightAttackMap();
        GeneratePawnAttackMap();

        AttackMap = SlidingAttackMap | KnightAttackMap | PawnAttackMap | KingAttackMap;
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

    public boolean SquareIsAttacked(int Square){
        return ((AttackMap >> Square)& 1) != 0;
    }

    public boolean IsPinned(int Square){
        return pinExist && ((pinRayBitMask >> Square) & 1) != 0;
    }
    public boolean SquareIsInCheckRay(int Square) {
        return inCheck && ((checkRayBitMask >> Square) & 1) != 0;
    }
    public boolean IsMovingAlongRay(int rayDir, int startSquare, int targetSquare){
        int moveDir = Calculate.directionLookup[startSquare-targetSquare+63];
        return (rayDir==moveDir || -rayDir == moveDir);
    }

    public boolean isInCheck() {
        return inCheck;
    }
}
