import java.net.*;
import java.io.*;
import java.util.*;

public class Service_Diffuser implements Runnable{
    Socket client;
    BufferedReader br;
    PrintWriter pw;
    static LinkedList<Message> msgs = new LinkedList<Message> ();

    public Service_Diffuser(Socket c){
        try{
            client = c;
            br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(c.getOutputStream()));
        }
        catch(Exception e){

        }    
    }

    public void mess(String [] request){
        if(request.length != 3 || request[1].length() != 8 || request[2].length() != 140){
            System.out.println("error in request");
            return;
        }
        synchronized(msgs){
            if(msgs.size()>=99)
                msgs.removeFirst();      
            msgs.add(new Message(request[1], request[2]));
        }
        pw.print("ACKM\n");
        pw.flush();
    }

    public void last(String request []){
        try {
            int nbMess = Integer.parseInt(request[1]);
            if(request.length != 2 || nbMess > 999 || nbMess < 0){
                System.out.println("error in last message composition");
                return;
            }
            synchronized(msgs){
                System.out.println("--- " + msgs.size() + " messages ---");
                if(nbMess > msgs.size())
                    nbMess = msgs.size();
                for(int i=0; i<nbMess; i++){
                    pw.print("OLDM"+" "+String.valueOf(i+1)+" "+msgs.get(i).toString()+"\n");
                    pw.flush();
                }
            }
            pw.print("ENDM");
        }
        catch(Exception e){
            System.out.println("error in last message composition");
            e.printStackTrace();
            return;
        }
    }

    public void diff(){
        try{
            String message;
            while((message = br.readLine())==null);
            String str [] = message.split(" ",3);
            switch(str[0]){
                case "MESS":
                    mess(str);
                    return;

                case "LAST":
                    last(str);
                    return;

                default:
                    return;

            }

        }
        catch(Exception e){
            System.out.println("error in diff");
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            diff();
            pw.close();
            br.close();
            client.close();
        }
        catch(Exception e){

        }
    }
}
