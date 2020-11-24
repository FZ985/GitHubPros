package api29.libs.okhttp.tool;


import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpLoggerInterceptor implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        OkhttpUtil.log("OkHttpLoggerInterceptor", message);
    }
}
