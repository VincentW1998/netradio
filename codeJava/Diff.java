public class Diff {
    public static void main(String [] args){
        for(int i =0 ; i < 5; i++){
            String id =  String.format("%0$-" + 8 + "s", "RADIO"+String.valueOf(i)).replace(' ', '#');
            String ip = "225.0.0.";
            ip += String.format("%03d", i);
            DiffTest dt = new DiffTest(id, ip, args[0]);
            Thread t = new Thread(dt);
            t.start();
        }
    }
}
