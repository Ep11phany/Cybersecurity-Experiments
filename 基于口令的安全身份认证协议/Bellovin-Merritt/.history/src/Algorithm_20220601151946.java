import javax.crypto.Cipher;


public abstract class Algorithm {
    Cipher algorithm;

    public Algorithm(String algorithm) throws Exception {
        this.algorithm = Cipher.getInstance(algorithm);
    }
}
