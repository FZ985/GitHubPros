package com.okhttplib2.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.okhttplib2.BuildConfig;
import com.okhttplib2.callback.Http;
import com.okhttplib2.config.JRequest;
import com.okhttplib2.config.OkHttpConfig;
import com.okhttplib2.config.Params;
import com.okhttplib2.upload.ProgressRequestBody;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Create by JFZ
 * date: 2020-09-15 15:51
 **/
public class OkhttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static boolean DEBUG = BuildConfig.DEBUG;
    public static final Gson GSON = new Gson();

    public static void LogDebug(boolean debug) {
        DEBUG = debug;
    }

    public static void log(String tag, String msg) {
        if (DEBUG) {
//            String result = "\n";
//            StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
//            result += thisMethodStack.getClassName() + "."; // 当前的类名（全名）
//            result += thisMethodStack.getMethodName();
//            result += "(" + thisMethodStack.getFileName();
//            result += ":" + thisMethodStack.getLineNumber() + ")  \n";
//            Log.e(tag, result + "\n" + msg);

            String result = "\n";
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement thisMethodStack = stackTrace[5];
            String className = thisMethodStack.getClassName();
            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1] + ".java";
            }
            if (className.contains("$")) {
                className = className.split("\\$")[0] + ".java";
            }
            result += className + "."; // 当前的类名（全名）
            result += thisMethodStack.getMethodName();
            result += "(" + thisMethodStack.getFileName();
            result += ":" + thisMethodStack.getLineNumber() + ")  \n";
            Log.e(tag, result + "\n" + msg);
        }
    }

    //添加参数
    public static String appendParams(Params[] params) {
        if (params.length == 0) {
            return "";
        }
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Params p : params) {
            if (pos > 0) {
                sb.append("&");
            }
            sb.append(p.key).append("=").append(p.value);
            pos++;
        }
        return sb.toString();
    }

    //map转换params
    public static Params[] map2Params(HashMap<String, Object> map) {
        Params[] params = null;
        if (map != null) {
            Iterator iter = map.entrySet().iterator();
            List<Params> paramsList = new ArrayList<Params>();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val == null) {
                    val = "";
                }
                paramsList.add(new Params(key.toString(), val.toString()));
            }
            params = new Params[paramsList.size()];
            for (int i = 0; i < paramsList.size(); i++) {
                params[i] = paramsList.get(i);
            }
        } else {
            params = new Params[0];
        }
        return params;
    }

    public static Params[] validateParam(Params[] params) {
        if (params == null)
            return new Params[0];
        else
            return params;
    }

    public static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static String reqParams(Object src) {
        String json = "";
        if (src != null && src instanceof String) {
            json = (String) src;
        } else {
            if (src == null) {
            } else {
                json = GSON.toJson(src);
            }
        }
        return json;
    }

    public static Request.Builder getBuilder(Http.GETBuilder methodBuilder, JRequest request, RequestBody body) {
        Request.Builder builder = new Request.Builder();
        if (request != null && request.getHeaders().size() > 0) {
            for (JRequest.JRequestHeaders header : request.getHeaders()) {
                log("HttpApi", "添加请求头key:" + header.key + ",value:" + header.value);
                builder.addHeader(header.key, header.value);
            }
        }
        switch (methodBuilder.requestWay()) {
            default:
            case Http.GET:
                builder.get();
                break;
            case Http.POST:
                builder.post(body);
                break;
            case Http.PUT:
                builder.put(body);
                break;
            case Http.DELETE:
                if (body == null) builder.delete();
                else builder.delete(body);
                break;
            case Http.HEAD:
                builder.head();
                break;
            case Http.PATCH:
                builder.patch(body);
                break;
        }
        return builder;
    }

    public static Request getRequest(Http.GETBuilder methodBuilder, File[] fils, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener) {
        switch (methodBuilder.requestWay()) {
            default:
            case Http.GET:
                String finalUrl = methodBuilder.url();
                if (methodBuilder.request() != null) {
                    finalUrl = methodBuilder.url() + OkhttpUtil.appendParams(OkhttpUtil.validateParam(OkhttpUtil.map2Params(methodBuilder.request().requestParams)));
                }
                log("HttpApi", "#####" + typeTag(methodBuilder) + "请求:" + finalUrl);
                return getBuilder(methodBuilder, methodBuilder.request(), null).url(finalUrl).tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag).cacheControl(OkHttpConfig.getInstance().cacheControl()).build();
            case Http.POST:
            case Http.PUT:
            case Http.DELETE:
            case Http.HEAD:
            case Http.PATCH:
                if (methodBuilder.requestType() == Http.NO) {
                    return getBuilder(methodBuilder, methodBuilder.request(), null).url(methodBuilder.url()).tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag).build();
                } else if (methodBuilder.requestType() == Http.JSON) {
                    String json = (methodBuilder.request() != null && methodBuilder.request().requestBean != null) ? OkhttpUtil.reqParams(methodBuilder.request().requestBean) : "";
                    log("HttpApi", "#####" + typeTag(methodBuilder) + "请求:" + methodBuilder.url() + "<<请求json:" + json);
                    return getBuilder(methodBuilder, methodBuilder.request(), RequestBody.create(JSON, json)).url(methodBuilder.url()).tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag).build();
                } else if (methodBuilder.requestType() == Http.PARAMS) {
                    FormBody.Builder builder = new FormBody.Builder();
                    StringBuilder str = new StringBuilder();
                    if (methodBuilder.request() != null) {//key value传值请求
                        for (Params param : OkhttpUtil.validateParam(OkhttpUtil.map2Params(methodBuilder.request().requestParams))) {
                            builder.add(param.key, param.value);
                            str.append(param.key + ":" + param.value + ";");
                        }
                    }
                    log("HttpApi", "#####" + typeTag(methodBuilder) + "请求:" + methodBuilder.url() + "<<请求参数>>:" + str.toString());
                    return getBuilder(methodBuilder, methodBuilder.request(), builder.build()).url(methodBuilder.url()).tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag).build();
                } else if (methodBuilder.requestType() == Http.FORM) {
                    return buildMultipartFormRequest(methodBuilder, fils, fileKeys, uiProgressRequestListener);
                } else if (methodBuilder.requestType() == Http.BODY) {
                    return getBuilder(methodBuilder, methodBuilder.request(), methodBuilder.requestBody()).url(methodBuilder.url()).tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag).build();
                }
        }
        return null;
    }


    public static Request buildMultipartFormRequest(Http.GETBuilder methodBuilder, File[] files, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener) {
        Params[] params = (methodBuilder.request() != null ? OkhttpUtil.validateParam(OkhttpUtil.map2Params(methodBuilder.request().requestParams)) : OkhttpUtil.validateParam(null));
        MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x");
        builder.setType(MultipartBody.FORM);
        for (Params param : params) {
            log("HttpApi", "##请求数据##key:" + param.key + ",value:" + param.value);
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\""
                            + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null && fileKeys != null && files.length == fileKeys.length) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(
                        MediaType.parse(OkhttpUtil.guessMimeType(fileName)), file);
                // TODO 根据文件名设置contentType
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\""
                                + fileKeys[i] + "\"; filename=\"" + fileName
                                + "\""), fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return getBuilder(methodBuilder,
                methodBuilder.request(),
                uiProgressRequestListener == null
                        ? requestBody
                        : ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
                .url(methodBuilder.url())
                .tag((methodBuilder.request() == null || methodBuilder.request().tag == null) ? methodBuilder.url() : methodBuilder.request().tag)
                .build();
    }

    public static String typeTag(Http.GETBuilder methodBuilder) {
        if (methodBuilder.requestWay() == Http.GET) return "get";
        else if (methodBuilder.requestWay() == Http.POST) return "post";
        else if (methodBuilder.requestWay() == Http.PUT) return "put";
        else if (methodBuilder.requestWay() == Http.DELETE) return "delete";
        else if (methodBuilder.requestWay() == Http.HEAD) return "head";
        else if (methodBuilder.requestWay() == Http.PATCH) return "patch";
        else return "";
    }

    public static class ProgressHelper {

        /**
         * 包装请求体用于上传文件的回调
         *
         * @param requestBody             请求体RequestBody
         * @param progressRequestListener 进度回调接口
         * @return 包装后的进度回调请求体
         */
        public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, Http.ProgressRequestListener progressRequestListener) {
            //包装请求体
            return new ProgressRequestBody(requestBody, progressRequestListener);
        }
    }
}
