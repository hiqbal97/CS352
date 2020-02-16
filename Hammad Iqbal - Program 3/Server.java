import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
	private static final double LOSS_RATE = 0.3;
    private static final int AVERAGE_DELAY = 100;
    private static final int PACKET_SIZE = 512;
    private static final int DOUBLE = 2;
    
    public static void main(String[] args)
    {
    	try
        {
            Server server = new Server();
            server.run();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void run()
    {
        System.out.println("Ping Server running....");
        try
        {
            Random random = new Random(new Date().getTime());
            DatagramSocket udpSocket = new DatagramSocket(5520);
            while(true)
            {
                try
                {
                    System.out.println("Waiting for UDP packet....");
                    byte[] buff = new byte[PACKET_SIZE];
                    DatagramPacket inpacket = new DatagramPacket(buff, PACKET_SIZE);
                    udpSocket.receive(inpacket);
                    String message = new String(inpacket.getData(),  0, inpacket.getLength());
                    System.out.println("Received from: " + inpacket.getSocketAddress() + " " + message);
                    if (random.nextDouble() < LOSS_RATE)
                    {
                        System.out.println("Packet loss...., reply not sent.");
                        continue;
                    }
                    Thread.sleep((int)(random.nextDouble() * DOUBLE * AVERAGE_DELAY));
                    udpSocket.send(inpacket);
                    System.out.println("Reply Sent.");
                }
                catch (IOException e)
                {
                    System.out.println(e);
                }
                catch (InterruptedException e)
                {
                    continue;
                }    
            }
        }
        catch (SocketException e)
        {
            System.out.print(e);
        }
    } 
}