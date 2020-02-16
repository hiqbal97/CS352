import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

public class Client extends UDPPinger
{
	static final int NUM_PINGS = 10;
    static final int TIMEOUT = 1000; 
	static final int REPLY_TIMEOUT = 5000;
	static boolean[] replies = new boolean[NUM_PINGS]; 
	static long[] rtt = new long[NUM_PINGS];
	private String host;
	private int port;
	
	public Client(String inHost, int inPort) 
    {
		host = inHost;
		port = inPort;
	}
	    
	public static void main(String arg[])
	{
		Client client = new Client("localhost", 5520);
		client.run();
	}
	
	private void handleRTT(String payload)
	{
		String[] parts = payload.split(" ");
		long time;
		time = new Date().getTime() - Long.valueOf(parts[2]);
		rtt[Integer.valueOf(parts[1])] = time;  	
	}
	    
	public void run()
	{
		System.out.println("Contacting host: " + host + " at port " + port);
		createSocket();
		for(int i = 0; i < NUM_PINGS; i++)
		{
			try
			{
				PingMessage pingMessage = new PingMessage(InetAddress.getByName(host), port, "PING " + i + " " + String.valueOf(new Date().getTime()));
	            sendPing(pingMessage);   
	            PingMessage receivePingMessage = receivePing();
	            System.out.println("Received packet from " + receivePingMessage.getIP().toString() +  " " + new Date().toString());
	            replies[i] = true;
	            handleRTT(receivePingMessage.getPayload());    
			}
			catch(Exception e)
			{
				continue;
			}	
		}
		
		for(int i = 0; i < NUM_PINGS; i++)
		{
			if(replies[i] == false)
				rtt[i] = TIMEOUT; 
			System.out.println("PING " + i + ": " + replies[i] + " RTT: " + rtt[i]);
		} 
		double average = rtt[0];
		long min = rtt[0];
		long max = rtt[0];
		if(replies[0] == false)
			rtt[0] = TIMEOUT;
		for(int i = 1; i < NUM_PINGS; i++)
		{
			if(replies[i] == false)
				rtt[i] = TIMEOUT;
			average += rtt[i];
			if(min > rtt[i])
				min = rtt[i];
			if(max < rtt[i])
				max = rtt[i];	
		}
		average = (average / NUM_PINGS);
		System.out.println("Minimum = " + min + "ms," + " Maximum = " + max + "ms," + " Average = " + average + "ms." ); 
	}
}

class PingMessage
{
	private InetAddress addr;
    private int port;
    private String payload;
	public PingMessage(InetAddress addr, int port, String payload)
	{
		this.addr = addr;
		this.port = port;
		this.payload = payload;
	}
	public InetAddress getIP()
	{
		return addr;
	}
	public int getPort()
	{
		return port;
	}
	public String getPayload()
	{
		return payload;	
	}
}

class UDPPinger
{
	static final int MAX_PAYLOAD = 512; 
    static final int REPLY_TIMEOUT = 1000;
    DatagramSocket socket;
    
    public void createSocket()
    {
        try
        {
            socket = new DatagramSocket();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
	public void sendPing(PingMessage ping)
	{
		DatagramPacket SendPing = new DatagramPacket(ping.getPayload().getBytes(), ping.getPayload().length(), ping.getIP(), ping.getPort());
		try
		{
			socket.send(SendPing);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	public PingMessage receivePing()
	{
		PingMessage receiveMessage = null; 
        byte[] buff = new byte[MAX_PAYLOAD];
        DatagramPacket ReceivePing = new DatagramPacket(buff, MAX_PAYLOAD);   
        try
        {
            socket.setSoTimeout(REPLY_TIMEOUT);
            socket.receive(ReceivePing);
            receiveMessage = new PingMessage(ReceivePing.getAddress(),ReceivePing.getPort(), new String(ReceivePing.getData(), 0, ReceivePing.getLength()));
        }
        catch(SocketTimeoutException e)
        {
            System.out.println(e);
        }
        catch(Exception ex)
        {
        }
        return receiveMessage;
	}
	
}
