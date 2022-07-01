import javax.crypto.Cipher;


public abstract class Algorithm {
    Cipher algorithm;

    public Algorithm(String algorithm) {
        this.algorithm = Cipher.getInstance(algorithm);
    }

    public byte[] encrypt(String text) {}
    public byte[] decrypt(String cipher) {}
}
