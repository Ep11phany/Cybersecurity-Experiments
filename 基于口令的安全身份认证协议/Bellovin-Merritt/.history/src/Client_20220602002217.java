import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Client should be acted as A

public class Client extends Thread {
    String pw = "Ep1phanyFillTo16";

    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 7654);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
