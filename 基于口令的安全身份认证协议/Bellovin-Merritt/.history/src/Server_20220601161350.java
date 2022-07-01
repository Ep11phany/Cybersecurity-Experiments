import java.net.Socket;
import java.net.ServerSocket;

public class Server extends Thread {
    ServerSocket serverSocket;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}