public class Message {
    private String id, data;

    public Message(String identifiant, String msg){
        id = identifiant;
        data = msg;
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

    public String remove_hashtag(String mess) {
        int len = mess.length();
        while(mess.charAt(len-1) == '#') {
            len --;
        }
        return mess.substring(0, len);
    }
}
