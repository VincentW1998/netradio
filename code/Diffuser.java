import java.net.*;
import java.io.*;

public class Diffuser {
    private String id;
    private InetAddress ip1, ip2;
    private int port1, port2;

    public Diffuser(String identifiant, InetAddress ipAdress1, int p1, InetAddress ipAdress2, int p2){
        id = identifiant;
        ip1 = ipAdress1;
        ip2 = ipAdress2;
        port1 = p1;
        port2 = p2;
    }

    @Override
    public String toString() {
        return id+" "+ip1.toString()+" "+String.valueOf(port1)+" "+ip2.toString() +" "+ String.valueOf(port2);
    }
}
