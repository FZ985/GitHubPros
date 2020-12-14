package okhttp.file;



import okhttp.callback.ProgressRequestListener;
import okhttp.file.upload.ProgressRequestBody;
import okhttp3.RequestBody;

/**
 * Created by JFZ .
 * on 2018/1/16.
 * 进度回调辅助类
 */

public class ProgressHelper {

    /**
     * 包装请求体用于上传文件的回调
     * @param requestBody 请求体RequestBody
     * @param progressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener){
        //包装请求体
        return new ProgressRequestBody(requestBody,progressRequestListener);
    }
}
