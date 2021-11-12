package controller;

import java.io.IOException;
import java.net.Socket;//this is the socket package
/*dont under any circumstance remove this import XD*/
import java.net.UnknownHostException;
import view.ChessView;

import java.util.Scanner;

import javax.swing.JFrame;

import model.Board;
import model.Calculate;

public class Client{

	MultiClients ClientThread;
	public static void main(String[] args) {

		new Client();
	}
	public Client()
	{
		Board board = new Board();
		//Bị thay đổi  chuỗi ở đây nên không thể đánh qua lại
		String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		Calculate c = new Calculate();
		board.CreateBoard(fenCode);
		board.ShowBoard();
		ChessView chessView = new ChessView(board);

	}

}
