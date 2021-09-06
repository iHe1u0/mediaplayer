//
// Created by iMorning on 2021/9/7.
//

#ifndef MEDIAPLAYER_FFMPEGAUDIOPLAY_H
#define MEDIAPLAYER_FFMPEGAUDIOPLAY_H

#include <stdio.h>

int createFFmpegAudioPlay(const char *file_name, int *rate, int *channel);

int getPCM(void **pcm, size_t *pcmSize);

int releaseFFmpegAudioPlay();

#endif //MEDIAPLAYER_FFMPEGAUDIOPLAY_H
