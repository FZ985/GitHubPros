package jni.jfz.http;

public interface NativeHttpCallback {

    void onSuccess(String data);

    void onError(String msg);

}
