package expand.download;

import java.io.File;

import okhttp3.Response;

interface DownLoadListener {

    void update(long progress, float percent, long contentLength, boolean done);

    void complete(File file);

    void error(Exception e);

    void newResponse(Response response, File file);

    void cancel();
}
