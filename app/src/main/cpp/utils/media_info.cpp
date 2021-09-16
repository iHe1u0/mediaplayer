//
// Created by iMorning on 2021/9/14.
//
#include <jni.h>
#include <log.h>
#include <string>

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
    string jsonBuilder;
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
    //媒体流数量率
    int nb_streams = pAvFormatContext->nb_streams;

    //时长
    int64_t duration = pAvFormatContext->duration / AV_TIME_BASE;

    //媒体路径
    char *fileAbsPath = pAvFormatContext->url;

    //首帧的时间
    int64_t start_time = pAvFormatContext->start_time / AV_TIME_BASE;

    jsonBuilder.append("{\n");
    jsonBuilder.append("文件路径:").append(fileAbsPath).append(";\n");
    jsonBuilder.append("解码器:").append(pAvFormatContext->iformat->name).append(";\n");
    jsonBuilder.append("首帧时间:").append(to_string(start_time)).append(";\n");
    jsonBuilder.append("媒体流数量:").append(to_string(nb_streams)).append(";\n");
    jsonBuilder.append("总时长:").append(to_string(duration)).append(";\n");
    //拿到音视频流的相关信息
    int audioIndex = 0;
    int videoIndex = 0;

    jsonBuilder.append("streamInfo:{\n");
    //=========================方式1：获取音视频索引=========================================
    for (int i = 0; i < pAvFormatContext->nb_streams; i++) {
        jsonBuilder.append("streamId:").append(to_string(i)).append(";\n");
        AVStream *pAvStream = pAvFormatContext->streams[i];
        //拿到流的编码器参数指针
        AVCodecParameters *avCodecParameters = pAvStream->codecpar;

        //当前的 AVStream 是视频流
        if (avCodecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {
            //avCodecParameters->bit_rate为0，代表着这是一个音频文件
            if (avCodecParameters->bit_rate == 0) {
                jsonBuilder.append("isVideo:false;\n");
                LOGI("%s", "This is not a video file.");
            }
            jsonBuilder.append("媒体类型:").append("视频").append(";\n");
            //视频对应的 format 就是  AVPixelFormat,codec_id 编码器的 id ，例如 h264,aac
            LOGI("视频帧率 = %d", pAvStream->avg_frame_rate.num);
            LOGI("视频宽度 = %d", avCodecParameters->width);
            LOGI("视频高度 = %d", avCodecParameters->height);
            LOGI("视频pixel_format = %d", avCodecParameters->format);
            LOGI("视频编解码器 = %s", avcodec_get_name(avCodecParameters->codec_id));
            LOGI("视频bit_rate %ld kb/s", avCodecParameters->bit_rate / 1000);
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioIndex = i;
            jsonBuilder.append("媒体类型:").append("音频").append(";\n");
            jsonBuilder.append("当前播放索引:").append(to_string(audioIndex)).append(";\n");
            jsonBuilder.append("音频采样率:").append(to_string(avCodecParameters->sample_rate)).append(
                    ";\n");
            jsonBuilder.append("音频 bit_rate:").append(
                    to_string(avCodecParameters->bit_rate / 1000)).append("kb/s").append(";\n");
            jsonBuilder.append("音频通道数:").append(to_string(avCodecParameters->channels)).append(
                    ";\n");
            jsonBuilder.append("音频编解码器:").append(avcodec_get_name(avCodecParameters->codec_id))
                    .append(";\n");
            jsonBuilder.append("音频采样格式:").append(
                            av_get_sample_fmt_name((AVSampleFormat) avCodecParameters->format))
                    .append(";\n");
            jsonBuilder.append("音频音频帧大小:")
                    .append(to_string(avCodecParameters->frame_size)).append(";\n");
        }
        jsonBuilder.append("}\n");
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
    jsonBuilder.append("}\n");
    return env->NewStringUTF(jsonBuilder.c_str());

}