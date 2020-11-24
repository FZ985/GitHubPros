//
// Created by ss on 2020-11-17.
//

#include <jni.h>
#include <string>
#include "jnitool.h"
#include "jni_jfz_http_NativeHttp.h"

extern "C" JNIEXPORT void JNICALL
Java_jni_jfz_http_NativeHttp_getNative(JNIEnv *env, jobject nativeHttp, jstring url, jobject call) {
    LOGI("getNative===start");
    const char *mUrl = env->GetStringUTFChars(url, JNI_FALSE);

}
