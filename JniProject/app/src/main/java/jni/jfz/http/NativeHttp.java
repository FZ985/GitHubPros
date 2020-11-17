package jni.jfz.http;

public class NativeHttp {

    private static NativeHttp nativeHttp;

    private NativeHttp() {
    }

    public static synchronized NativeHttp getInstance() {
        if (nativeHttp == null) {
            nativeHttp = new NativeHttp();
        }
        return nativeHttp;
    }

    static {
        System.loadLibrary("jfz_native_http");
    }

    public native void getNative(String url, NativeHttpCallback callback);
}
