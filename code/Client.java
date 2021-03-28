import java.net.*;

public class Client{
    public static void main(String [] args){
        try{
            int p = Integer.parseInt(args[0]);
            for(int i =0 ; i < 5 ; i++){
                Socket client=new Socket("localhost",p);
                ClientTest ct = new ClientTest(client, i);
                Thread t = new Thread(ct);
                t.start();
            }
        }
        catch (Exception e){
            // e.printStackTrace();
        }
    }
}