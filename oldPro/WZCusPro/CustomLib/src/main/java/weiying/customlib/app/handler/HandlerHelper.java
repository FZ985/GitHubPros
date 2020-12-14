package weiying.customlib.app.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 安全无内存泄漏handler
 * @author Administrator
 * 
 * @param <T>
 */
public abstract class HandlerHelper<T> extends Handler {
	protected WeakReference<T> acts;

	public HandlerHelper(T act, Callback callback) {
		super(callback);
		acts = new WeakReference<T>(act);
	}

	public HandlerHelper(T act) {
		super();
		acts = new WeakReference<T>(act);
	}

	public void handleMessage(Message msg) {
		final T act = acts.get();
		if (act != null) {
			if (act instanceof Activity) {
				Activity acty = (Activity) act;
				if (acty.isFinishing() || acty.isDestroyed()) {
					return;
				}
			}
			processTask(act, msg);
		}
	}

	public abstract void processTask(T act, Message msg);
}
