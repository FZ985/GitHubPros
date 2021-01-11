package com.okhttpapp;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.okhttpapp.okhttp2.test.IPBean;
import com.okhttpapp.okhttp2.test.JsonReq;
import com.okhttpapp.okhttp2.test.JsonResp;
import com.okhttpapp.upload.UploadErr;
import com.okhttpapp.upload.UploadInfo;
import com.okhttpapp.upload.UploadListener;
import com.okhttpapp.upload.UploadManager;
import com.okhttplib2.HttpImpl;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.config.JRequest;

import java.io.File;
import java.util.HashMap;

import expand.DefLoad;
import expand.download2.AbsDownloadManager;
import expand.download2.DownloadInfo;
import expand.download2.DownloadManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 13:52
 **/
public class OkHttp2Activity extends AppCompatActivity {
    private String ipUrl = "http://apis.juhe.cn/ip/ipNew";
    private String testUrl = "http://appapi1.lnamphp.com/job/app/job/qrCodeInfo";
    private TextView downloadMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp2);
        downloadMsg = findViewById(R.id.downloadMsg);

        DownloadManager.instance(this.getApplicationContext()).setMaxRunningTask(3);
        DownloadManager.instance(this.getApplicationContext()).registerDownloadObserver(new AbsDownloadManager.IDownloadObserver<DownloadInfo>() {
            @Override
            public void onUpdateStatus(DownloadInfo downloadInfo) {
                System.out.println("Download_onStatus:" + downloadInfo.getStatus());
            }

            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                downloadMsg.setText(downloadInfo.getDownloadedBytes() + "/" + downloadInfo.getTotalSize());
//                System.out.println("Download_progress" + downloadInfo.getDownloadedBytes() + "," + downloadInfo.getTotalSize());
            }
        });

    }

    public void syncGet(View view) {
        try {
            Response response = HttpImpl.get("http://www.baidu.com").bind(this).execute();
            if (response != null) {
                String string = response.body().string();
                Toast.makeText(this, "返回:" + string, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "同步catch:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void asyncGet(View view) {
        HttpImpl.get("http://www.baidu.com")
                .bind(this)
                .load(DefLoad.use(this))
                .enqueue(new RequestCallback<String>() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void postParams(View view) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", "192.168.0.99");
        map.put("key", "9f703454a63103c1588a6abfec6ef91d");
        try {
            IPBean data = (IPBean) HttpImpl.postParams(ipUrl)
                    .request(new JRequest(IPBean.class, map))
                    .executeObject();
            if (data != null) {
                Toast.makeText(this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void postAsyncParams(View view) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", "192.168.0.99");
        map.put("key", "9f703454a63103c1588a6abfec6ef91d");
        HttpImpl.postParams(ipUrl)
                .request(new JRequest(map))
                .enqueue(new RequestCallback<IPBean>() {
                    @Override
                    public void onResponse(IPBean data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void postAsyncJson(View view) {
        JRequest request = new JRequest(new JsonReq("1082565"));
        HttpImpl.postJson(testUrl)
                .request(request)
                .enqueue(new RequestCallback<JsonResp>() {
                    @Override
                    public void onResponse(JsonResp data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void download(View view) {
        if (!PermissionUtils.checkReadWritePermission(this)) {
            PermissionUtils.reqPermission(this, PermissionUtils.PERMISSIONS_READ_AND_WRITE, 100);
            return;
        }

//        DownLoadBuilder3.getInstance().cancelAll();
//        ((Button) view).setText("download");
//        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator;
//        System.out.println(path);
//        HttpImpl.download3("https://g37.gdl.netease.com/onmyoji_setup_11.9.0.zip",
//                path, "weixin7021android1800_arm64.zip", new DownLoadListenerAdapter() {
//                    @Override
//                    public void update(long progress, float percent, long contentLength, boolean done) {
//                        runOnUiThread(() -> {
//                            ((Button) view).setText(progress + "" + percent);
//                        });
//                    }
//
//                    @Override
//                    public void complete(File file) {
//                        runOnUiThread(() -> {
//                            ((Button) view).setText("下载完成");
//                            Toast.makeText(OkHttp2Activity.this, "下载完成:" + file.getPath(), Toast.LENGTH_SHORT).show();
//                        });
//                    }
//
//                    @Override
//                    public void error(Exception e) {
//                        runOnUiThread(() -> {
//                            Toast.makeText(OkHttp2Activity.this, "下载失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//                    }
//                });


        DownloadInfo info = new DownloadInfo(1);

//        String url = "https://g37.gdl.netease.com/onmyoji_setup_11.9.0.zip";
        String url = "http://weixinzhuanfa-app009.oss-cn-shenzhen.aliyuncs.com/10005_app_apk/guanfang1.0.3.apk";
        String fileName = getFileName(url);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/" + fileName;

        info.setUrl(url);
        info.setTag(fileName);
        info.setPath(filePath);
        info.setTaskName(fileName);
//
//        DownloadInfo info2 = new DownloadInfo(2);
//
//        String url2 = "https://g37.gdl.netease.com/onmyoji_setup_11.9.0.zip";
//        String fileName2 = getFileName(url2);
//        String filePath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/" + fileName2;
//
//        info2.setUrl(url2);
//        info2.setTag(fileName2);
//        info2.setPath(filePath2);
//        info2.setTaskName(fileName2);
//        info2.setId(fileName2.hashCode());

        DownloadManager.instance(this).add(info);
    }

    public static String getFileName(String url) {
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

    public void cancel(View view) {
//        DownloadManager.instance(this).pause(1);
    }

    //上传图片
    public void uploadImage() {
        String image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qq.jpg";

        UploadInfo[] infos = new UploadInfo[5];
        for (int i = 0; i < 5; i++) {
            infos[i] = new UploadInfo(i, new File(image), "image");
        }

        String url = "http://xxxxx";
        JRequest request = new JRequest();
        request.tag = url;
        UploadManager.instance()
                .add(HttpImpl.postForm(url)
                        .request(request), new UploadListener<String>() {
                    @Override
                    public void onCall(String t, UploadInfo uploadInfo) {
                        switch (uploadInfo.getStatus()) {
                            case UploadErr.CANCEL:
                                System.out.println("id:" + uploadInfo.getId() + "取消了");
                                break;
                            case UploadErr.RUNNING:
                                System.out.println("id：" + uploadInfo.getId() + "," + uploadInfo.getBytesWrite() + "," + uploadInfo.getContentLength() + "," + uploadInfo.isDone());
                                break;
                            case UploadErr.START:
                                System.out.println("id：" + uploadInfo.getId() + "上传开始");
                                break;
                            case UploadErr.SUCCESS:
                                System.out.println("id：" + uploadInfo.getId() + ",上传成功");
                                if (t != null) {//请求成功数据
                                    System.out.println(t.toString());
                                }
                                break;
                            case UploadErr.ERROR:
                                System.out.println("id:" + uploadInfo.getId() + "上传失败:" + uploadInfo.getMsg());
                                break;
                        }
                    }
                }, infos);
    }

    //取消上传
    public void uploadCancel() {
        UploadManager.instance().cancelAll("http://xxxxx");
    }


    //自定义请求体
    public void customBody() {
        HttpImpl.postBody("http://xxxxx.xxxxx.xxxx")
                .upRequestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "dataxxxxx"))
                .bind(this)
                .enqueue(new RequestCallback<String>() {
                    @Override
                    public void onResponse(String data) {

                    }

                    @Override
                    public void onError(int code, Exception e) {

                    }
                });
    }
}
