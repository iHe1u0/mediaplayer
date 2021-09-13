//
// Created by iMorning on 2021/9/12.
//

#ifndef MEDIAPLAYER_AUDIO_PLAYER_H
#define MEDIAPLAYER_AUDIO_PLAYER_H

#include <jni.h>

class AudioPlayer {
public:
    const char *audioFilePath;
};

int playAudio(JNIEnv *env, jobject instance, jstring audioPath);


#endif //MEDIAPLAYER_AUDIO_PLAYER_H
