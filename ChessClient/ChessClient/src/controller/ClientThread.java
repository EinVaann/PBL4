package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


import model.Board;
import model.Move;
import view.ChessView;

public class ClientThread extends Thread {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    public ChessView view;

    public String NameClient;

    public String getNameClient() {
        return this.NameClient;
    }

    public ClientThread(Socket Socket, ChessView view) {
        socket = Socket;
        this.view = view;
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(String Text) {
        try {
            this.dout.writeUTF(Text);
            this.dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                try {
                    String reply = din.readUTF();
                    if (reply.equals("possibleMove")) {
                        int start = Integer.parseInt(din.readUTF());
                        int target = Integer.parseInt(din.readUTF());
                        int flag = Integer.parseInt(din.readUTF());
                        Move move = new Move(start, target, flag);
                        view.addMove(move);
                    }
                    processReply(reply);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                din.close();
                dout.close();
                socket.close();
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }

    public void CloseClient() {
        try {
            din.close();
            dout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processReply(String reply) {

        String[] code = reply.split(" ");

        if (code[0].equals("/code")) {
            String[] moveData = code[1].split("-");
            int startSquare = Integer.parseInt(moveData[0]);
            int targetSquare = Integer.parseInt(moveData[1]);
            view.executeMove(startSquare, targetSquare);
        }
        if (code[0].equals("/msg")) {
            String mess = code[1] + ":";
            for (int i = 2; i < code.length; i++) {
                mess += (" " + code[i]);
            }
            if(mess.equals("Server: /endGame")){
                view.showMessage("The game has ended.");
            }
            if(view.chat!=null) {
                view.chat.setDisplay(mess);
                if (mess.equals("Server: Channel create success") || mess.equals("Server: Join Channel success")) {
                    view.setChannelView();
                }
            }
        }
        if(code[0].equals("/newGame")){
            view.board.CreateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 1111 - 0 1");
            view.boardPanel.repaint();
        }
    }
}