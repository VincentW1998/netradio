import java.net.*;
import java.io.*;

public class Diffuser {
    private String id;
    private InetAddress ip1, ip2;
    private int port1, port2;

    public Diffuser(String identifiant, InetAddress ipAdress1, int p1, InetAddress ipAdress2, int p2) {
        id = identifiant;
        ip1 = ipAdress1;
        ip2 = ipAdress2;
        port1 = p1;
        port2 = p2;
    }

    public static String getFullIp(InetAddress ip) {
        String str[] = ip.getHostAddress().split("\\.");
        return String.format("%03d", Integer.parseInt(str[0])) + "." +
            String.format("%03d", Integer.parseInt(str[1])) + "." +
            String.format("%03d", Integer.parseInt(str[2])) + "." +
            String.format("%03d", Integer.parseInt(str[3]));
    }
    @Override
    public String toString() {
        return id + " " + getFullIp(ip1) + " " + String.valueOf(port1) + " " + getFullIp(ip2) + " " + String.valueOf(port2);
    }

    public void getRegistered(BufferedReader br, PrintWriter pw){ // will try to register until REOK is received
        try{   
            pw.print("REGI "+toString()+"\n");
            pw.flush();
            String message = br.readLine();
            System.out.println("message received : "+message);
            if(!message.equals("REOK"))
                getRegistered(br, pw);
        }
        catch(Exception e){
            System.out.println("registration error");
            e.printStackTrace();
        }
    }

    public static ServerSocket connectToAvailablePort(int p) {
        try {
            ServerSocket server = new ServerSocket(p);
            return server;
        } catch (IOException e) {
            if (p == 1024)
                return connectToAvailablePort(9998);
            return connectToAvailablePort(p - 1);
        }
    }

    public static void main(String [] args){
        try{
            // assert args.length != 2;
            if (args.length < 2) {
                System.out.println("need id (8 char max) and handler port (4 char max)\n");
                System.exit(1);
            }
            int p = Integer.parseInt(args[1]);
            ServerSocket reception = connectToAvailablePort(9998); 
            Socket connexionToGestionnaire = new Socket("localhost", p);
            BufferedReader br = new BufferedReader(new InputStreamReader(connexionToGestionnaire.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(connexionToGestionnaire.getOutputStream()));
            Diffuser d = new Diffuser(args[0], reception.getInetAddress(), p,  reception.getInetAddress(), reception.getLocalPort()); // not ok broadcast ip and port needs to be changed
            d.getRegistered(br, pw); //Diffuser registration
            ImAlive ia = new ImAlive(br, pw);
            Thread imAliveCheck = new Thread(ia);
            imAliveCheck.start();
            while(true){
                Socket client = reception.accept();
                Service_Diffuser SD = new Service_Diffuser(client);
                System.out.println("New connection detected");
                Thread t = new Thread(SD);
                t.start();
            }
        }
        catch(Exception e){

        }
    }
}