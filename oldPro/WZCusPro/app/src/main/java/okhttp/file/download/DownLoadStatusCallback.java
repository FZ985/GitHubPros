package okhttp.file.download;

public interface DownLoadStatusCallback<T> {
	public void onStart(T bean);

	public void onError(T bean);

	public void onSuccess(T bean);

	public void onFinished(T bean);

	public void onStop(T bean);

	public void onPause(T bean);

	public void onCancel(T bean);
	/**
	 * @param currentSize
	 */
	public void onProgress(T bean, int currentSize);

	public void onPrepare(T bean);
}
