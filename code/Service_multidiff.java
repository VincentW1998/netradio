import java.net.*;
import java.util.*;

public class Service_multidiff implements Runnable{
    DatagramSocket dso;
    InetSocketAddress ia;
    static LinkedList <Message> msgs = new LinkedList <Message> ();

    public Service_multidiff(){
        try{
            dso = new DatagramSocket();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void add(Message mess) {
        msgs.add(mess);
    }

    public synchronized void remove(Message mess){
        for(int i = 0 ; i < msgs.size() ; i++){
            if(msgs.get(i).getId().equals(mess.getId()))
                msgs.remove(i);
        }
    }
    
    public void broadcast(int i){
        try{
            int n = -1;
            synchronized(msgs){
                if(msgs.size()>0){
                    n = i % msgs.size();
                    Message msg = msgs.get(n);
                    String str = "DIFF " + String.format("%04d", n) + " " + msg.toString() + "\r\n";
                    byte [] data = str.getBytes();
                    DatagramPacket paquet = new DatagramPacket(data, data.length, ia);
                    dso.send(paquet);
                }
            }
            Thread.sleep(4000);
            broadcast(n+1);
        }
        catch(Exception e){
            System.out.println("Broadcast error");
            e.printStackTrace();
        }
    } 
    public static String addressChecker (int addr [], int port){
        try{
            String iAddress = String.valueOf(addr[0]) + "." + String.valueOf(addr[1]) + "." + String.valueOf(addr[2]) + "." + String.valueOf(addr[3]);
            InetSocketAddress test = new InetSocketAddress(iAddress, port);        
            return iAddress;
        }
        catch(Exception e){
            if(addr[3] < 999) addr[3] ++;
              else if(addr[2] < 999) addr[2] ++;
                   else if(addr[1] < 999) addr[1]++;
                      else if(addr[0] < 239) addr[0]++;
                            else {
                                int loop [] = {224 ,000, 000, 001};
                                return addressChecker(loop, port);
                            }
            return addressChecker(addr, port);
        }
    }

    public void run(){
        int port = Diffuser.portLeft(9998);
        int addr [] = {224 ,000, 000, 001};
        String address = addressChecker(addr, port);
        ia = new InetSocketAddress(address, port);
        broadcast(0);
    }
}