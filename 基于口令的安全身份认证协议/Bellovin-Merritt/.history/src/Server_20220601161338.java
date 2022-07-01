import java.net.Socket;
import java.net.ServerSocket;

public class Server {
    ServerSocket serverSocket;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}