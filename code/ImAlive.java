import java.io.*;

public class ImAlive implements Runnable{
    private PrintWriter pw;
    private BufferedReader br;

    public ImAlive(BufferedReader b, PrintWriter p){
        br = b;
        pw = p; 
    }

    public void run(){
        try{
            String message;
            while(true){
                message = br.readLine();
                if(message.equals("RUOK")){
                    pw.print("IMOK\n");
                    pw.flush();
                }
                else{
                    System.out.println("message not recognized");
                    return;
                }
            }
        }
        catch(Exception e){
            return;
        }
    }
}
