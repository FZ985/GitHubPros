package com.kmt.pro.http.interceptor;


import com.kmtlibs.app.utils.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.http.HttpHeaders.hasBody;

public class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        NONE,
        HEADERS,
        BODY
    }

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpLoggingInterceptor(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Level level = this.level;

        final Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> " + request.method() + ' ' + request.url().toString() + ' ' + protocol(protocol);
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        //发送请求信息时的打印信息,此处不必打印,因为请求返回的时候会打印请求信息
        //Logger.i(requestStartMessage);

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        StringBuilder respStr = new StringBuilder();
        respStr.append("-->")
                .append(response.code())
                .append(" ")
                .append(response.message())
                .append(", ")
                .append(response.request().url().toString().replace("&", "\n").replace("?", "\n"))
                .append(" (")
                .append(tookMs)
                .append("ms")
                .append(!logHeaders ? ", " + bodySize + " body" : "")
                .append(')');
        if (logHeaders) {
            if (!logBody || !hasBody(response)) {
                respStr.append("\n<----- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                respStr.append("\n<----- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (contentLength != 0) {
                    String json = buffer.clone().readString(charset);
                    respStr.append("\n结果:").append(json);
                }
                respStr.append("\n<----- END HTTP (").append(buffer.size()).append("-byte body)");
            }
        }
        Logger.e("JFZResult", "\n" + respStr.toString());
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

}
