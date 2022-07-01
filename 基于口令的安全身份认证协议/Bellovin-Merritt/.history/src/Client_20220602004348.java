import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;

// Client should be acted as A

public class Client extends Thread {
    static String pw = "Ep1phanyFillTo16";

    public static void main(String[] args) {
        try {
            String identity = args[0];
            RSA rsa = new RSA();
            AES aes = new AES();
            DES des = new DES();
            Socket client = new Socket("localhost", 7654);
            System.out.println("Connected to " + client.getRemoteSocketAddress());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            // Send 1: Identity and E_0(pw, pk_A)
            out.writeObject(identity);
            out.flush();
            aes.setKey(pw.getBytes());
            byte[] pk_A = rsa.getPublicKey();
            out.writeObject(aes.encrypt(pk_A.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
