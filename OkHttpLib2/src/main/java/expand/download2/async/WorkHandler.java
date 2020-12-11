package expand.download2.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public class WorkHandler {
    private static final String DEFAULT_NAME = "handler-thread";
    private HandlerThread mHandlerThread;
    private Handler mWorkHandler;

    public WorkHandler() {
        this(DEFAULT_NAME);
    }

    public WorkHandler(String name) {
        mHandlerThread = new HandlerThread(TextUtils.isEmpty(name) ? DEFAULT_NAME : name) {
            @Override
            protected void onLooperPrepared() {
                WorkHandler.this.onPrepared();
            }
        };

        mHandlerThread.start();
        mWorkHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                onHandleMessage(msg);
            }
        };
    }

    protected void onPrepared() {

    }

    protected void onHandleMessage(Message msg) {

    }

    public void dispatchMessage(Message msg) {
        mWorkHandler.dispatchMessage(msg);
    }

    public void post(Runnable runnable) {
        mWorkHandler.post(runnable);
    }

    public void postAtFrontOfQueue(Runnable runnable) {
        mWorkHandler.postAtFrontOfQueue(runnable);
    }

    public void postAtTime(Runnable runnable, long uptimeMills) {
        mWorkHandler.postAtTime(runnable, uptimeMills);
    }

    public void postAtTime(Runnable runnable, Object token, long uptimeMills) {
        mWorkHandler.postAtTime(runnable, token, uptimeMills);
    }

    public void postDelay(Runnable runnable, long delayInMills) {
        mWorkHandler.postDelayed(runnable, delayInMills);
    }

    public boolean hasMessage(int what) {
        return mWorkHandler.hasMessages(what);
    }

    public boolean hasMessage(int what, Object object) {
        return mWorkHandler.hasMessages(what, object);
    }

    public Message obtainMessage() {
        return mWorkHandler.obtainMessage();
    }

    public Message obtainMessage(int what) {
        return mWorkHandler.obtainMessage(what);
    }

    public Message obtainMessage(int what, int arg1, int arg2) {
        return mWorkHandler.obtainMessage(what, arg1, arg2);
    }

    public Message obtainMessage(int what, int arg1, int arg2, Object object) {
        return mWorkHandler.obtainMessage(what, arg1, arg2, object);
    }

    public Message obtainMessage(int what, Object object) {
        return mWorkHandler.obtainMessage(what, object);
    }

    public void sendMessage(Message message) {
        mWorkHandler.sendMessage(message);
    }

    public void sendEmptyMessage(int what) {
        mWorkHandler.sendEmptyMessage(what);
    }

    public void sendEmptyMessageDelayed(int what, long delayMills) {
        mWorkHandler.sendEmptyMessageDelayed(what, delayMills);
    }

    public void sendEmptyMessageAtTime(int what, long uptimeMills) {
        mWorkHandler.sendEmptyMessageAtTime(what, uptimeMills);
    }

    public void sendMessageAtFrontOfQueue(Message message) {
        mWorkHandler.sendMessageAtFrontOfQueue(message);
    }

    public void sendMessageDelayed(Message message, long delayMills) {
        mWorkHandler.sendMessageDelayed(message, delayMills);
    }

    public void sendMessageAtTime(Message message, long uptimeMills) {
        mWorkHandler.sendMessageAtTime(message, uptimeMills);
    }

    public void removeMessages(int what) {
        mWorkHandler.removeMessages(what);
    }

    public void removeMessages(int what, Object object) {
        mWorkHandler.removeMessages(what, object);
    }

    public void removeCallbacks(Runnable runnable) {
        mWorkHandler.removeCallbacks(runnable);
    }

    public void removeCallbacks(Runnable runnable, Object token) {
        mWorkHandler.removeCallbacks(runnable, token);
    }

    public void close() {
        mHandlerThread.quit();
    }

}
