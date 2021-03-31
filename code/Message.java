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

}
