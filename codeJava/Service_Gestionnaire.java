import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Service_Gestionnaire implements Runnable {
    private Socket client;
    private final int maxDiff = 99;
    private BufferedReader br;
    private PrintWriter pw;
    static LinkedList < Diffuser > register = new LinkedList < Diffuser > ();
    static LinkedList <String> listFiles = new LinkedList <String> ();

    

    public Service_Gestionnaire(Socket c) {
        client = c;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (Exception e) {
            System.out.println("Service constructor : Service_Gestionnaire.java");
            e.printStackTrace();
        }
    }

     public static boolean isUnique( String id, InetAddress MulticastIp){
        for (int i = 0 ; i < register.size(); i++){
            if( MulticastIp.equals( register.get(i).getIp1())){
                System.out.println("MulticastAddress is already used");
                return false;
            }
            if(id.equals( register.get(i).getId())){
                System.out.println("ID is already used");
                return false;
            }
        }
        return true;
    }

    public static boolean isGoodRegi(String mess[]) { // check if all arguments in REGI message is correct
        try {
            if (mess.length != 6 || mess[1].length() != 8 || mess[2].length() != 15 || mess[3].length() != 4 || mess[4].length() != 15 || mess[5].length() != 4){
                System.out.println("toto");
                return false;
            }

            InetAddress IP1 = InetAddress.getByName(mess[2]); // will return an UnkwonHostException if the ip is invalid
            InetAddress IP2 = InetAddress.getByName(mess[4]);
            int port1 = Integer.parseInt(mess[3]);
            int port2 = Integer.parseInt(mess[5]);
            return isUnique(mess[1], IP1) && port1 < 9999 && port1 >= 1024 && port2 < 9999 && port2 >= 1024;
        } catch (UnknownHostException ue) {
            System.out.println("unknownHostException");
            return false;
        }
    }

    public void Regi(String message) throws IOException {
        System.out.println("Request : REGI");
        Diffuser currDiff;
        synchronized (register) {
            String splitRegi[] = message.split(" ");
            if (!isGoodRegi(splitRegi) || register.size() >= maxDiff) { // check if the REGI message has the correct amount of arguments
                System.out.println("Issue with Regi message, connection closed");
                pw.print("RENO\r\n");
                pw.flush();
                client.close();
                return;
            }

            //shows the new diffuser info
            System.out.println("\tid : " + splitRegi[1] +
                               "\n\tMulticast Address : " + splitRegi[2] +
                               "\n\tMulticast Port: " + splitRegi[3] +
                               "\n\tIP address : " + splitRegi[4] +
                               "\n\tPort : " + splitRegi[5]); 
                                
            currDiff = new Diffuser(
                splitRegi[1],
                InetAddress.getByName(splitRegi[2]),
                Integer.parseInt(splitRegi[3]),
                InetAddress.getByName(splitRegi[4]),
                Integer.parseInt(splitRegi[5])
                );
            register.add(currDiff);
        }  
        pw.print("REOK\r\n");
        pw.flush();
        areUAlive(currDiff.getId());
        synchronized (register) {
            register.remove(currDiff);
        }
    }

    public void areUAlive(String id) { // check if the diffuser is still active
        try {
            String mess;
            while (true) {
                try{
                    mess = CompletableFuture.supplyAsync(() -> {
                        try {
                            pw.print("RUOK\r\n");
                            pw.flush();
                            String recv = br.readLine();
                            return recv;
                                    
                        } catch (Exception e) {
                            System.out.println("connexion interupted with diffuser " + id);
                            return null;
                        }
                    }).get(1, TimeUnit.SECONDS);
                    if (mess == null || !mess.equals("IMOK"))
                        throw new TimeoutException();
                }
                catch(TimeoutException te){
                    pw.flush();
                    System.out.println("connexion interupted with diffuser " + id);
                    return;
                }
            Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.out.println("areUalive error in Service_Gestionnaire");
            e.printStackTrace();
        }
    }


    // receipt file from client and write into directory Fichier/
    public void write_file() {
        try {
            String contenu;
            String pathFile= br.readLine();
            if(pathFile.equals("-CANCELED-")) { // read pathfile
                System.out.println("Sending file canceled by client");
                return;
            }             
            String [] token = pathFile.split("/");
            String fileName = token[token.length - 1];
            listFiles.add(fileName);

            BufferedWriter writer = new BufferedWriter(new FileWriter("../Fichier/" + fileName));
            while(!(contenu = br.readLine()).equals("-ENDFILE-")) {
                writer.write(contenu + "\n");
            }
            writer.close();
            System.out.println("file received !");
            pw.print("File received from register !\n");
            pw.flush();
            send_File_Diff(fileName);
          } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    // send file to all diffuser connect with this register
    public void send_File_Diff(String fileName) {
        for (Diffuser diffu : register) {
            try {
                Socket client = new Socket(diffu.getIp2(), diffu.getPort2());
                PrintWriter pwriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                pwriter.print("FILEREG\n");
                pwriter.print(fileName + '\n');
                pwriter.flush();
                pwriter.close();
                client.close();
            } catch (Exception e) {
                System.out.println("Service constructor : Service_Gestionnaire.java");
                e.printStackTrace();
            } 
        }
    }

    public void send_file_client(int idFile) {
        try {
            String contenu;
            String fileName = listFiles.get(idFile-1);
            String fileNameGood = Diffuser.fill_hashtag_or_zero(fileName, 30, "#");
            pw.print(fileNameGood);// send filename to client

            BufferedReader reader = new BufferedReader(new FileReader("../Fichier/" + fileName));

            while((contenu = reader.readLine()) != null) {
                String contenuGood = Diffuser.fill_hashtag_or_zero(contenu, 200, "#");
                pw.print(contenuGood); // send line by line
            }
            pw.flush();
            reader.close();
        }
        catch (Exception e) {
            System.out.println("Error read file !");
        }
    }

    public void client_handler(String message) {
        try {
            String [] dlist;
            if (message.equals("LIST")) {
                sendRegister();
            }
            if (message.equals("FILE")) {
                write_file();
            }
            if( (dlist = message.split(" "))[0].equals("DOWNLOAD")) {
                send_file_client(Integer.parseInt(dlist[1]));
            }
            return;
        } catch (Exception e) {
            System.out.println("client_handler :  Gestionnaire_clienthandler.java");
            e.printStackTrace();
        }
    }

    public void sendRegister() {
        synchronized (register){
            pw.print("LINB " + String.format("%02d", register.size()) + "\n");
            pw.flush(); 
            for (int i = 0; i < register.size(); i++) {
                pw.print("ITEM " + register.get(i).toString() + "\n");
                pw.flush();
            }
        }
    }


    public void gest() {
        try {
            String message = br.readLine();
            if (message.startsWith("REGI "))
                Regi(message);
            else
                client_handler(message);
        } catch (Exception e) {
        }
    }

    public void run() {
        try {
            gest();
            br.close();
            pw.close();
            client.close();
        } catch (Exception e) {
            System.out.println("run Service_Gestionnaire");
        }
    }
}