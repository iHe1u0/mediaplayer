//
// Created by iMorning on 2021/9/7.
//

#include <jni.h>
#include "log.h"

extern "C" {
#include "AudioDevice.h"
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_imorning_mediaplayer_player_audio_AudioPlayer_play(JNIEnv *env, jobject thiz,
                                                            jstring url_) {
    const char *url = env->GetStringUTFChars(url_, 0);

    LOGD("play file >>> %s", url);
    int code = play(url);
    return code;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_imorning_mediaplayer_player_audio_AudioPlayer_stop(JNIEnv *env, jobject thiz) {
    LOGD("stop");
    int code = shutdown();
    return code;
}