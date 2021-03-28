import java.net.*;
import java.io.*;

public class ClientTest implements Runnable{
    Socket client;
    int id;
    public ClientTest(Socket c, int i){
        client = c;
        id = i;
    }

    public void run(){
        try{
                // int p = Integer.parseInt(args[0]);
                // Socket client=new Socket("localhost",p);
                BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                pw.print("REGI "+String.format("%08d",id) + " 127.000.000.001 1340 127.000.000.001 1340");
                // Thread.sleep(60000);
                pw.flush();
                pw.close();
                br.close();

        }
        catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}