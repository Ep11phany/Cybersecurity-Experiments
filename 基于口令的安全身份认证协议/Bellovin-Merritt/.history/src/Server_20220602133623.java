import java.net.Socket;
import java.security.PrivateKey;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.util.Base64;

// Server should be acted as B

public class Server extends Thread {
    ServerSocket serverSocket;
    String pw = "Ep1phanyFillTo16";

    public Server(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {

        while (true) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(pw.getBytes("utf-8"));
                System.out.println("DEBUG DIGEST: " + digest);
                RSA rsa = new RSA();
                AES aes = new AES();
                DES des = new DES();
                Socket server = serverSocket.accept();
                System.out.println("Accepted connection from " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                // Receive 1: Identity and E_0(pw, pk_A)
                String identity = (String) in.readObject();
                System.out.println("Received unverified identity: " + identity);
                byte[] pw_cipher = Base64.getDecoder().decode(in.readObject().toString());
                // decrypt pw_cipher to get pk_A
                System.out.println("DEBUG pw_cipher: " + pw_cipher);
                aes.setKey(digest);
                byte[] pk_A = aes.decrypt(pw_cipher);
                System.out.println("DEBUG PK_A: " + pk_A);
                // Generate K_s
                PrivateKey K_s = rsa.genPrivateKey();
                rsa.setPublicKey(pk_A);
                System.out.println(K_s.getEncoded().length);
                // Send 1: E_0(pw, E(pk_A, K_s))
                out.writeObject(aes.encrypt(rsa.encrypt(K_s.getEncoded())));
                out.flush();
                // Receive 2: E_1(K_s, N_A)
                byte[] N_A_cipher = (byte[]) in.readObject();
                // decrypt N_A_cipher to get N_A    
                des.setKey(K_s.getEncoded());
                byte[] N_A = des.decrypt(N_A_cipher);
                // Create N_B
                byte[] N_B = rsa.genRandomString(16).getBytes();
                byte[] Concat = new byte[N_A.length + N_B.length];
                System.arraycopy(N_A, 0, Concat, 0, N_A.length);
                System.arraycopy(N_B, 0, Concat, N_A.length, N_B.length);
                // Send 2: E_1(K_s, Concat)
                out.writeObject(des.encrypt(Concat));
                out.flush();
                // Receive 3: E_1(K_s, N_2)
                byte[] N_2_cipher = (byte[]) in.readObject();
                // decrypt N_2_cipher to get N_2
                if (N_B == des.decrypt(N_2_cipher)) {
                    System.out.println("Server Authentication succeeded, Client identity: " + identity);
                } else {
                    System.out.println("Server Authentication failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t = new Server(7654);
        t.start();
    }
}