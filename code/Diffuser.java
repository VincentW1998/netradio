import java.net.*;
import java.io.*;

public class Diffuser {
    private String id;
    private InetAddress ip1, ip2;
    private int port1, port2;
    private Socket gestionnaire;
    // private BufferedReader br;
    // private PrintWriter pw;

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

    public void getRegistered(Socket gestionnaire){ // will try to register until REOK is received
        try{   
            BufferedReader br = new BufferedReader(new InputStreamReader(gestionnaire.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(gestionnaire.getOutputStream()));
            pw.print("REGI "+toString()+"\n");
            pw.flush();
            String message;
            while((message = br.readLine()) == null);
            System.out.println("message received : "+message);
            if(message.equals("REOK"))
                return;
            getRegistered(gestionnaire);
            // message = br.readLine();
            // System.out.println(message);
            // System.out.println("finished");
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
            assert args.length != 2;
            int p = Integer.parseInt(args[1]);
            ServerSocket server = connectToAvailablePort(9998);
            Socket diff_gest = new Socket("localhost", p);
            Diffuser d = new Diffuser(args[0], server.getInetAddress(), p,  server.getInetAddress(), server.getLocalPort()); // not ok broadcast ip and port needs to be changed
            d.getRegistered(diff_gest);
            ImAlive ia = new ImAlive(diff_gest);
            Thread imAliveCheck = new Thread(ia);
            imAliveCheck.start();
            while(true){
                Socket client = server.accept();
                Service_Diffuser SD = new Service_Diffuser(client);
                System.out.println("New connection detected");
                Thread t = new Thread(SD);
                t.start();
            }


        }
        catch(Exception e){

        }
    }

    // public static void main(String [] args) {

    //     try{
    //         DatagramSocket udpsocket = new DatagramSocket();
    //         byte[]data;
    //         for(int i=0;i <= 10; i++){
    //             String s="MESSAGE "+i+" \n";
    //             data=s.getBytes();
    //             InetSocketAddress ia = new InetSocketAddress("localhost",9998);
    //             DatagramPacket paquet = new DatagramPacket(data,data.length, ia);
    //             udpsocket.send(paquet);
    //         }
    //         udpsocket.close();
    //     } catch(Exception e){
    //         e.printStackTrace();
    //     }
    // }
}