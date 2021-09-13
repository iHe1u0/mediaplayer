//
// Created by iMorning on 2021/9/12.
//
#include <audio_player.h>
#include <jni.h>


extern "C"
JNIEXPORT jint JNICALL
Java_com_imorning_mediaplayer_player_audio_AudioPlayer__1play(JNIEnv *env, jobject thiz,
                                                              jstring path) {
    const char *filePath = env->GetStringUTFChars(path, 0);
    return play(filePath);
}