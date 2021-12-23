package server;


import AI.Search;
import model.Board;
import model.Move;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;
    Server server;
    boolean quite = false;
    Channel channel;

    public ServerThread(Socket Socket, Server Server) {
        super("ServerThread");
        this.socket = Socket;
        this.server = Server;
    }

    public void sendMessageToThisClient(String message) {
        try {
            dout.writeUTF("/msg Server " + message);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequestFromClient(String request) {
        String requestData = request.split(" ")[0];
        switch (requestData) {
            case "/channel" -> setChannel(request);
            case "/channelsp" -> setAIChannel();
            case "/code" -> receiveChessMove(request);
            case "/msg" -> sendMessage(request);
            case "/listChannel" -> sendAvailableChannel();
            case "/leave" -> leave();
            case "/newGame" -> newGame();
        }
    }

    private void newGame() {
        for (Socket player : channel.player) {
            try {
                DataOutputStream dos1 = new DataOutputStream(player.getOutputStream());
                dos1.writeUTF("/newGame");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        channel.startGame();
        channel.setTurn(0);
    }

    private void leave() {

        for (Socket player : channel.player) {
            try {
                DataOutputStream dos1 = new DataOutputStream(player.getOutputStream());
                dos1.writeUTF("/msg Server The game has ended due to a player has left the channel");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        channel.player.remove(this.socket);
        if (channel.player.size() == 0) {
            Server.channelList.remove(this.channel);
        }
        this.channel = null;
        this.interrupt();
    }

    private void sendAvailableChannel() {
        boolean hasChannel = false;
        for (Channel s : Server.channelList) {
            if (s.player.size() < s.getPlayerNumber()) {
                sendMessageToThisClient("Channel: " + s.name + " is available");
                hasChannel = true;
            }
        }
        if (!hasChannel) {
            sendMessageToThisClient("No channel is available");
        }
    }

    public void sendMessage(String OutText) {
        if (channel != null && channel.getGameState().equals("playing")) {
            try {
                for (Socket s : channel.player) {
                    DataOutputStream dos1 = new DataOutputStream(s.getOutputStream());
                    dos1.writeUTF(OutText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveChessMove(String chessMove) {
        if (channel != null && channel.getGameState().equals("playing")) {
            if (channel.playerNumber == 1) {
                executeMove(chessMove);
                if (channel.gameState.equals("playing")) {
                    Search search = new Search(channel.board.generateMove, channel.board);
                    search.SearchMove(4, -999999, 999999);
                    Move moves = search.bestPossibleMove;
                    channel.board.executeMove(moves);
                    String AIMove = "/code " + moves.StartSquare + "-" + moves.TargetSquare + "-" + moves.Flag;
                    try {
                        dout.writeUTF(AIMove);
                        channel.sendPossibleMove();
                        if(channel.board.generateMove.moves.size()==0) {
                            System.out.println("endGame");
                            sendMessageToThisClient("/endGame");
                            channel.setGameState("end");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                int turn = channel.player.get(0) == this.socket ? 0 : 1;
                int oppo = 1 - turn;
                if (turn == channel.turn) {
                    try {
                        DataOutputStream dos1 = new DataOutputStream(channel.player.get(oppo).getOutputStream());
                        dos1.writeUTF(chessMove);
                        executeMove(chessMove);
                        channel.setTurn(oppo);
                        channel.sendPossibleMove();
                        if (channel.board.generateMove.moves.size() == 0) {
                            sendMessageToThisClient("/endGame");
                            channel.setGameState("end");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void setAIChannel() {
        if (channel == null) {
            List<Socket> player = new ArrayList<>();
            player.add(this.socket);
            Channel newChannel = new Channel(null, player, "playing", 1);
//            Server.channelList.add(newChannel);
            System.out.println("create channelAI");
            this.channel = newChannel;
            this.channel.startGame();
            sendMessageToThisClient("Channel AI create success");

        } else {
            sendMessageToThisClient("ChannelAI is full");
        }

    }

    public void setChannel(String OutText) {
        String channelName = OutText.split(" ")[1];
        System.out.println(OutText);
        Channel channel = Server.existChannel(channelName);
        if (channel == null) {
            List<Socket> player = new ArrayList<Socket>();
            player.add(this.socket);
            Channel newChannel = new Channel(channelName, player, "waiting", 2);
            Server.channelList.add(newChannel);
            System.out.println("create channel");
            this.channel = newChannel;
            sendMessageToThisClient("Channel create success");

        } else {
            if (channel.player.size() < channel.playerNumber) {
                channel.player.add(this.socket);
                channel.startGame();
                System.out.println("set player 2");
                this.channel = channel;
                sendMessageToThisClient("Join Channel success");
            } else {
                System.out.println("occupy");
                sendMessageToThisClient("Channel is full");
            }
        }
    }


    public void executeMove(String Rep) {
        String[] code = Rep.split(" ");

        if (code[0].equals("/code")) {
            String[] moveData = code[1].split("-");
            int startSquare = Integer.parseInt(moveData[0]);
            int targetSquare = Integer.parseInt(moveData[1]);
            int flag = Integer.parseInt(moveData[2]);
            //System.out.println("movePiece");
            channel.board.executeMove(new Move(startSquare, targetSquare, flag));
        }
    }

    public void ListChannel() {
        for (Channel s : Server.channelList) {
            sendMessageToThisClient(s.name);
        }

    }

    public void run() {
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while (!quite) {
                while (din.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String ComingText = din.readUTF();
                processRequestFromClient(ComingText);
            }
            din.close();
            dout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

