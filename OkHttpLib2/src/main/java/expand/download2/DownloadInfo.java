package expand.download2;

import android.os.Parcel;
import android.os.Parcelable;

import expand.download2.interfaces.DownloadError;
import expand.download2.interfaces.DownloadStatus;

/**
 * author：jfz
 * version：1.0.0
 * date：2017/8/27
 */
public class DownloadInfo implements Parcelable {

    public static final int ID_INVALID = 0;

    /**
     * 自动生成，区别不同的下载任务
     */
    private long mId = ID_INVALID;
    private String mUrl;//下载url
    private String mTag;//标签
    private String mPath;//本地保存路径
    private int mStatus = DownloadStatus.STATUS_PENDED;//下载状态
    private long mTotalSize = 0;//总大小
    private long mDownloadedBytes = 0;//已下载
    private int mErrorCode = DownloadError.ERROR_SUCCESS;//下载错误码
    private long mModifyTime = 0;//任务更新时间
    private String mTaskName;//任务名
    private String mFileName;//文件名
    private int mHttpStatus;//http状态码
    private String mMD5;//MD5码

    public DownloadInfo(long mId) {
        this.mId = mId;
    }

    public DownloadInfo(long mId, String mUrl, String mPath, String mFileName) {
        this.mId = mId;
        this.mUrl = mUrl;
        this.mPath = mPath;
        this.mFileName = mFileName;
        setTaskName(getFileName(mUrl));
    }

    public DownloadInfo(Parcel in) {
        mId = in.readLong();
        mUrl = in.readString();
        mTag = in.readString();
        mPath = in.readString();
        mStatus = in.readInt();
        mTotalSize = in.readLong();
        mDownloadedBytes = in.readLong();
        mErrorCode = in.readInt();
        mModifyTime = in.readLong();
        mTaskName = in.readString();
        mFileName = in.readString();
        mHttpStatus = in.readInt();
        mMD5 = in.readString();
    }


    public static final Creator<expand.download2.DownloadInfo> CREATOR
            = new Creator<expand.download2.DownloadInfo>() {
        public expand.download2.DownloadInfo createFromParcel(Parcel in) {
            return new expand.download2.DownloadInfo(in);
        }

        public expand.download2.DownloadInfo[] newArray(int size) {
            return new expand.download2.DownloadInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mUrl);
        dest.writeString(mTag);
        dest.writeString(mPath);
        dest.writeInt(mStatus);
        dest.writeLong(mTotalSize);
        dest.writeLong(mDownloadedBytes);
        dest.writeInt(mErrorCode);
        dest.writeLong(mModifyTime);
        dest.writeString(mTaskName);
        dest.writeString(mFileName);
        dest.writeInt(mHttpStatus);
        dest.writeString(mMD5);
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public long getTotalSize() {
        return mTotalSize;
    }

    public void setTotalSize(long totalSize) {
        this.mTotalSize = totalSize;
    }

    public long getDownloadedBytes() {
        return mDownloadedBytes;
    }

    public void setDownloadedBytes(long downloadedBytes) {
        this.mDownloadedBytes = downloadedBytes;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    public long getModifyTime() {
        return mModifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.mModifyTime = modifyTime;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        this.mTaskName = taskName;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public int getHttpStatus() {
        return mHttpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.mHttpStatus = httpStatus;
    }

    public String getMD5() {
        return mMD5;
    }

    public void setMD5(String mD5) {
        this.mMD5 = mD5;
    }

    public String getFileName(String url) {
        String result = "";
        int i = 0;
        while (i < 1) {
            int lastFirst = url.lastIndexOf('/');
            result = url.substring(lastFirst) + result;
            url = url.substring(0, lastFirst);
            i++;
        }
        return result.substring(1);
    }
}
