import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class DES extends Algorithm {
    Key key;

    public DES() {
        super("DES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        key = keyGenerator.generateKey();
    }

    public void setKey(byte[] key) {
        this.key = new SecretKeySpec(key, "DES");
    }

    public byte[] getKey() {
        return key.getEncoded();
    }

    public byte[] encrypt(String text) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(String cipher) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipher.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
