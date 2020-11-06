//
// Created by ss on 2020-10-30.
//

//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
#include <jni.h>
#include <com_jni_sdk_SdkNative.h>
#include <android/log.h>
#include <android_log_print.h>

JNIEXPORT jstring JNICALL Java_com_jni_sdk_SdkNative_getStr
  (JNIEnv * env, jobject object){
  return (* env).NewStringUTF("wx1d3b0ab077d5f2f5");
  }

/*
 * Class:     com_jni_sdk_SdkNative
 * Method:    updateValue
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jni_sdk_SdkNative_updateValue
  (JNIEnv * env, jobject object){

    //获取类的class
    jclass constantCls = (*env).FindClass("com/jni/sdk/SdkConstant");
    //jclass constantCls = (*env).GetObjectClass(object);
    if(constantCls != NULL){
        LOGE("jni: %s","constantCls 不为空");
        //获取静态属性字段的id   Ljava/lang/String;
        // 参数1: 静态类class;  参数2: 字段的名字;   参数3: 字段的类型
        jfieldID baseUrlId = (*env).GetStaticFieldID(constantCls, "BASE_URL", "Ljava/lang/String;");
        //修改BASE_URL的属性值
        (*env).SetStaticObjectField(constantCls,baseUrlId, (*env).NewStringUTF("https://www.baidu.com"));
    }
}

  JNIEXPORT void JNICALL Java_com_jni_sdk_SdkNative_onNativeConfig
    (JNIEnv * env, jobject obj, jobject call){
    jclass cls = (*env).GetObjectClass(call);
    if(cls != NULL){
      jmethodID successId = (*env).GetMethodID(cls,"onSuccess","()V");
      jmethodID errorId = (*env).GetMethodID(cls,"onError","(Ljava/lang/String;)V");
      env->CallVoidMethod(call,successId,NULL);
      jstring msg = env->NewStringUTF("滴滴滴滴滴滴滴滴");
      env->CallVoidMethod(call,errorId,msg);

    }

}