//
// Created by ss on 2020-12-09.
//

#include <jni.h>
#include <string>
#include <jni.h>
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_aaeess_AAEESS_pass(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("ddd");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_aaeess_AAEESS_passIv(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("eee");
}
