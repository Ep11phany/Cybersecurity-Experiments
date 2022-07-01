import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            // Send 1: Identity and E_0(pw, pk_A)
            out.writeObject(identity);
            out.flush();
            aes.setKey(pw.getBytes());
            byte[] pk_A = rsa.getPublicKey();
            out.writeObject(aes.encrypt(pk_A.toString()).toString());
            out.flush();
            // Receive 1: E_0(pw, E(pk_A, K_s))
            String pw_cipher = (String) in.readObject();
            // decrypt pw_cipher to get K_s
            String K_s_cipher = new String(aes.decrypt(pw_cipher));
            String K_s = new String(rsa.decrypt(K_s_cipher));
            // Generate N_A
            String N_A = rsa.genRandomString(16);
            // Send 2: E_1(K_s, N_A)
            des.setKey(K_s.getBytes());
            out.writeObject(des.encrypt(N_A).toString());
            out.flush();
            // Receive 2: E_1(K_s, Concat)
            String Concat_cipher = (String) in.readObject();
            // decrypt Concat_cipher to get Concat
            String Concat = new String(des.decrypt(Concat_cipher));
            // Verify N1 == NA
            if (Concat.substring(0, 16).equals(N_A)) {
                System.out.println("Client Authentication succeeded");
                String N2 = Concat.substring(16);
                // Send 3: E_1(K_s, N_2)
                out.writeObject(des.encrypt(N2));
                out.flush();
            } else {
                System.out.println("Client Authentication failed");
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
