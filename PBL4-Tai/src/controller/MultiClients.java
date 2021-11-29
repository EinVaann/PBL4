package controller;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



import model.Board;
import model.Calculate;
import view.ChessView;

public class MultiClients extends Thread {
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	boolean quite=false;
	public ClientData cd;
	
	public ChessView GUI;

    public Board board;

    public Calculate ca ;


	
	public MultiClients(Socket OurMultiSocket,ChessView gui)
	{
		s=OurMultiSocket;
		cd=new ClientData();
		GUI=gui;
	}
	public void ClientOutServerIn(String Text)
	{
		try {
			if(Text.equals("change channel"))
			{
				System.out.print("sending changing channel: "+Text+"\n");
				dout.writeUTF(Text);
				dout.flush();
			}
			else if(Text.equals("new user"))
			{
				System.out.print("sending new user: "+ Text+"\n");
				dout.writeUTF(Text+":"+cd.GetName()+"="+cd.GetChannel());
				dout.flush();
			}
			else
			{
				dout.writeUTF(cd.GetChannel()+"="+this.getName()+":"+Text);
				dout.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void SetClient(String channel,String Name)
	{
		cd.SetName(Name);
		cd.SetChannel(channel);
		
	}
	public void run()
	{
		try {
			din=new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			while(!quite)
			{
				try {
					while(din.available()==0)
					{
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					String reply=din.readUTF();
					String Chan=ExtractChannel(reply);
					String name=ExtractName(reply);
					if (reply.equals("change channel"))
					{
						System.out.print("changing channel in body: "+reply+"\n");
						GUI.ClearDisplay();
						setChangedChannel();
						        

					}

					if(name.equals("new user"))
					{
						System.out.print("new user in body: "+reply+"\n");
						setChannel(reply);
					}
					else
					{
						PrintReply(Chan,reply);
					}

				} catch (IOException e) {
					e.printStackTrace();
					try {
						din.close();
						dout.close();
						s.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				din.close();
				dout.close();
				s.close();
			} catch (IOException x) {

				x.printStackTrace();
			}
		}
	}
	public void CloseClient()
	{
		try {
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String ExtractName(String x)
	{
		String[]Y=x.split(":");
		return Y[0];
	}
	public String ExtractChannel(String X)
	{
		String[]Y=X.split("=");
		return Y[0];
	}
	public void PrintReply(String Chan,String Rep)
	{
		if(cd.GetChannel().equals(Chan))
		{
			String []Y=Rep.split("=");
			
			
			String []code = Y[1].split(":");
			if(code[1].equals("code")) {
				String[] moveData = code[2].split("-");
				int startSquare = Integer.parseInt(moveData[0]);
				int targetSquare = Integer.parseInt(moveData[1]);
				GUI.executeMove(startSquare, targetSquare);
			}
			if(code[1].equals("msg"))
				GUI.setDisplay(code[0]+":"+code[2]);
			System.out.println(Y[1]+"\n \n \n \n");
		}
		
	}
	public void setChannel(String x)
	{
		String []Y=x.split(":");
		String []Z=Y[1].split("=");
		System.out.print("setting "+Z[0]+" channel to "+Z[1]+"\n");
		GUI.setUserInChannel(Z[0]);
	}
	public void setChangedChannel()
	{
		GUI.setUserInChannel(cd.GetName()+": "+cd.GetChannel());
	}
public class ClientData
	{
		public String ClientName;
		public String channel;
		
		public void SetChannel(String Chan)
		{
			channel=Chan;
		}
		public void SetName(String name)
		{
			ClientName=name;
		}
		public String GetChannel()
		{
			return channel;
		}
		public String GetName()
		{
			return ClientName;
		}
	}
	
}