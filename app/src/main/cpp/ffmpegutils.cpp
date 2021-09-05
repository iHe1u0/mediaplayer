//
// Created by iMorning on 2021/9/5.
//
#include <jni.h>
#include <string>

#ifdef __cplusplus
extern "C" {
#endif
#include "libavcodec/avcodec.h"
#ifdef __cplusplus
}
#endif


extern "C"
JNIEXPORT jstring JNICALL
Java_com_imorning_mediaplayer_utils_Jni_getVersion(JNIEnv *env, jobject thiz) {

    std::string version = avcodec_configuration();
    return env->NewStringUTF(version.c_str());
}