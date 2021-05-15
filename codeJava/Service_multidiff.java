import java.net.*;
import java.util.*;

public class Service_multidiff implements Runnable{
    DatagramSocket dso;
    InetSocketAddress ia;
    static LinkedList <Message> msgs = new LinkedList <Message> ();

    public Service_multidiff(InetSocketAddress inetSock){
        try{
            dso = new DatagramSocket();
            ia = inetSock;
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
    
    public void broadcast(int i, int numMess){
        try{
            int n = 0;
            synchronized(msgs){
                if(msgs.size()>0){
                    n = i % msgs.size();
                    Message msg = msgs.get(n);
                    String str = "DIFF " + String.format("%04d", numMess) + " " + msg.toString() + "\r\n";
                    byte [] data = str.getBytes();
                    DatagramPacket paquet = new DatagramPacket(data, data.length, ia);
                    dso.send(paquet);
                    numMess = (numMess+1) % 10000;
                    n += 1;
                }
            }
            Thread.sleep(3000);
            broadcast(n, numMess);
        }
        catch(Exception e){
            System.out.println("Broadcast error");
            e.printStackTrace();
        }
    } 
    
    public void run(){
        broadcast(0, 0);
    }
}