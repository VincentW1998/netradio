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

    public static void main(String [] args) {

        try{
            DatagramSocket udpsocket = new DatagramSocket();
            byte[]data;
            while(true) {
                Thread.sleep(3000);
                String s="Hello\n";
                data=s.getBytes();
                InetSocketAddress ia = new InetSocketAddress("225.1.2.4",9998);
                DatagramPacket paquet = new DatagramPacket(data,data.length, ia);
                udpsocket.send(paquet);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}