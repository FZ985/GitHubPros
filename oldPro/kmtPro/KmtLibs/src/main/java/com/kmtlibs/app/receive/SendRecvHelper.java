package com.kmtlibs.app.receive;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SendRecvHelper {
	/** 只是动作 */
	public static void send(Context mContext, String action) {
		Intent b = new Intent(action);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(b);
	}

	/**
	 * ---------------------<br/>
	 * 携带数据赛<br/>
	 * ---------------------<br/>
	 * new Intent(ACT).put..<br/>
	 * */
	public static void send(Context mContext, Intent b) {
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(b);
	}
}
