#include <jni.h>
#include <string>
#include "common.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_cmake_jni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    LOGI("DDDDDD");
    return env->NewStringUTF(hello.c_str());
}
