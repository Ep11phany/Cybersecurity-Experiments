import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA extends Algorithm {
    PublicKey publicKey;
    PrivateKey privateKey;

    public RSA(String algorithm) {
        super(algorithm);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
    }
}
