import java.io.*;
import java.util.concurrent.*;

public class ImAlive implements Runnable{
    private PrintWriter pw;
    private BufferedReader br;

    public ImAlive(BufferedReader b, PrintWriter p){
        br = b;
        pw = p; 
    }
    public void run(){
        try{
            String message = "";
            while(true){
                try{
                    message = CompletableFuture.supplyAsync(() -> {
                        try {
                            return (String) br.readLine();           
                        } catch (Exception e) {
                            return null;
                        }
                    }).get(6, TimeUnit.SECONDS);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
                if(message.equals("RUOK")){
                    pw.print("IMOK\n");
                    pw.flush();
                }
                else{
                    System.out.println("message not recognized");
                    System.exit(1);
                }
            }
        }
        catch(Exception e){
            System.out.println("Connexion interupted with Gestionnaire");
            System.exit(1);
        }
    }
}
