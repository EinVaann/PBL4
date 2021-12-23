package server;

import model.Board;
import model.Calculate;
import model.Move;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import model.Board;
//import model.Calculate;
//import view.ChessView;


public class Channel {
    String name;
    List<Socket> player;
    String gameState;
    int turn;
    int playerNumber;
    
    Board board;
	String fenCode; 
	Calculate c;


    public Channel(String name, List<Socket> player,String gameState ,int playerNumber) {
        this.name = name;
        this.player = player;
        this.gameState = gameState;
        this.playerNumber = playerNumber;
        this.turn = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Socket> getPlayer() {
        return player;
    }

    public void setPlayer(List<Socket> player) {
        this.player = player;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void startGame(){
        gameState = "playing";
        this.board = new Board();
        this.fenCode= "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 1111 - 0 1";
        this.c = new Calculate();
        this.board.CreateBoard(fenCode);
        try {
            sendPossibleMove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPossibleMove() throws IOException {
        Socket currentPlayer = player.get(turn);
        DataOutputStream dos1 = new DataOutputStream(currentPlayer.getOutputStream());
        for(Move move: board.generateMove.moves){
            //System.out.println(move);
            dos1.writeUTF("possibleMove");
            dos1.writeUTF(move.StartSquare+"");
            dos1.writeUTF(move.TargetSquare+"");
            dos1.writeUTF(move.Flag+"");
        }
       // System.out.println("DONE");
    }

}
