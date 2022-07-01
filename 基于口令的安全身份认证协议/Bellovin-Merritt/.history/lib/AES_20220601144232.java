import javax.crypto.KeyGenerator;
import java.security.Key;

public class AES extends Algorithm {
    Key key;

    public AES() {
        super("AES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        key = keyGenerator.generateKey();
    }
}
