import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;

// Client should be acted as A

public class Client extends Thread {
    static String pw = "Ep1phanyFillTo16";

    public static void main(String[] args) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(pw.getBytes());
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
            aes.setKey(digest);
            byte[] pk_A = rsa.getPublicKey();
            System.out.println("DEBUG PK_A: " + pk_A);
            out.writeObject(aes.encrypt(pk_A));
            out.flush();
            // Receive 1: E_0(pw, E(pk_A, K_s))
            byte[] pw_cipher = (byte[]) in.readObject();
            // decrypt pw_cipher to get K_s
            byte[] K_s_cipher = aes.decrypt(pw_cipher);
            byte[] K_s = rsa.decrypt(K_s_cipher);
            // Generate N_A
            byte[] N_A = rsa.genRandomString(16).getBytes();
            // Send 2: E_1(K_s, N_A)
            des.setKey(K_s);
            out.writeObject(des.encrypt(N_A));
            out.flush();
            // Receive 2: E_1(K_s, Concat)
            byte[] Concat_cipher = (byte[]) in.readObject();
            // decrypt Concat_cipher to get Concat
            byte[] Concat = des.decrypt(Concat_cipher);
            // Verify N1 == NA
            byte[] N_1 = new byte[16];
            System.arraycopy(Concat, 0, N_1, 0, 16);
            if (N_1 == N_A) {
                System.out.println("Client Authentication succeeded");
                byte[] N_2 = new byte[16];
                System.arraycopy(Concat, 16, N_2, 0, 16);
                // Send 3: E_1(K_s, N_2)
                out.writeObject(des.encrypt(N_2));
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
