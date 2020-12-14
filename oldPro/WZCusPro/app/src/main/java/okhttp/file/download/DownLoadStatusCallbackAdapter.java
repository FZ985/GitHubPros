package okhttp.file.download;


import okhttp.utils.OkUtil;

public abstract class DownLoadStatusCallbackAdapter<T> implements
        DownLoadStatusCallback<T> {

	public DownLoadStatusCallbackAdapter() {
		super();
	}

	@Override
	public void onStart(T bean) {
		if (bean != null) {
			OkUtil.e("#开始下载#" + bean);
		}
	}

	@Override
	public void onError(T bean) {
		if (bean != null) {
			OkUtil.e("#下载失败#" + bean);
		}
	}

	@Override
	public void onSuccess(T bean) {
		if (bean != null) {
			OkUtil.e("#下载成功#" + bean);
		}
	}

	@Override
	public void onFinished(T bean) {
		if (bean != null) {
			OkUtil.e("#下载完成#" + bean);
		}
	}

	@Override
	public void onStop(T bean) {
		if (bean != null) {
			OkUtil.e("#下载停止#" + bean);
		}
	}

	@Override
	public void onPause(T bean) {
		if (bean != null) {
			OkUtil.e("#下载暂停#" + bean);
		}
	}

	@Override
	public void onCancel(T bean) {
		if (bean != null) {
			OkUtil.e("#取消下载#" + bean);
		}
	}

	@Override
	public void onProgress(T bean, int currentSize) {
		if (bean != null) {
			OkUtil.e("#下载进度#" + currentSize);
		}
	}

	@Override
	public void onPrepare(T bean) {
		if (bean != null) {
			OkUtil.e("#下载准备#" + bean);
		}
	}

}
