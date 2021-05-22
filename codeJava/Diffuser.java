import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Diffuser {
    private String id;
    private InetAddress ip1, ip2; // ip1 : multicast -- ip2 : normal
    private int port1, port2; // port1 : multicast -- port2 : normal

    public Diffuser(String identifiant, InetAddress ipAdress1, int p1, InetAddress ipAdress2, int p2) {
        id = identifiant;
        ip1 = ipAdress1;
        ip2 = ipAdress2;
        port1 = p1;
        port2 = p2;
    }

    public String getId(){
        return id;
    }
    public InetAddress getIp1(){
        return ip1;
    }
    public int getPort1(){
        return port1;
    }
    public InetAddress getIp2() {
        return ip2;
    }
    public int getPort2() {
        return port2;
    }

    @Override
    public String toString() {
        return id + " " + getFullIp(ip1) + " " + String.valueOf(port1) + " " + getFullIp(ip2) + " " + String.valueOf(port2);
    }

    public boolean getRegistered(BufferedReader br, PrintWriter pw){ // will try to register until REOK is received
        try{   
            pw.print("REGI "+toString()+"\r\n");
            pw.flush();
            String message = manager.readline(br);
            if(!message.equals("REOK\r\n"))
                return false;
            System.out.println("Successfully registered");
            return true;
        }
        catch(Exception e){
            System.out.println("registration error");
            e.printStackTrace();
            return false;
        }
    }

    public static String getFullIp(InetAddress ip) {
        String str[] = ip.getHostAddress().split("\\.");
        return String.format("%03d", Integer.parseInt(str[0])) + "." +
            String.format("%03d", Integer.parseInt(str[1])) + "." +
            String.format("%03d", Integer.parseInt(str[2])) + "." +
            String.format("%03d", Integer.parseInt(str[3]));
    }

    public static int portLeft(int p) {
        try{
            ServerSocket reception = new ServerSocket(p);
            reception.close();
            return p;
        } catch (IOException e) {
            if (p == 1024){
                return portLeft(9998);
            }
            return portLeft(p - 1);
        }
    } 

    public static String addressChecker(int port){
        try{
            Scanner ask = new Scanner(System.in);
            System.out.print("Enter Multicast IP (224-239): ");
            String res = ask.nextLine();
            InetAddress address = InetAddress.getByName(res);
            if(!address.isMulticastAddress())
                throw new Exception();
            return res;
        }
        catch(Exception e){
            System.out.println("invalid Multicast IP");
            return addressChecker(port);
        }
    }

    public static String askID(){
        Scanner ask = new Scanner(System.in);
        System.out.print("Enter ID (max 8 char): ");
        String res = ask.nextLine();
        if(res.length() > 8){
            System.out.println("ID too long");
            return askID();
        }
        if(!(res.length() > 0)){
            System.out.println("ID too short");
            return askID();
        }
        return res;
    }

    public static String askIP(int port){
        try{
            Scanner ask = new Scanner(System.in);
            System.out.print("Enter IP : ");
            String res = ask.nextLine();
            InetAddress address = InetAddress.getByName(res);
            return res;
        }
        catch(Exception e){
            System.out.println("IP");
            return addressChecker(port);
        }

    }

    public static String fill_hashtag_or_zero(String str, int len, String symbol) {
        String tmp = "";
        int length = str.length();
        int complete_len = len - length;
        if (complete_len > 0) {
            if(symbol.equals("#")) {
                tmp += str;
            }
            for(int i = 0; i < complete_len; i++) {
                tmp += symbol;
            }
            if(symbol.equals("0")) {
                tmp += str;
            }
            return tmp;
        }
        return str; 
    }

    public static void main(String [] args){
        try{
            assert(args.length != 1 || args.length != 4);
            int portGestionnaire = Integer.parseInt(args[0]);
            ServerSocket reception = Gestionnaire.connectToAvailablePort(9998); //port reception
            Socket connexionToGestionnaire;
            BufferedReader br;
            PrintWriter pw;
            Diffuser diffuser;
            String ip;
            if(args.length == 1)
                ip = askIP(portGestionnaire);
            else 
                ip = args[3];
            while(true){ // check if the given id and multiDiff adrress are valid
                connexionToGestionnaire =  new Socket(ip, portGestionnaire);
                br = new BufferedReader(new InputStreamReader(connexionToGestionnaire.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(connexionToGestionnaire.getOutputStream()));
                int portMultiDiff = portLeft(9998);
                String id, multicastIP;
                if(args.length == 4){
                    id = fill_hashtag_or_zero(args[1], 8, "#");
                    multicastIP = args[2];
                }
                else{
                    id = fill_hashtag_or_zero(askID(), 8, "#");
                    multicastIP = addressChecker(portMultiDiff);
                }
                diffuser = new Diffuser(id, InetAddress.getByName(multicastIP), portMultiDiff,  reception.getInetAddress(), reception.getLocalPort());
                if(diffuser.getRegistered(br, pw))
                    break;
            }
            
            // start the multidiffusion
            InetSocketAddress inetAddress = new InetSocketAddress(diffuser.ip1, diffuser.port1); 
            Service_multidiff sm = new Service_multidiff(inetAddress);
            Thread tsm = new Thread(sm);
            tsm.start();

            // start the automatic response to gestionnaire
            ImAlive ia = new ImAlive(br, pw);
            Thread imAliveCheck = new Thread(ia);
            imAliveCheck.start();

            LinkedList<Message> msgs = new LinkedList<Message> ();
            LinkedList<String> listFiles = new LinkedList<String>();

            // client treatment
            while (true){ 
                Socket client = reception.accept();
                Service_Diffuser SD = new Service_Diffuser(client, sm, msgs, listFiles);
                // System.out.println("New connection detected");
                Thread t = new Thread(SD);
                t.start();
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}