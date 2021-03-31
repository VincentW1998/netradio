import java.net.*;
import java.io.*;

public class ImAlive implements Runnable{
    Socket gestionnaire;

    public ImAlive(Socket gest){
        gestionnaire = gest;
    }

    public void run(){
        try{
            System.out.println("imalive");
            BufferedReader br = new BufferedReader(new InputStreamReader(gestionnaire.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(gestionnaire.getOutputStream()));
            String message;
            while(true){
                message = br.readLine();
                // while((message = br.readLine()) == null);
                System.out.println(message);
                if(message.equals("RUOK")){
                    pw.print("IMOK");
                    pw.flush();
                }
            }
        }
        catch(Exception e){
            return;
        }
    }
}
