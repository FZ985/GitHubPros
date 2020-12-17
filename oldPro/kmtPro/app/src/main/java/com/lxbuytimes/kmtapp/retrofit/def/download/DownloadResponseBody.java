package com.lxbuytimes.kmtapp.retrofit.def.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Create by JFZ
 * date: 2019-10-22 11:10
 **/
public class DownloadResponseBody extends ResponseBody {
    private Response originalResponse;
    private DownloadListener downloadListener;
    private BufferedSource bufferedSource;
    private DownloadInfo info;

    public DownloadResponseBody(Response originalResponse, DownloadInfo info, DownloadListener downloadListener) {
        this.originalResponse = originalResponse;
        this.downloadListener = downloadListener;
        this.info = info;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(originalResponse.body().source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = info.currentLength;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;//不断统计当前下载好的数据
                //接口回调
                info.totalLength = (contentLength() + info.currentLength);
                int percent = (int) (totalBytesRead * 1.0f / (contentLength() + info.currentLength) * 100);
                downloadListener.update(totalBytesRead, percent, contentLength() + info.currentLength, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
