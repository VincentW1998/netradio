import java.net.*;
import java.io.*;

public class ClientTest implements Runnable{
    Socket client;
    String id;
    String msg;
    public ClientTest(Socket c, String i,String mess){
        client = c;
        id = i;
        msg = Diffuser.fill_hashtag_or_zero(mess, 140, "#");
    }

    public void run(){
        try{
                BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                String message = "MESS "+id+" "+msg+"\r\n"; // 156
                System.out.println("taille : " + message.length());
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