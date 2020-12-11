package com.okhttpapp.upload;

import android.os.Environment;

import com.okhttplib2.HttpImpl;
import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.config.JRequest;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 递归的方法来上传图片
 * Author: jfz
 * Date: 2020-12-11 12:13
 */
public class Upload2 {


    public boolean isCanceled = false;

    // TODO: 2020-12-11 递归上传图片
    private void uploadNext(List<String> list) {
        if (!isCanceled) { // TODO: 2020-12-11 未取消才能上传
            if (list.size() > 0) {
                // TODO: 2020-12-11  每次取集合的第一个上传
                String filePath = list.get(0);
                String url = "http://xxxxx";
                JRequest request = new JRequest();
                request.tag = url;
                HttpImpl.postForm(url).enqueueUploadFile(new File[]{new File(filePath)}, new String[]{"image"},
                        new RequestCallback<String>() {
                            @Override
                            public void onResponse(String data) {
                                // TODO: 2020-12-11 第一张上传成功,继续上传第二张
                                // TODO: 2020-12-11 只有未取消的时候才删除第一条
                                if (!isCanceled) {
                                    list.remove(0);
                                    // TODO: 2020-12-11 当一张图片上传成功的时候,在这里处理逻辑
                                }
                                uploadNext(list);
                            }

                            @Override
                            public void onError(int code, Exception e) {
                                // TODO: 2020-12-11 上传失败,删除第一张,继续上传第二张
                                // TODO: 2020-12-11 只有未取消的时候才删除第一条
                                if (!isCanceled) {
                                    list.remove(0);
                                    // TODO: 2020-12-11 当一张图片上传失败的时候,在这里处理逻辑
                                }
                                uploadNext(list);
                            }
                        }, new UIProgressRequestListener() {
                            @Override
                            public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {

                            }
                        });
            } else {
                // TODO: 2020-12-11 list为空,已经全部上传完成
                // TODO: 2020-12-11 没有取消, 要处理接下来的逻辑
            }
        }


    }


    //上传图片
    public void uploadImage() {
        String image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qq.jpg";
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(image);
        }
        isCanceled = false;
        uploadNext(list);
    }

    //取消上传
    public void uploadCancel() {
        isCanceled = true;
        OkHttpFactory.getInstance().cancel("http://xxxxx");
    }

}