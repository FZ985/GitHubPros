package okhttp.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.text.TextUtils;

import java.util.TimerTask;

/**
 * 网速工具类<br/>
 */
public class NetWorkSpeedUtil {
	private Context mContext;
	private String speedHistory = "";

	public NetWorkSpeedUtil(Context mContext) {
		super();
		this.mContext = mContext;
		startShowNetSpeed();
	}

	Handler mHandler;

	private long lastTotalRxBytes = 0;
	private long lastTimeStamp = 0;

	public NetWorkSpeedUtil(Context context, Handler mHandler) {
		this.mContext = context;
		this.mHandler = mHandler;
		startShowNetSpeed();
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			displaySpeed();
		}
	};

	public void startShowNetSpeed() {
		lastTotalRxBytes = getTotalRxBytes();
		lastTimeStamp = System.currentTimeMillis();
	}

	private long getTotalRxBytes() {
		return TrafficStats.getUidRxBytes(mContext.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
				: (TrafficStats.getTotalRxBytes() / 1024);//
	}

	public String displaySpeed() {
		long nowTotalRxBytes = getTotalRxBytes();
		long nowTimeStamp = System.currentTimeMillis();
		long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));
		long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % (nowTimeStamp - lastTimeStamp));
		lastTimeStamp = nowTimeStamp;
		lastTotalRxBytes = nowTotalRxBytes;
		int error = -1;
		if (speed <= 0) {
			speed = 0;
			error = 1;
		}
		if (speed2 <= 0) {
			speed2 = 0;
			error = 2;
		}
		if (error == 2) {
			if (!TextUtils.isEmpty(speedHistory)) {
				OkUtil.e("|取值历史速率|" + speedHistory);
				return speedHistory;
			}
		}
		OkUtil.e("\n|##速率一##" + speed + "\n|##速率二##" + speed2);
		String netSpeed = String.valueOf(speed)
				+ "."
				+ (String.valueOf(speed2).length() > 2 ? String.valueOf(speed2)
						.subSequence(0, 2)
						: String.valueOf(speed2).length() < 2 ? ("0" + String
								.valueOf(speed2)) : String.valueOf(speed2))
				+ " kb/s";
		speedHistory = netSpeed;
		OkUtil.e("\n|##速率##" + netSpeed);
		return netSpeed;
	}
}
