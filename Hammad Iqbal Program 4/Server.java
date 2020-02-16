import java.io.*;
import java.net.*;
import java.util.*;

public class Server 
{
   private Socket socket;
   private OutputStream out ;
   private int CHUNK_SIZE = 1024;
   
   public static void main(String argv[])
   {
      Server server = new Server();
      server.run();
   }
   
   private void run()
   {
      try
      {
         int portNum = 5520;
         ServerSocket servSock = new ServerSocket(portNum);
         System.out.println("Server Running...");
         while(true)
         {
            try
            {
               Date date = new Date();
               System.out.println("Waiting for conenction....");
               socket = servSock.accept();
               System.out.println("Got a connection : " + date.toString() );
               System.out.println("Connected to: " + socket.getInetAddress() + "   Port: " + socket.getPort());
               out = socket.getOutputStream();
               String name = getNullTerminatedString();
               System.out.println("Got file name: " + name);
               Long size = Long.parseLong(getNullTerminatedString());
               System.out.println("File size: " + size);
               getFile(name,size);
               socket.close();
            }
            catch(Exception e)
            {
               System.out.println(e.toString());
            }
         }
      }
      catch (Exception e)
      {
         System.out.println(e.toString());
      }
   }

   private String getNullTerminatedString()
   {
      String str = "";
      try
      {
         byte buff[] = new byte[CHUNK_SIZE];
         int count; 
         InputStream in = socket.getInputStream();
         count = in.read(buff,0,1);
         while((buff[0]) != 0 )
         {
            str += new String(buff).trim();
            count = in.read(buff,0,1);
         }
      }
      catch (Exception ex)
      {
         System.out.println(ex.toString());
      }
      return str;
    }

    private void getFile(String filename, long size)
    {
       try
       {
          long x = 0;
          InputStream in = socket.getInputStream();
          FileOutputStream output = new FileOutputStream (filename);
          byte buff[] = new byte[CHUNK_SIZE];
          while(x < size)
          {
             int y = in.read(buff);
             if( y != -1)
             {
                output.write(buff, 0, y);
                x += y;
             }
             else
             {
                out.write('Q');
                throw new Exception("Error: Did not get the file") ; 
             }
          }
          if(x == size)
          {
             out.write('@');
             System.out.println("Got the file.");
          }

       }
       catch (Exception e)
       {
          System.out.println(e.toString());
       } 
    }
}
