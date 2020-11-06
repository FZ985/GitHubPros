package com.jni.sdk;

public class SdkNative {
    private static SdkNative sdkNative;

    private SdkNative() {
    }

    public static SdkNative getInstance() {
        if (sdkNative == null) {
            synchronized (SdkNative.class) {
                if (sdkNative == null) {
                    sdkNative = new SdkNative();
                }
            }
        }
        return sdkNative;
    }

    static {
        System.loadLibrary("wy_sdk-lib");
    }

    public native String getStr();

    public native void updateValue();

    public native void onNativeConfig(OnNativeConfig call);

}
