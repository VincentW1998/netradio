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

    public static boolean isGoodRegi(String mess[]) { // check if all arguments in REGI message is correct
        try {
            if (mess.length != 6 || mess[1].length() != 8 || mess[2].length() != 15 || mess[3].length() != 4 || mess[4].length() != 15 || mess[5].length() != 4){
                System.out.println("toto");
                return false;
            }

            InetAddress.getByName(mess[2]); // will return an UnkwonHostException if the ip is invalid
            InetAddress.getByName(mess[4]);
            int port1 = Integer.parseInt(mess[3]);
            int port2 = Integer.parseInt(mess[5]);
            return port1 < 9999 && port1 >= 1024 && port2 < 9999 && port2 >= 1024;
        } catch (UnknownHostException ue) {
            System.out.println("unknownHostException");
            return false;
        }
    }

    public void Regi(String message) throws IOException {
        Diffuser currDiff;
        synchronized (register) {
            String splitRegi[] = message.split(" ");
            if (!isGoodRegi(splitRegi) || register.size() >= maxDiff) { // check if the REGI message has the correct amount of arguments
                System.out.println("Issue with Regi message, connection closed");
                pw.print("RENO\n");
                pw.flush();
                client.close();
                return;
            }
            currDiff = new Diffuser(
                splitRegi[1] ,
                InetAddress.getByName(splitRegi[2]),
                Integer.parseInt(splitRegi[3]),
                InetAddress.getByName(splitRegi[4]),
                Integer.parseInt(splitRegi[5])
                );
            register.add(currDiff);
        }
            pw.print("REOK\n");
            pw.flush();
            areUAlive();
        synchronized (register) {
            register.remove(currDiff);
        }
        
    }

    public void areUAlive() { // check if the diffuser is still active
        try {
            String mess;
            while (true) {
                try{
                    mess = CompletableFuture.supplyAsync(() -> {
                        try {
                            pw.print("RUOK\n");
                            pw.flush();
                            return br.readLine();
                                    
                        } catch (Exception e) {
                            System.out.println("connexion interupted with diffuser");
                            return null;
                        }
                    }).get(1, TimeUnit.SECONDS);
                }
                catch(TimeoutException te){
                    pw.flush();
                    System.out.println("time out diffuser released");
                    return;
                }
            
            if (mess == null || !mess.equals("IMOK"))
                return;
            // System.out.print("received !\n");
            Thread.sleep(5000);
                
            }
        } catch (Exception e) {
            System.out.println("areUalive error in Service_Gestionnaire");
            e.printStackTrace();
        }
    }

    public void write_file() {
        
        try {
            String nameFile = br.readLine();
            String contenu;
            BufferedWriter writer = new BufferedWriter(new FileWriter("Fichier/" + nameFile));
            while(!(contenu = br.readLine()).equals("")) {
                writer.write(contenu + "\n");
            }

            System.out.println("file sent");
            pw.print("File received from register !\n");
            pw.flush();
            writer.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public void client_handler(String message) {
        try {
            if (message.equals("LIST")) {
                sendRegister();
            }
            if (message.equals("FILE")) {
                write_file();
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
            System.out.println(message);
            if (message.startsWith("REGI "))
                Regi(message);
            else
                client_handler(message);

        } catch (Exception e) {
            System.out.println("issue in gest() : Service_Gestionnaire");
            e.printStackTrace();
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