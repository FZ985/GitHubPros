package com.kmtlibs.app.handler;

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

	public HandlerHelperAdapter(T act, Callback callback) {
		super(act, callback);
	}

	public void processTask(T act, Message msg) {

	}
}
