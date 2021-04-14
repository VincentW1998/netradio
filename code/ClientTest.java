import java.net.*;
import java.io.*;

public class ClientTest implements Runnable{
    Socket client;
    String id;
    String msg;
    public ClientTest(Socket c, String i,String mess){
        client = c;
        id = i;
        msg = String.format("%1$-" + 140 + "s", mess).replace(' ', '#');
    }

    public void run(){
        try{
                // int p = Integer.parseInt(args[0]);
                // Socket client=new Socket("localhost",p);
                BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                // pw.print("REGI "+String.format("%08d",id) + " 127.000.000.001 1340 127.000.000.001 1340");
                // Thread.sleep(60000);
                String message = "MESS "+id+" "+msg+"\r\n";
                System.out.println(message.length());
                pw.print(message);
                pw.flush();
                System.out.println(br.readLine());
                pw.close();
                br.close();

        }
        catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}