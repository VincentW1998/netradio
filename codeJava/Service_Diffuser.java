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
        if(request.length != 3){
            System.out.println("error in request");
            return;
        }
        if(request[1].getBytes().length != 8 || request[2].getBytes().length != 140){
            System.out.println("bad length for id or message");
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
        System.out.println("Message received - ACKM sent");
        pw.print("ACKM\r\n");
        pw.flush();
    }

    public void last(String request []){
        try {
            int nbMess = Integer.parseInt(request[1]);
            if(request.length != 2 || nbMess > 9999 || nbMess < 0){
                System.out.println("error in last message composition");
                return;
            }
            synchronized(msgs){
                System.out.println("--- " + msgs.size() + " messages ---");
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
            System.out.println("error in last message composition");
            e.printStackTrace();
            return;
        }
    }

    // Stock the path file into list and print this list
    public void stockFile() {
        try {
            String fileName = br.readLine();
            System.out.println("Received this file : " + fileName);
            fileName = Diffuser.fill_hashtag_or_zero(fileName, 25, "#");
            listFiles.add(fileName);
        }
        catch(Exception e) {
            System.out.println("Error readline");
        }
    }

    public void listFiles() {
        synchronized(listFiles){
            System.out.println("REQUEST : LISTFILES\r\n");
            try {
                pw.write("NBFI "+String.format("%02d", listFiles.size())+"\n");
                for(String fileName : listFiles) {
                    pw.write(fileName);
                }
                pw.flush();
                System.out.println("done");
            }
            catch (Exception e) {
                System.out.println("Error send listFiles !");
            }
        }
    }

    public String readline(){
        try{
        String str = "";
        char c = '\0';
        while(br.ready() || c != '\n'){
            c = (char) br.read();
            str += c;
        }
        return str;
        }
        catch(Exception e){
            return null;
        }
    }

    public boolean checkEnding(String message){
        if(! message.substring(message.length()-2, message.length()).equals("\r\n")){ // check if the message has \r\n at the end
            System.out.println("\\r and \\n missing");
            return false;
        }
        return true;
    }

    public void diff(){
        try{
            String message = readline();
            message = message.substring(0,message.length()-2);
            String str [] = message.split(" ",3);
            switch(str[0]){
                case "MESS":
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
