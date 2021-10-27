package ServerAndClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	ServerSocket ss;
	boolean quite=false;
	ArrayList<MultiServerConnection> OurDomainsConnections=new ArrayList<MultiServerConnection>();
	
	public static void main(String[] args) {
		new Server();

	}
	public Server() {
		try {
			ss=new ServerSocket(3333);
			while(!quite)
			{
				Socket s=ss.accept();
				MultiServerConnection OurConnection = new MultiServerConnection(s,this);
				OurConnection.start();
				OurDomainsConnections.add(OurConnection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}