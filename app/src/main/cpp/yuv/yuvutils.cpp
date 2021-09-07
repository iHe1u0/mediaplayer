//
// Created by iMorning on 2021/9/7.
//

#include <jni.h>
#include <libyuv.h>
#include <log.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_imorning_mediaplayer_utils_YuvUtils_sayHello(JNIEnv *env, jobject thiz) {
    LOGD("Hello form yuv");
}