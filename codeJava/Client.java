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
                "La vie, c'est comme une bicyclette, il faut avancer pour ne pas perdre l'équilibre.",
                "You’re off to great places, today is your day. Your mountain is waiting, so get on your way.",
                "You always pass failure on the way to success.",
                "No one is perfect - that’s why pencils have erasers.",
                "Winning doesn’t always mean being first. Winning means you’re doing better than you’ve done before.",
                "You’re braver than you believe, and stronger than you seem, and smarter than you think.",
                "It always seems impossible until it is done."
            };
            
            for(int x = 0 ; x < 1; x++){
                for(int i =0 ; i < msgs.length ; i++){
                    Socket client=new Socket("90.61.41.61",p);
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