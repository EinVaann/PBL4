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

    public GenerateMove generateMove;
    private static final int WhiteIndex = 0;
    private static final int BlackIndex = 1;

    public int[] Square;
    public int TurnColor;

    public int[] kingSquare;

    public Stack<Move> preMove;

    public String state;
    public int gameState;
    public Stack<Integer> stateHistory;
    //bit 0-3 store white/black queen/king castling right
    //bit 4-9 store enp square ( only need (0 mean no enp) and from the current color can calculate
    //bit 10->14 captured piece
    private int wKingCastle = 1; //0001
    private int wQueenCastle = 2; // 0010
    private int bKingCastle = 4; //0100
    private int bQueenCastle = 8; // 1000
    private int wCastle = wKingCastle | wQueenCastle;
    private int bCastle = bKingCastle | bQueenCastle;

    public Board(){
        Square = new int[64];
        generateMove = null;
        state = "PLAYING";
        gameState = 15;// = 00000 00000 1111
        stateHistory = new Stack<>();
        preMove = new Stack<>();
        stateHistory.add(gameState);
    }

    public void CreateBoard(String fen){
       //create Board from a fenCode
        String board = fen.split(" ")[0];
        LoadPosition(board);
        //System.out.println(Square[0]);
        //set turn from fenCode
        String turn = fen.split(" ")[1];
        TurnColor = (turn.equals("w"))?8:16;

        String enPassant = fen.split(" ")[4];
        if(!enPassant.equals("-")){
            
        }
        String castleRight = fen.split(" ")[2];
        int castle = Integer.parseInt(castleRight);
        wKingCastle = castle%10;
        castle /= 10;
        wQueenCastle = castle%10*2;
        castle /= 10;
        bKingCastle &= castle%10*4;
        castle /= 10;
        bQueenCastle &= castle%10*8;
        wCastle = wKingCastle | wQueenCastle;
        bCastle = bKingCastle | bQueenCastle;
        int castleState = wCastle | bCastle;
        gameState = castleState;
        stateHistory.push(gameState);
        generateMove = new GenerateMove(this);
        // System.out.println(castleRight.length());
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

    public void MovePiece(int startSquare,int targetSquare){
        Square[targetSquare] = Square[startSquare];
        Square[startSquare] = 0;
        TurnColor = TurnColor==16?8:16;
        generateMove = new GenerateMove(this);
    }

    public void executeMove(Move move){
        gameState = 0;


        int colorIndex = TurnColor/8-1;
        int opponentColorIndex = 1 - colorIndex;

        int startSquare = move.StartSquare;
        int targetSquare = move.TargetSquare;
        int flag = move.Flag;

        int capturePiece = Square[targetSquare];
        int capturePieceType = Piece.getType(Square[targetSquare]);
        int movePiece = Square[startSquare];
        int movePieceType = Piece.getType(movePiece);

        if(movePieceType == Piece.Rook){
            switch (startSquare){
                case 0 -> bQueenCastle = 0;
                case 7 -> bKingCastle = 0;
                case 63 -> wKingCastle = 0;
                case 56 -> wQueenCastle = 0;
            }
        }
        if(capturePieceType == Piece.Rook) {
            switch (targetSquare) {
                case 0 -> bQueenCastle = 0;
                case 7 -> bKingCastle = 0;
                case 63 -> wKingCastle = 0;
                case 56 -> wQueenCastle = 0;
            }
        }

        if (movePieceType == Piece.King) {
            kingSquare[colorIndex] = targetSquare;
            if(TurnColor == Piece.White){
                wKingCastle=0;
                wQueenCastle=0;
            }else {
                bKingCastle = 0;
                bQueenCastle = 0;
            }
        }

        Square[targetSquare] = Square[startSquare];
        Square[startSquare] = 0;
//        if(Piece.isType(capturePieceType,Piece.King)){
//           state = "GAME END";
//            return;
//        }

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
            if(flag==3){
                int EnPassant;
                if(TurnColor==Piece.White){
                    EnPassant=targetSquare+8;
                }else EnPassant=targetSquare-8;
                gameState |= EnPassant << 4;
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
        wCastle = wKingCastle | wQueenCastle;
        bCastle = bKingCastle | bQueenCastle;
        preMove.add(move);
        gameState |= (wCastle | bCastle);
        gameState |= capturePiece << 10;

        stateHistory.push(gameState);

        TurnColor = TurnColor==16?8:16;
        generateMove = new GenerateMove(this);

    }

    public void UndoMove(Move move){
        int startSquare = move.StartSquare;
        int targetSquare = move.TargetSquare;
        int flag = move.Flag;

        int opponentColor = TurnColor;
        boolean undoingWhiteMove = opponentColor==Piece.Black;
        int currentColor = opponentColor==Piece.Black?Piece.White:Piece.Black;

        boolean isEnPassant = flag == 4;
        boolean isPromote = flag == 5;

        int toSquarePieceType = Piece.getType(Square[targetSquare]);
        int movedPieceType = (isPromote) ? Piece.Pawn : toSquarePieceType;
        Square[startSquare] = currentColor + movedPieceType;

        int capturePiece = (gameState >> 10) & 31;

        if (movedPieceType == Piece.King) {
            kingSquare[currentColor/8-1] = startSquare;
        }
        if(isEnPassant){
            int EnPassantCapture = targetSquare+((undoingWhiteMove)?8:-8);
            Square[targetSquare] = 0;
            Square[EnPassantCapture] = capturePiece;
        }else if(flag==1) {
            int rookBeforeCastling = undoingWhiteMove ? 63 : 7;
            Square[rookBeforeCastling] = currentColor + Piece.Rook;
            Square[targetSquare] = Piece.None;
            Square[targetSquare - 1] = Piece.None;
        }else if(flag==2) {
            int rookBeforeCastling = undoingWhiteMove?56:0;
            Square[rookBeforeCastling] = currentColor+Piece.Rook;
            Square[targetSquare] = Piece.None;
            Square[targetSquare+1] = Piece.None;
        }else {
            Square[targetSquare] = capturePiece;
        }
        stateHistory.pop();
        int oldGameState = stateHistory.peek();

        TurnColor = TurnColor==16?8:16;

        gameState = oldGameState;
        int castleState = gameState & 15;
        wKingCastle = castleState&1;
        wQueenCastle = castleState&2;
        bKingCastle = castleState&4;
        bQueenCastle = castleState&8;

        generateMove = new GenerateMove(this);



    }

    public Board(int[] square, int turnColor, int[] kingSquare, Stack<Move> preMove, int gameState, Stack<Integer> stateHistory) {
        Square = square;
        TurnColor = turnColor;
        this.kingSquare = kingSquare;
        this.preMove = preMove;
        this.gameState = gameState;
        this.stateHistory = stateHistory;
    }

    public Move getMove(int startSquare, int targetSquare){
        for(Move move: generateMove.moves){
            if(move.StartSquare==startSquare && move.TargetSquare==targetSquare)
                return move;
        }
        return null;
    }

    public void doMove(){
        int[] priorList = getPriority(generateMove.moves);
        int maxIndex = 0;
        int max = 0;
        for(int index=0;index<priorList.length;index++){
            if (priorList[index] >max){
                maxIndex = index;
                max = priorList[index];
            }
        }
        if(max==0 || max==1){
            doRandomMove();
            return;
        }
        Move move = generateMove.moves.get(maxIndex);
        executeMove(move);
    }

    public int[] getPriority(List<Move> moveList){
        int size = moveList.size();
        int[] priorList = new int[size];
        for(int index = 0;index<size;index++){
            Move move = moveList.get(index);
            if(move.Flag==1 || move.Flag==2){
                priorList[index] = 10; continue;
            }
            if(move.Flag==5){
                priorList[index] = 9; continue;
            }
            if(Square[move.TargetSquare]!=0){
                int capturePiece = Square[move.TargetSquare];
                int pieceType = capturePiece%8;
                if(pieceType==Piece.King){
                    priorList[index] = 8;continue;
                } if(pieceType==Piece.Queen){
                    priorList[index] = 7;continue;
                } if(pieceType==Piece.Knight){
                    priorList[index] = 6;continue;
                } if(pieceType==Piece.Rook){
                    priorList[index] = 5;continue;
                } if(pieceType==Piece.Bishop){
                    priorList[index] = 4;continue;
                }
            }
            if(move.Flag==3){
                priorList[index] = 1; continue;
            }
            if(Piece.isType(Square[move.StartSquare],Piece.Pawn)){
                priorList[index] = 0; continue;
            }
            boolean inCheck = generateMove.isInCheck();
            if(Piece.isType(Square[move.StartSquare],Piece.King)){
                int priorValue = -1;
                if(inCheck){
                   if(Square[move.TargetSquare]!=0) priorValue = 12;
                   else priorValue = 11;
                }
                priorList[index] = priorValue; continue;
            }
            priorList[index] = 0;
        }
        return priorList;
    }
    public void doRandomMove(){
        int numberOfMove = generateMove.moves.size();
        if(numberOfMove>0) {
            Random rand = new Random();
            int moveIndex = rand.nextInt(numberOfMove);
            Move move = generateMove.moves.get(moveIndex);
            executeMove(move);
        }
    }


}


