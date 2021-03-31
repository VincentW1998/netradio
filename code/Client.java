import java.net.*;

public class Client{
    public static void main(String [] args){
        try{
            int p = Integer.parseInt(args[0]);
            String msgs [] = {
                "Le courage n'est pas l'absence de peur, mais la capacité de vaincre ce qui fait peur.",
                "La vie sans musique est tout simplement une erreur, une fatigue, un exil.",
                "Vis comme si tu devais mourir demain. Apprends comme si tu devais vivre toujours.",
                "Ce qui compte, chez un homme, ce n'est pas la couleur de sa peau ou la texture de sa chevelure, mais la texture et la qualité de son âme.",
                "La vie, c'est comme une bicyclette, il faut avancer pour ne pas perdre l'équilibre."
            };
            for(int x = 0 ; x < 20; x++){
                for(int i =0 ; i < 5 ; i++){
                    Socket client=new Socket("localhost",p);
                    String id =  String.format("%0$-" + 8 + "s", "RADIO"+String.valueOf(i)).replace(' ', '#');
                    ClientTest ct = new ClientTest(client,id, msgs[i]);
                    Thread t = new Thread(ct);
                    t.start();
                }
            }
        }
        catch (Exception e){
            // e.printStackTrace();
        }
    }
}