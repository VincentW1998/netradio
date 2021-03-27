import java.io.*;
import java.net.*;

public class Service_Gestionnaire implements Runnable{
    private Socket client;
    private BufferedReader br;
    private PrintWriter pw;

    public Service_Gestionnaire(Socket c){
        client = c;
        try{
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        }
        catch(Exception e){
            System.out.println("Service constructor");
            e.printStackTrace();
        }
    }

    public void gest(){
        pw.print("Hello you are connected");
        pw.flush();
    }

    public void run(){
        try{
            gest();
            br.close();
            pw.close();
            client.close();
        }
        catch (Exception e){
            System.out.println("run Service_Gestionnaire");
        }
    }
}