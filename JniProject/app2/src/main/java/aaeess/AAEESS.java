package aaeess;

public class AAEESS {

    private static AAEESS aes;

    private AAEESS(){}

    static {
        System.loadLibrary("aaeess-lib");
    }

    public synchronized static AAEESS getInstance(){
        if (aes == null){
            aes = new AAEESS();
        }
        return aes;
    }

    public native String pass();
    public native String passIv();
}