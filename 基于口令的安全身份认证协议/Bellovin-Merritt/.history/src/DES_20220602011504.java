import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class DES extends Algorithm {
    Key key;

    public DES() throws Exception {
        super("DES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        key = keyGenerator.generateKey();
    }

    synchronized public void setKey(byte[] key) {
        this.key = new SecretKeySpec(key, "DES");
    }

    public byte[] getKey() {
        return key.getEncoded();
    }

    synchronized public byte[] encrypt(byte[] text) {
        try {
            algorithm.init(Cipher.ENCRYPT_MODE, key);
            return algorithm.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public byte[] decrypt(byte[] cipher) {
        try {
            algorithm.init(Cipher.DECRYPT_MODE, key);
            return algorithm.doFinal(cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
