import java.text.Normalizer;
public class Message {
    private String id, data;

    public Message(String identifiant, String msg){
        id = identifiant;
        data = normalize(msg); //remove all accents 
    }
    public static String normalize (String s){
        String str = Normalizer.normalize(s, Normalizer.Form.NFD);
        str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        str = str.replace("’","'");
        return str;
    }
    
    @Override
    public String toString() {
        return id + " " + data;
    }

    public Message clone(){
        return new Message(id, data);
    }

    public String getId(){
        return id;
    }
}
