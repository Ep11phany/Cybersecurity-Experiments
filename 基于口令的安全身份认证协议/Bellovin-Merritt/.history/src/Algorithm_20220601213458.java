import javax.crypto.Cipher;


public abstract class Algorithm {
    Cipher algorithm;

    public Algorithm(String algorithm) throws Exception {
        this.algorithm = Cipher.getInstance(algorithm);
    }

    public String genRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }
}
