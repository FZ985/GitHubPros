package weiying.customlib.app.handler;

import android.os.Handler;
import android.os.Message;

/**
 * 安全无内存泄漏handler
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public class HandlerHelperAdapter<T> extends HandlerHelper<T> {
	public HandlerHelperAdapter(T act) {
		super(act);
	}

	public HandlerHelperAdapter(T act, Handler.Callback callback) {
		super(act, callback);
	}

	public void processTask(T act, Message msg) {

	}
}
