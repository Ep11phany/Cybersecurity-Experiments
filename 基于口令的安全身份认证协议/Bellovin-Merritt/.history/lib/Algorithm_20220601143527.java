import javax.crpyto.Cipher;


public abstract class Algorithm {
    Cipher algorithm;

    public Algorithm(String algorithm) {
        this.algorithm = Cipher.getInstance(algorithm);
    }

    public byte[] encrypt(byte[] text) {}
    public byte[] decrypt(byte[] cipher) {}
}
