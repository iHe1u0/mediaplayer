//
// Created by iMorning on 2021/9/14.
//
#include <jni.h>
#include <log.h>
#include <string>
#include <json/json.h>

extern "C" {
#include <libavutil/avutil.h>
#include <libavformat/avformat.h>
}

using namespace std;
/**
 * 打开输入流，得到 AVFormatContext 相关信息
 *
 * 1. 音视频的封装格式
 * 2. 音视频的时长，大小
 * 3. 码率，分辨率，fps
 * 4. 采样率，通道数，位宽
 * 5. 编解码格式
 */

extern "C"
JNIEXPORT jstring JNICALL
Java_com_imorning_mediaplayer_player_Player_nativeGetMediaInfo(JNIEnv *env, jobject instance,
                                                               jstring url) {
    string jsonBuilder;    // 用来保存媒体信息
    Json::Value jsonRoot, jsonStreamInfo;
    // 注册所有的封装和解封装格式
    av_register_all();

    // 初始化网络组件
    avformat_network_init();

    // 注册编解码器
    avcodec_register_all();

    // 打开指定的输入流地址
    // 指针定义之后，一定要赋值才能使用，不然会抛出错误地址异常
    AVFormatContext *pAvFormatContext = NULL;

    const char *_url = env->GetStringUTFChars(url, 0);

    int ret = avformat_open_input(&pAvFormatContext, _url, 0, 0);

    if (ret != 0) {
        //加入本地传入的文件地址找不到，会报：avformat_open_input failed：No such file or directory
        LOGE("avformat_open_input failed：%s", av_err2str(ret));
        return (jstring) "avformat_open_input failed";
    }

    //【探测】如果通过 avformat_open_input 无法读取到流的头信息，那么可以通过 avformat_find_stream_info 获取流信息
    ret = avformat_find_stream_info(pAvFormatContext, 0);
    if (ret < 0) {
        LOGE("avformat_find_stream_info failed:%s", av_err2str(ret));
        return env->NewStringUTF((new string("avformat_find_stream_info failed"))->c_str());
    }
    //媒体流数量
    int nb_streams = pAvFormatContext->nb_streams;

    //时长
    double duration = pAvFormatContext->duration / AV_TIME_BASE;

    //媒体路径
    char *fileAbsPath = pAvFormatContext->url;

    //首帧的时间
    double start_time = pAvFormatContext->start_time / AV_TIME_BASE;

    jsonRoot["fileAbsPath"] = fileAbsPath;
    jsonRoot["解码器:"] = pAvFormatContext->iformat->name;
    jsonRoot["首帧时间:"] = start_time;
    jsonRoot["媒体流数量:"] = nb_streams;
    jsonRoot["总时长:"] = duration;
    //拿到音视频流的相关信息
    int audioIndex = 0;
    int videoIndex = 0;


    //=========================方式1：获取音视频索引=========================================
    for (int streamIndex = 0; streamIndex < pAvFormatContext->nb_streams; streamIndex++) {
        jsonStreamInfo["streamId:"] = streamIndex;
        AVStream *pAvStream = pAvFormatContext->streams[streamIndex];
        // 流的编码器参数指针
        AVCodecParameters *avCodecParameters = pAvStream->codecpar;
        if (avCodecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {
            jsonStreamInfo["媒体流类型"] = av_get_media_type_string(AVMEDIA_TYPE_VIDEO);
            if (avCodecParameters->bit_rate != 0) {
                jsonStreamInfo["视频帧率"] = pAvStream->avg_frame_rate.num;
                jsonStreamInfo["视频宽度"] = avCodecParameters->width;
                jsonStreamInfo["视频高度"] = avCodecParameters->height;
                jsonStreamInfo["视频pixel_format"] = avCodecParameters->format;
                jsonStreamInfo["视频编解码器"] = avcodec_get_name(avCodecParameters->codec_id);
                jsonStreamInfo["视频bit_rate"] = avCodecParameters->bit_rate / 1000.0; // Kb/s
            }
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioIndex = streamIndex;
            jsonStreamInfo["媒体流类型"] = av_get_media_type_string(AVMEDIA_TYPE_AUDIO);
            jsonStreamInfo["音频采样率"] = avCodecParameters->sample_rate;
            jsonStreamInfo["音频 bit_rate"] = avCodecParameters->bit_rate / 1000.0;
            jsonStreamInfo["音频通道数"] = avCodecParameters->channels;
            jsonStreamInfo["音频编解码器"] = avcodec_get_name(avCodecParameters->codec_id);
            jsonStreamInfo["音频采样格式"] = av_get_sample_fmt_name(
                    (AVSampleFormat) avCodecParameters->format);
            jsonStreamInfo["音频音频帧大小"] = avCodecParameters->frame_size;
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_ATTACHMENT) {
            jsonStreamInfo["媒体流类型"] = av_get_media_type_string(AVMEDIA_TYPE_ATTACHMENT);
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_DATA) {
            jsonStreamInfo["媒体流类型"] = "AVMEDIA_TYPE_DATA";
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_NB) {
            jsonStreamInfo["媒体流类型"] = "AVMEDIA_TYPE_NB";
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_SUBTITLE) {
            jsonStreamInfo["媒体流类型"] = "AVMEDIA_TYPE_SUBTITLE";
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_UNKNOWN) {
            jsonStreamInfo["媒体流类型"] = "AVMEDIA_TYPE_UNKNOWN";
        }
        jsonRoot["stream_info"].append(jsonStreamInfo);
        jsonStreamInfo.clear();
    }

    //=========================方式2：获取音视频索引=========================================
    //如果在前面没有得到视频或者音频的索引值，那么可以通过以下的 api 获取

    //audioIndex = av_find_best_stream(pAvFormatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    //LOGI("最佳音频的索引值 = %d", audioIndex);
    //videoIndex = av_find_best_stream(pAvFormatContext, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
    //LOGI("最佳视频的索引值 = %d", videoIndex);

    env->ReleaseStringUTFChars(url, _url);
    //关闭 AVFormatContext
    avformat_close_input(&pAvFormatContext);

    return env->NewStringUTF(jsonRoot.toStyledString().c_str());

}