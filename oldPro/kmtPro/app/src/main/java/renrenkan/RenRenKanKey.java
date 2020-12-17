package renrenkan;


public class RenRenKanKey {

    private static RenRenKanKey key;

    static {
        System.loadLibrary("renrenkankey");
    }

    private RenRenKanKey() {
    }

    public static RenRenKanKey get() {
        if (key == null) {
            synchronized (RenRenKanKey.class) {
                if (key == null) {
                    key = new RenRenKanKey();
                }
            }
        }
        return key;
    }

    public native String getAppId();

    public native String getInterfaceKeyToxiaofei();

    public native String getInterfaceKeyIvToxiaofei();

    public native String getInterfaceKeyToyanshen();

    public native String getInterfaceKeyIvToyanshen();
}
