import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.KeyGenerator;

// Server should be acted as B

public class Server extends Thread {
    ServerSocket serverSocket;
    static String pw = "Ep1phanyFillTo16";

    public Server(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public String convert(byte[] data) {
        return new String(Base64.getEncoder().encodeToString(data));
    }

    public void run() throws Exception {
        System.out.println("Bellovin-Merritt Protocol server is running ...");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pw.getBytes("utf-8"));
        byte[] digest = md.digest();
        RSA rsa = new RSA();
        AES aes = new AES();
        DES des = new DES();
        while (true) {
            try {
                Socket server = serverSocket.accept();
                System.out.println("Accepted connection from " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                // Receive 1: Identity and E_0(pw, pk_A)
                String identity = (String) in.readObject();
                System.out.println("Received unverified identity: " + identity);
                byte[] pw_cipher = Base64.getDecoder().decode(in.readObject().toString());
                // decrypt pw_cipher to get pk_A
                // System.out.println("DEBUG pw_cipher: " + convert(pw_cipher));
                aes.setKey(digest);
                byte[] pk_A = aes.decrypt(pw_cipher);
                // System.out.println("DEBUG PK_A: " + convert(pk_A));
                // Generate K_s
                KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
                keyGenerator.init(56);
                Key K_s = keyGenerator.generateKey();
                rsa.setPublicKey(pk_A);
                // Send 1: E_0(pw, E(pk_A, K_s))
                out.writeObject(aes.encrypt(rsa.encrypt(K_s.getEncoded())));
                out.flush();
                // Receive 2: E_1(K_s, N_A)
                byte[] N_A_cipher = (byte[]) in.readObject();
                // decrypt N_A_cipher to get N_A    
                des.setKey(K_s.getEncoded());
                byte[] N_A = des.decrypt(N_A_cipher);
                // System.out.println("DEBUG N_A LENGTH: " + N_A.length);
                // System.out.println("DEBUG N_A: " + convert(N_A));
                // Create N_B
                byte[] N_B = rsa.genRandomString(16).getBytes();
                // System.out.println("DEBUG N_B: " + convert(N_B));
                byte[] Concat = new byte[N_A.length + N_B.length];
                System.arraycopy(N_A, 0, Concat, 0, N_A.length);
                System.arraycopy(N_B, 0, Concat, N_A.length, N_B.length);
                // Send 2: E_1(K_s, Concat)
                // System.out.println("DEBUG Concat: " + convert(Concat));
                out.writeObject(des.encrypt(Concat));
                out.flush();
                // Receive 3: E_1(K_s, N_2)
                byte[] N_2_cipher = (byte[]) in.readObject();
                // decrypt N_2_cipher to get N_2
                if (Arrays.equals(N_B, des.decrypt(N_2_cipher))) {
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