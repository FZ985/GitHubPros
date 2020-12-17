package com.lxbuytimes.kmtapp.retrofit.def;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Create by JFZ
 * date: 2019-04-22 10:30
 **/
public abstract class DefaultCallback<T> {
    public Type mType;

    public DefaultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("jfz_exception:Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized
                .getActualTypeArguments()[0]);
    }

    public abstract void onResponse(T data);

    public abstract void onCallError(int code,Throwable e);

    /**
     * 开始请求时，无网络调用此方法
     * 没有联网
     */
    public void notNetWork() {
    }
}
