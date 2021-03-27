import java.net.*;
import java.io.*;

public class Gestionnaire{

    public static ServerSocket connectToAvailablePort(int p){
        try{
            ServerSocket server = new ServerSocket(p);
            return server;
        }
        catch (IOException e){
            if(p == 1024)
                return connectToAvailablePort(9999);
            return connectToAvailablePort(p--);
        }
    }
    public static void main(String[] args){
        try {
            ServerSocket server = connectToAvailablePort(9999);
            System.out.println("Gestionnaire connected to port "+server.getLocalPort()+"\n");
            while(true){
                Socket client = server.accept();
                Service_Gestionnaire SG = new Service_Gestionnaire(client);
                System.out.println("New connection detected");
                Thread t = new Thread(SG);
                t.start();
            }
        }
        catch (Exception e){
            System.out.println("Gestionnaire.java Error");
            e.printStackTrace();
        }
    }
}