package ServerAndClient;

import java.io.IOException;
import java.net.Socket;//this is the socket package
/*dont under any circumstance remove this import XD*/
import java.net.UnknownHostException;

///////////////////////////////////////
//our scanner import
import java.util.Scanner;

import javax.swing.JFrame;

import backup.ChessController;
import model.Board;
import model.Calculate;

///////////////////////////////////////
//our GUI libraries
////////////////////////////////////////
public class Client{
	/////////////////////////////////////
	//better than sending them as an arguments to each function
	MultiClients ClientThread;
	////////////////////////////////////
	public static void main(String[] args) {
//		for(int i =1; i<200;i++) {
		new Client();}
	//}
	public Client()
	{
		Board board = new Board();
		String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";
		Calculate c = new Calculate();
		board.CreateBoard(fenCode);
		board.ShowBoard();
		ChessController chessController = new ChessController(board);


//        Board board = new Board();
//        String fenCode = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";
//        Calculate c = new Calculate();
//        board.CreateBoard(fenCode);
//        board.ShowBoard();
//
//		tuna crape = new tuna();
//		crape.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		crape.setSize(1200,600);
//		crape.setVisible(true);





		/*try {
			Socket s=new Socket("localhost",3333);
			ClientThread =new MultiClients(s);
			System.out.println("Enter Your Anonomus Name:");
			@SuppressWarnings("resource")
			Scanner UserThreadName=new Scanner(System.in);
			String UserName=UserThreadName.nextLine();
			ClientThread.setName(UserName);
			ClientThread.SetClient("channel0",UserName);
			ClientThread.start();
			for(int i=0;i<50;i++)
			{
				System.out.print("\n");
			}
			ListenForInput();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	//Listen for input from user and response from server
	//user will use quite for leaving the chat
//	public void ListenForInput()
//	{
//		//Scanners are used to read input of user from conceal
//		@SuppressWarnings("resource")
//		Scanner console=new Scanner(System.in);
//		while(true)
//		{
//			//waiting for a line form console
//			while(!console.hasNextLine())//only run upon pressing run
//			{//make sure not to leave thread awake, my cpu was overloaded XD
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			String input=console.nextLine();//this method will automatically get the new line
//			System.out.println(input);
//			if(input.toLowerCase().equals("quit"))
//			{
//
//				break;
//				
//			}
//
//			if(input.toLowerCase().equals("change channel"))
//			{
//				input=console.nextLine();
//				ClientThread.c.SetChannel(input);
//			}
//			else
//			{
//				ClientThread.ClientOutServerIn(input);
//			}
//		}
//		ClientThread.CloseClient();
//	}
}
