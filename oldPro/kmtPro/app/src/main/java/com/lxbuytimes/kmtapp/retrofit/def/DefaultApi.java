package com.lxbuytimes.kmtapp.retrofit.def;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Create by JFZ
 * date: 2019-10-17 16:02
 * 默认的请求Service管理
 **/
public interface DefaultApi {

    @GET
    Call<ResponseBody> executeGet(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<ResponseBody> executeGet(@Url String url, @QueryMap Map<String, Object> params, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePost(@Url String url, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePost(@Url String url, @FieldMap Map<String, Object> params, @HeaderMap Map<String, String> headers);

    //    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePostJson(@Url String url, @Body RequestBody body, @HeaderMap Map<String, String> headers);

    @GET
    Observable<ResponseBody> enqueueGet(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Observable<ResponseBody> enqueueGet(@Url String url, @QueryMap Map<String, Object> map, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> enqueuePost(@Url String url, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> enqueuePost(@Url String url, @FieldMap Map<String, Object> map, @HeaderMap Map<String, String> headers);

    @POST
    Observable<ResponseBody> enqueuePostJson(@Url String url, @Body RequestBody body, @HeaderMap Map<String, String> headers);

    /**
     * @param start 从某个字节开始下载数据
     * @param url   文件下载的url
     * @return Observable
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);


    /**
     * 支持文件上传的表单请求
     *
     * @param url  请求地址
     * @param body 请求体
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> postForm(@Url String url, @PartMap RequestBody body, @HeaderMap Map<String, String> headers);
}
