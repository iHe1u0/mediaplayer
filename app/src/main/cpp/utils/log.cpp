//
// Created by iMorning on 2021/9/12.
//
#include <jni.h>
#include <libsdl/SDL.h>
#include <string.h>
#include <stdio.h>
#include <log.h>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
}

void log_ffmpeg_error(int errorCode) {
    char *err_buf = new char;
    av_strerror(errorCode, err_buf, 1024);
    LOGE("ffmpeg error:%d(%s)", errorCode, err_buf);
    delete err_buf;
}
