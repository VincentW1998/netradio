public class DiffTest implements Runnable{
    String id, multidiffIp, gestPort;

    public DiffTest(String identifiant, String ipAddress, String port){
        id = identifiant;
        multidiffIp = ipAddress;
        gestPort = port;
    }

    public void run(){
        String args [] = new String [] { gestPort, id, multidiffIp };
        Diffuser.main(args);   
    }
}
