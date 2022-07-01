import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA extends Algorithm {
    PublicKey publicKey;
    PrivateKey privateKey;

    public RSA(String algorithm) {
        super(algorithm);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void setPublicKey(byte[] key) {
        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
    }

    public void setPrivateKey(byte[] key) {
        privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key));
    }

    public byte[] getPublicKey() {
        return publicKey.getEncoded();
    }

    public byte[] encrypt(byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public


}
