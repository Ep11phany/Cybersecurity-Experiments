import javax.crpyto.Cipher;


public abstract class Algorithm {
    Cipher algorithm;

    public Algorithm(Cipher algorithm) {
        this.algorithm = algorithm;
    }
}
