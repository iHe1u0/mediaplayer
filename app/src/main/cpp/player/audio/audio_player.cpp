//
// Created by iMorning on 2021/9/12.
//

#include <stdio.h>
#include <stdbool.h>
#include <assert.h>
#include <sys/time.h>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
#include <log.h>
}

#include <audio_player.h>

#include <SDL2/SDL.h>
#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
#include <unistd.h>


int log_ffmpeg_error(int errorCode, int line) {
    char *err_buf = new char;
    av_strerror(errorCode, err_buf, 1024);
    LOGE("ffmpeg error:%d(%s),line:%d", errorCode, err_buf, line);
    delete err_buf;
    return errorCode;
}

int playAudio(JNIEnv *env, jobject instance, jstring audioPath) {
    int status = 0;
    const char *path = env->GetStringUTFChars(audioPath, 0);
    av_register_all();
    AVFormatContext *pFormatContext = avformat_alloc_context();
    if ((status = avformat_open_input(&pFormatContext, path, NULL, NULL)) != 0) {
        return log_ffmpeg_error(status, code_line);
    }
    if ((status = avformat_find_stream_info(pFormatContext, NULL)) < 0) {
        return log_ffmpeg_error(status, code_line);
    }
    av_dump_format(pFormatContext, 0, path, 0);
    int audio_index = -1;
    for (int i = 0; i < pFormatContext->nb_streams; i++) {
        if (pFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_index = i;
            break;
        }
    }
    if (audio_index == -1) {
        return -1;
    }
    AVCodecParameters *pCodecParams = pFormatContext->streams[audio_index]->codecpar;
    AVCodecID pCodecId = pCodecParams->codec_id;
    if (pCodecId == NULL) {
        LOGE("%s", "CodecID == null");
        return -1;
    }
    AVCodec *pCodec = avcodec_find_decoder(pCodecId);
    if (pCodec == NULL) {
        LOGE("%s", "没有找到解码器");
        return -1;
    }
    AVCodecContext *pCodecContext = avcodec_alloc_context3(pCodec);
    if (pCodecContext == NULL) {
        LOGE("%s", "不能为CodecContext分配内存");
        return -1;
    }
    if ((status = avcodec_parameters_to_context(pCodecContext, pCodecParams)) < 0) {
        return log_ffmpeg_error(status, code_line);
    }
    if ((status = avcodec_open2(pCodecContext, pCodec, NULL)) < 0) {
        return log_ffmpeg_error(status, code_line);
    }
    AVPacket *avp = av_packet_alloc();
    AVFrame *avf = av_frame_alloc();

    //frame->16bit 44100 PCM 统一音频采样格式与采样率
    SwrContext *swr_cxt = swr_alloc();
    //重采样设置选项-----------------------------------------------------------start

    // 输入的采样格式
    enum AVSampleFormat in_sample_fmt = pCodecContext->sample_fmt;
    //输出的采样格式
    enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
    //输入的采样率
    int in_sample_rate = pCodecContext->sample_rate;
    LOGD("sample rate = %d \n", in_sample_rate);
    //输出的采样率
    int out_sample_rate = 44100;
    //输入的声道布局
    uint64_t in_ch_layout = pCodecContext->channel_layout;
    //输出的声道布局
    uint64_t out_ch_layout = AV_CH_LAYOUT_MONO;
    //SwrContext 设置参数
    swr_alloc_set_opts(swr_cxt, out_ch_layout, out_sample_fmt, out_sample_rate, in_ch_layout,
                       in_sample_fmt, in_sample_rate, 0, NULL);
    //初始化SwrContext
    swr_init(swr_cxt);

    //重采样设置选项-----------------------------------------------------------end

    // 获取输出的声道个数
    int out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    jclass clazz = env->GetObjectClass(instance);
    //调用Java方法MethodID
    jmethodID methodId = env->GetMethodID(clazz, "createTrack", "(II)V");
    jmethodID methodID1 = env->GetMethodID(clazz, "playTrack", "([BI)V");
    //通过methodId调用Java方法
    env->CallVoidMethod(instance, methodId, 44100, out_channel_nb);
    //存储pcm数据
    uint8_t *out_buf = (uint8_t *) av_malloc(2 * 44100);
    int frame_count = 0;
    //一帧一帧读取压缩的音频数据AVPacket
    int ret;
    while (av_read_frame(pFormatContext, avp) >= 0) {
        if (avp->stream_index == audio_index) {
            ret = avcodec_send_packet(pCodecContext, avp);
            if (ret != 0) {
                return ret;
            }
            while (avcodec_receive_frame(pCodecContext, avf) == 0) {
                LOGD("正在解码第%d帧", ++frame_count);
                int len = swr_convert(swr_cxt, &out_buf, 2 * 44100, (const uint8_t **) avf->data,
                                      avf->nb_samples);
                //get size of sample
                int out_buf_size = len * out_channel_nb * av_get_bytes_per_sample(out_sample_fmt);
                jbyteArray audioArray = env->NewByteArray(out_buf_size);
                env->SetByteArrayRegion(audioArray, 0, out_buf_size, (const jbyte *) out_buf);

                env->CallVoidMethod(instance, methodID1, audioArray, out_buf_size);
                env->DeleteLocalRef(audioArray);
            }
        }
        av_packet_unref(avp);
    }
    av_frame_free(&avf);
    swr_free(&swr_cxt);
    avcodec_close(pCodecContext);
    avformat_close_input(&pFormatContext);
    env->ReleaseStringUTFChars(audioPath, path);
}

extern "C"
JNIEXPORT int JNICALL
Java_com_imorning_mediaplayer_player_audio_AudioPlayer__1play(JNIEnv *env, jobject thiz,
                                                              jstring path) {
    return playAudio(env, thiz, path);
}
extern "C"
JNIEXPORT int JNICALL
Java_com_imorning_mediaplayer_player_audio_AudioPlayer__1stop(JNIEnv *env, jobject thiz) {

    return 0;
}