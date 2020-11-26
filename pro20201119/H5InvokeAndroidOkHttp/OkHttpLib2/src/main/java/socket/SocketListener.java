package socket;

import okhttp3.WebSocketListener;

public interface SocketListener {

    void build(WebSocketListener listener);
}