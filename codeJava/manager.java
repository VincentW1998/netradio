import java.io.*;
import java.net.*;

public class manager {
    public static String readline(BufferedReader br) throws IOException {
        try{
            String str = "";
            char c = '\0';
            while( c != '\n'){
                c = (char) br.read();
                str += c;
            }
            return str;
        }
        catch(IOException e){
            throw e;
        }
    }

     // check whether the string has \r\n at the end
     public static boolean checkEnding(String message){
        if(! message.substring(message.length()-2, message.length()).equals("\r\n")){ // check if the message has \r\n at the end
            System.out.println("[\\r\\n] missing at the end of request :\t request aborted");
            return false;
        }
        return true;
    }



}
