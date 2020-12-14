package okhttp.utils;

import java.math.BigDecimal;
import java.util.Locale;

public class SizeFormatUtil {
	private final static int FZ_KB = 1024;
	private final static int FZ_MB = 1024 * FZ_KB;
	private final static int FZ_GB = 1024 * FZ_MB;
	private final static int FZ_PB = 1024 * FZ_GB;

	private final static int TS_SECOND = 1000;
	private final static int TS_MINUTE = 60 * TS_SECOND;
	private final static int TS_HOUR = 60 * TS_MINUTE;

	/** 格式化单位 */
	public static String formatSize2(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte";
		}
		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}
		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}
		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	public static String formatSize(long fileLength) {
		StringBuilder sb = new StringBuilder();
		if (fileLength < FZ_KB) {
			sb.append(formatDouble(fileLength, 1)).append(" B");
		} else if (fileLength <= FZ_MB) {
			sb.append(formatDouble(fileLength, FZ_KB)).append(" KB");
		} else if (fileLength <= FZ_GB) {
			sb.append(formatDouble(fileLength, FZ_MB)).append(" MB");
		} else if (fileLength <= FZ_PB) {
			sb.append(formatDouble(fileLength, FZ_GB)).append(" GB");
		} else {
			sb.append(formatDouble(fileLength, FZ_PB)).append(" PB");
		}
		return sb.toString();
	}

	public static String formatMilliSeconds(long milliSeconds) {
		StringBuilder sb = new StringBuilder();
		long left = milliSeconds;
		if (left / TS_HOUR > 0) {
			sb.append(left / TS_HOUR).append("h ");
			left -= (left / TS_HOUR) * TS_HOUR;
		}
		if (left / TS_MINUTE > 0) {
			sb.append(left / TS_MINUTE).append("m ");
			left -= (left / TS_MINUTE) * TS_MINUTE;
		}
		if (left / TS_SECOND > 0) {
			sb.append(left / TS_SECOND).append("s ");
			left -= (left / TS_SECOND) * TS_SECOND;
		}
		sb.append(left).append("ms ");
		return sb.toString();
	}

	public static String formatDouble(long value, int divider) {
		double result = value * 1.0 / divider;
		return String.format(Locale.getDefault(), "%.2f", result);
	}

	public static String formatSpeed(double deltaSize, double deltaMillis) {
		double speed = deltaSize * 1000 / deltaMillis / FZ_KB;
		String result = String.format(Locale.getDefault(), "%.2f KB/s", speed);
		if ((int) speed > FZ_KB) {
			result = String.format(Locale.getDefault(), "%.2f MB/s", speed
					/ FZ_KB);
		}
		return result;
	}
}
