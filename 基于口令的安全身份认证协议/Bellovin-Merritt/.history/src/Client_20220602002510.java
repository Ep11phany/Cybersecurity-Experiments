import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Client should be acted as A

public class Client extends Thread {
    String pw = "Ep1phanyFillTo16";

    public static void main(String[] args) {
        try {
            RSA rsa = new RSA();
            AES aes = new AES();
            DES des = new DES();
            
            Socket client = new Socket("localhost", 7654);
            System.out.println("Connected to " + client.getRemoteSocketAddress());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
