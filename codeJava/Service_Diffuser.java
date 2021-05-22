import java.net.*;
import java.io.*;
import java.util.*;

public class Service_Diffuser implements Runnable{
    Socket client;
    Service_multidiff sm;
    BufferedReader br;
    PrintWriter pw;
    LinkedList<Message> msgs = new LinkedList<Message> ();
    LinkedList<String> listFiles = new LinkedList<String>();

    public Service_Diffuser(Socket c, Service_multidiff servM, LinkedList<Message> m, LinkedList <String> lf){
        try{
            client = c;
            sm = servM;
            br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(c.getOutputStream()));
            msgs = m;
            listFiles = lf;
        }
        catch(Exception e){
            e.printStackTrace();
        }    
    }

    public void mess(String [] request){
        if(request.length != 3 ){
            System.out.println("\terror in request");
            return;
        }
        if(request[1].getBytes().length != 8 || request[2].getBytes().length != 140){
            System.out.println("\tbad length for id or message");
            return;
        }
        synchronized(msgs){
            if(msgs.size()>=99){
                sm.remove(msgs.getFirst());
                msgs.removeFirst();
            }
            Message msg = new Message(request[1], request[2]);     
            msgs.add(msg);
            sm.add(msg);
        }
        System.out.println("\tMessage received - ACKM sent");
        pw.print("ACKM\r\n");
        pw.flush();
    }

    public void last(String request []){
        try {
            int nbMess = Integer.parseInt(request[1]);
            if(request.length != 2 || nbMess > 9999 || nbMess < 0){
                System.out.println("\terror in last message composition");
                return;
            }
            synchronized(msgs){
                System.out.println("\tContient " + msgs.size() + " messages");
                if(nbMess > msgs.size())
                    nbMess = msgs.size();
                for(int i=0; i<nbMess; i++){
                    String nb_with_zero = String.format("%04d", i+1);
                    pw.print("OLDM " + nb_with_zero +" "+msgs.get(i).toString()+"\r\n");
                    pw.flush();
                }
            }
            pw.print("ENDM\r\n");
        }
        catch(Exception e){
            System.out.println("\terror in last message composition");
            e.printStackTrace();
            return;
        }
    }

    // Stock the path file into list and print this list
    public void stockFile() {
        try{
            String fileName = br.readLine();
            System.out.println("\tReceived this file : " + fileName);
            fileName = Diffuser.fill_hashtag_or_zero(fileName, 25, "#");
            listFiles.add(fileName);
        }
        catch(Exception e){
            System.out.println("stockFile error");
        }
    }

    public void listFiles() {
        synchronized(listFiles){
            try {
                pw.write("NBFI "+String.format("%02d", listFiles.size())+"\r\n");
                for(String fileName : listFiles) {
                    pw.write(fileName);
                }
                pw.flush();
                System.out.println("\tdone");
            }
            catch (Exception e) {
                System.out.println("\tError send listFiles !");
            }
        }
    }

    public void diff(){
        try{
            String message = manager.readline(br);
            if(message.length() < 2){
                System.out.println("Unkown request : aborted");
                return;
            }
            if(!manager.checkEnding(message))
                return;
            message = message.substring(0,message.length()-2);
            String str [] = message.split(" ",2);
            System.out.println("Request : " + str[0]);
            switch(str[0]){
                case "MESS":  
                    str = message.split(" ",3);
                    mess(str);
                    return;
                case "LAST":
                    last(str);
                    return;
                case "FILEREG" :
                    stockFile();
                    break;
                case "LISTFILES":
                    listFiles();
                    break;
                default:
                    System.out.println("Unkown request : aborted");
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
            System.out.println("error in run : Service_Diffuser.java");
            e.printStackTrace();
        }
    }
}
