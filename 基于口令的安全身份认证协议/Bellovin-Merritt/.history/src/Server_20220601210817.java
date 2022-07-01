import java.net.Socket;
import java.io.DataInputStream;
import java.net.ServerSocket;

// Server should be acted as B

public class Server extends Thread {
    ServerSocket serverSocket;

    public Server(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {

        while (true) {
            try {
                Socket server = serverSocket.accept();
                System.out.println("Accepted connection from " + server.getRemoteSocketAddress());
                // 1: Identity and E_0(pw, pk_A)
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
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