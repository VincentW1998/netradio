import java.io.*;
import java.net.*;
import java.util.*;

public class Service_Gestionnaire implements Runnable{
    private Socket client;
    private final int maxDiff = 99;
    private BufferedReader br;
    private PrintWriter pw;
    static LinkedList<Diffuser> register = new LinkedList <Diffuser> ();

    public Service_Gestionnaire(Socket c){
        client = c;
        try{
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        }
        catch(Exception e){
            System.out.println("Service constructor : Service_Gestionnaire.java");
            e.printStackTrace();
        }
    }

    public static boolean isGoodRegi(String mess []){ // check if all arguments in REGI message is correct
        try{
            if(mess.length != 6 || mess[1].length() != 8 || mess[2].length() != 15 || mess[3].length() != 4 || mess[4].length() != 15 || mess[5].length() != 4)
                return false;
        
            InetAddress.getByName(mess [2]); // will return an UnkwonHostException if the ip is invalid
            InetAddress.getByName(mess [4]);
            int port1 = Integer.parseInt(mess[3]);
            int port2 = Integer.parseInt(mess[5]);
            return port1 < 9998 && port1 >= 1024 && port2 < 9998 && port2 >= 1024;
        }
        catch(UnknownHostException ue){
            return false;
        }
    }    

    public void Regi(String message) throws IOException{
        String splitRegi [] = message.split(" ");
        if(!isGoodRegi(splitRegi) || register.size() >= maxDiff){ // check if the REGI message has the correct amount of arguments
            System.out.println("Issue with Regi message, connection closed");
            pw.print("RENO");
            pw.flush();
            client.close();
            return;
        }
        String id = splitRegi[1];
        InetAddress ip1 = InetAddress.getByName(splitRegi [2]);
        int port1 = Integer.parseInt(splitRegi[3]);
        InetAddress ip2 = InetAddress.getByName(splitRegi [4]);
        int port2 = Integer.parseInt(splitRegi[5]);
        Diffuser currDiff = new Diffuser(id, ip1, port1, ip2, port2);
        register.add(currDiff);
        pw.print("REOK");
        pw.flush();
        // areUAlive();
        // register.remove(currDiff);
    }
    
    public void areUAlive(){ // check if the diffuser is still active
        try{
            String mess;
            while(true){
                try{
                    Thread.sleep(10000);
                    pw.print("RUOK");
                    pw.flush();
                    mess = br.readLine();
                    // while((mess = br.readLine()) == null);
                    if(mess == null || !mess.equals("IMOK"))
                        return;
                        // Thread.sleep(5000);
                }
                catch (InterruptedException ie){
                    //ignore
                }
            }
        }
        catch(Exception e){
            System.out.println("areUalive error in Service_Gestionnaire");
            e.printStackTrace();
        }
    }

    public void client_handler(String message){
        try{
            String mess = message;
            while(true){
                if(mess.equals("LIST")){
                    sendRegister();
                    client.close();
                    return;
                }
                System.out.println(mess);
                while((mess = br.readLine()) == null);
            }
        }
        catch(Exception e){
            System.out.println("client_handler :  Gestionnaire_clienthandler.java");
            e.printStackTrace();
        }
    }

    public void sendRegister(){
        pw.print("LINB "+ String.format("%02d", register.size())+"\n");
        pw.flush();
        for(int i = 0 ; i<register.size(); i++){
            pw.print("ITEM "+register.get(i).toString()+"\n");
            pw.flush();
        }
        return;
    }


    public void gest(){
        try {
            String message;
            while((message = br.readLine()) == null);
            System.out.println(message);
            if(message.startsWith("REGI "))
                Regi(message);
            else 
                client_handler(message);
        }
        catch(Exception e){
            System.out.println("issue in gest() : Service_Gestionnaire");
            e.printStackTrace();
        }
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