import java.net.Socket;
import java.security.PrivateKey;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

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
                String pw_cipher = (String) in.readObject();
                // decrypt pw_cipher to get pk_A
                aes.setKey(pw.getBytes());
                String pk_A = new String(aes.decrypt(pw));
                // Generate K_s
                PrivateKey K_s = rsa.genPrivateKey();
                rsa.setPublicKey(pk_A.getBytes());
                // Send 1: E_0(pw, E(pk_A, K_s))
                out.writeObject(aes.encrypt(rsa.encrypt(K_s.getEncoded().toString()).toString()));
                out.flush();
                // Receive 2: E_1(pw, E(pk_A, K_s))
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