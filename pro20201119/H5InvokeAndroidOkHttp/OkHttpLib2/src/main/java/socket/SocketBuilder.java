package socket;

import com.okhttplib2.config.RequestBuilder;

public class SocketBuilder extends RequestBuilder {

    public SocketBuilder(int requestWay, int requestType) {
        super(requestWay, requestType);
    }

    public SocketBuilder(String requestUrl, int requestWay, int requestType) {
        super(requestUrl, requestWay, requestType);
    }

    private void createSocket(){
        new SocketRequestImpl(build());
    }

}