package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	ServerSocket ss;
	boolean quite=false;
	ArrayList<ServerThread> OurDomainsConnections=new ArrayList<ServerThread>();
	static List<Channel> channelList = new ArrayList<Channel>();
	static List<Channel> channelAI = new ArrayList<Channel>();

	public static void main(String[] args) {
		new Server();

	}
	public Server() {
		try {
			ss=new ServerSocket(3333);
			System.out.println("READY");
			while(!quite)
			{
				Socket s=ss.accept();
				ServerThread OurConnection = new ServerThread(s,this);
				OurConnection.start();
				OurDomainsConnections.add(OurConnection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Channel existChannel(String name){
		for(Channel channel:channelList){
			if(channel.getName().equals(name)){
				return channel;
			}
		}
		return null;
	}
	static Channel existChannelAI(String name){
		for(Channel channel:channelAI){
			if(channel.getName().equals(name)){
				return channel;
			}
		}
		return null;
	}
}