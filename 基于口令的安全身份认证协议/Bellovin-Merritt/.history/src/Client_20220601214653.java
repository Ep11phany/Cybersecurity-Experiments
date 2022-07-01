import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Client should be acted as A

public class Client extends Thread {
    String pw = "Ep1phanyFillTo16";

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
