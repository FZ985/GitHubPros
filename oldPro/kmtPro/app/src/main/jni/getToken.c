//
// Created by HolyGoose on 16/6/29.
//
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include "renrenkan_Md5Utils.h"
#include <android/log.h>
#include <malloc.h>
#include <sys/time.h>
#include "md5c.h"
#include "android_log_print.h"

/*
 * Class:
 * Method:    getToken
 * Signature: (Ljava/lang/String;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_renrenkan_Md5Utils_getToken(JNIEnv *env,
                                                                     jclass clazz, jstring jInfo,
                                                                     jstring jstmp) {

    const char *jstr = (*env)->GetStringUTFChars(env, jInfo, NULL);//第一次MD5初始数据
    const char *stmp = (*env)->GetStringUTFChars(env, jstmp, NULL);//时间戳后三位
    const char *cstr = "sjs";//拼接sjs
    char *jjstr;
    if ((jjstr = malloc((strlen(jstr) + strlen(stmp) + 1) * sizeof(char))) == NULL) {
        return NULL;
    }
    strcpy(jjstr, jstr);
    strcat(jjstr, cstr);
//    LOGI("第一次MD5:%s", jjstr);
//    LOGI("时间戳后三位:%s", stmp);
    MD5_CTX context = {0};
    MD5Init(&context);
    MD5Update(&context, jjstr, strlen(jjstr));
    unsigned char dest[16] = {0};
    MD5Final(dest, &context);

    int i;
    char destination[32] = {0};
    for (i = 0; i < 16; i++) {
        sprintf(destination, "%s%02x", destination, dest[i]);
    }
//    LOGI("第一次MD5结束:%s", destination);
    char tmp[7] = {destination[0], destination[1], destination[2], destination[8], destination[10],
                   destination[14]};
    //之后的逻辑,第二次MD5
//    LOGI("抽取出来的值:%s", tmp);
    strcat(destination, tmp);
    strcat(destination, stmp);//拼接时间戳后3
    char *final = destination;
//    LOGI("第二次MD5:%s", final);

    //第二次MD5
    MD5_CTX context1 = {0};
    MD5Init(&context1);
    MD5Update(&context1, final, strlen(final));
    unsigned char dest1[16] = {0};
    MD5Final(dest1, &context1);
    int i1;
    char destination1[32] = {0};
    for (i1 = 0; i1 < 16; i1++) {
        sprintf(destination1, "%s%02x", destination1, dest1[i1]);
    }
//    LOGI(">>第二次MD5:%s", destination1);
    (*env)->ReleaseStringUTFChars(env, jInfo, jstr);
    (*env)->ReleaseStringUTFChars(env, jstmp, stmp);
//    LOGI("返回md5:%s", destination1);
    return (*env)->NewStringUTF(env, destination1);
}
