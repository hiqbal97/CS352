import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class Server
{
    public static void main(String argv[])
    {
        
        try
        {
            Server server  = new Server();
            server.run(); 
        }
        catch(Exception e)
        {
            System.out.println("Failed to run");
        }
    }
    
    public void run()
    {
        try
        {
            int port = 5520;
            FileOutputStream l = new FileOutputStream("prog1b.log");
            PrintWriter file = new PrintWriter(l, true);
            ServerSocket ss = new ServerSocket(port);
            while(true)
            {
                Socket socket = ss.accept();
                ServerThread servThread = new ServerThread(socket, file); 
                servThread.start();
            }
        }
        catch(Exception e)
        {
            System.out.println("Could not connect!");
        }
    }
}

class ServerThread extends Thread
{
    Socket socket;
    BufferedReader read;
    PrintWriter write;
    PrintWriter logfile;

    public ServerThread(Socket clientSock, PrintWriter file)
    {
    	try
    	{
    		logfile = file;
		    socket = clientSock;
		    write = new PrintWriter(socket.getOutputStream(), true);
		    read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	}
    	catch(Exception e)
    	{
    		System.out.println("Could not connect!");
        }
    }
    
    public void run()
    {
        Date date = new Date();
        logfile.println("Got a connection: " + date.toString() + " " + socket.getInetAddress() + " Port: " + socket.getPort());
        try
        {
            boolean quitTime = false;
            while(!quitTime)
            {
                String inLine = read.readLine();
                if(!inLine.equals("quit"))
                {
                	Encryption convert = new Encryption(inLine);
                    write.println(convert.encrypt());
                }
                if(inLine.equals("quit"))
                {
                    write.println("Good bye!");
                	logfile.println("Connection closed. Port: " + socket.getPort());
                    quitTime = true;
                } 
            }
            socket.close();
        }
        catch(Exception e)
        {
        	logfile.println("ServerThread Exception: java.net.SocketException: Connection reset");
        }
    }
}

class Encryption
{
	String message1;
	StringBuilder converted;
	
	public Encryption(String message)
	{
		StringBuilder encrypted = new StringBuilder(message.length());
		converted = encrypted;
		message1 = message;
	}
	
	public String encrypt()
	{
		char random;
		int x = 5;
        int y = 19;
        int z = 0;
        for(int i = 0; i < message1.length(); i++)
        {
            random = message1.charAt(i);
            if((random >= 'a' && random <= 'z'))
            {
                if(z == 0)
                {
                    random  = (char)(((int)random+x-97)%26+97);
                    converted.append(random);
                    z++;
                    continue;
                }
                if(z == 1)
                {
                    random  = (char)(((int)random+y-97)%26+97);
                    converted.append(random);
                    z++;
                    continue;
                }
                if(z == 2)
                {
                    random = (char)(((int)random+y-97)%26+97);
                    converted.append(random);
                    z++; 
                    continue;
                }
                if(z == 3)
                {
                	random  = (char)(((int)random+x-97)%26+97);
                    converted.append(random);
                    z++;
                    continue;
                }
                if(z == 4)
                {
                    random= (char)(char)(((int)random+y-97)%26+97);
                    converted.append(random);
                    z = 0;
                    continue;
                }
            }
            else if((random >= 'A' && random <= 'Z'))
            {
                random = Character.toLowerCase(random);
                if(z == 0)
                {
                    random  = (char)(((int)random+x-97)%26+97);
                    converted.append(Character.toUpperCase(random));
                    z++;
                    continue;
                }
                if(z == 1)
                {
                    random  = (char)(((int)random+y-97)%26+97);
                    converted.append(Character.toUpperCase(random));
                    z++;
                    continue;
                }
                if(z == 2)
                {
                    random = (char)(((int)random+y-97)%26+97);
                    converted.append(Character.toUpperCase(random));
                    z++; 
                    continue;
                }
                if(z == 3)
                {
                    random  = (char)(((int)random+x-97)%26+97);
                    converted.append(Character.toUpperCase(random));
                    z++;
                    continue;
                }
                if(z == 4)
                {
                    random= (char)(char)(((int)random+y-97)%26+97);
                    converted.append(Character.toUpperCase(random));
                    z = 0;
                    continue;
                }
            }
            else
            {
                converted.append(random);
            }
        }
        return converted.toString();
	}
}