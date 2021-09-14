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
Java_com_imorning_mediaplayer_player_Player_nativeGetMediaInfo(JNIEnv *env, jobject thiz,
                                                               jstring url) {
    string info;
    // 注册所有的封装和解封装格式
    av_register_all();

    // 初始化网络组件
    avformat_network_init();

    // 注册编解码器
    avcodec_register_all();

    // 打开指定的输入流地址
    // 指针定义之后，一定要赋值才能使用，不然会抛出错误地址异常
    AVFormatContext *avFormatContext = NULL;

    const char *_url = env->GetStringUTFChars(url, 0);

    //【连接服务器的连接和码流信息头的读取】
    // 打开输入流，并且读取流的头信息。读取出来的相关信息，会保存到 AVFormatContext 上下文中。
    /*
     * 参数1：封装格式上下文，要注意这个需要记得释放。
     * 参数2：需要打开的输入流地址，这个地址支持 http,rtsp(摄像头)，本地文件地址。这个地址会被保存到 AVFormatContext 中
     * 参数3：AVInputFormat 指定输入的格式，一般不指定，可以传0
     * 参数4：AVDictionary 一组 key-value 配置，一般不指定，可以传0
     */

    int ret = avformat_open_input(&avFormatContext, _url, 0, 0);

    if (ret != 0) {
        //加入本地传入的文件地址找不到，会报：avformat_open_input failed：No such file or directory
        LOGE("avformat_open_input failed：%s", av_err2str(ret));
        return (jstring) "avformat_open_input failed";
    }

    //通过 avformat_open_input 可能读取不到以下两个值，如果获取不到，可以通过 avformat_find_stream_info 来探测流，从而获取流的相关信息
    // int nb_streams = avFormatContext->nb_streams;
    // long long duration = avFormatContext->duration;
    // LOGI("nb_streams is %d,duration is %lld", nb_streams, duration);


    //【探测】如果通过 avformat_open_input 无法读取到流的头信息，那么可以通过 avformat_find_stream_info 获取流信息
    ret = avformat_find_stream_info(avFormatContext, 0);
    if (ret < 0) {
        LOGE("avformat_find_stream_info failed:%s", av_err2str(ret));
        return (jstring) "avformat_find_stream_info failed";
    }
    int nb_streams = avFormatContext->nb_streams;
    int64_t duration = avFormatContext->duration / AV_TIME_BASE;
    char *filename = avFormatContext->url;
    LOGI("name=%s", avFormatContext->iformat->name);
    //首帧的时间
    int64_t start_time = avFormatContext->start_time / AV_TIME_BASE;
    LOGI("nb_streams is %d", nb_streams);
    LOGI("duration is %ld", duration);
    LOGI("文件路径 = %s", filename);
    LOGI("开始时间 = %ld", start_time);
    //拿到音视频流的相关信息
    int audioIndex = 0;
    int videoIndex = 0;

    //=========================方式1：获取音视频索引=========================================
    for (int i = 0; i < avFormatContext->nb_streams; i++) {

        AVStream *as = avFormatContext->streams[i];
        //拿到流的编码器参数指针
        AVCodecParameters *avCodecParameters = as->codecpar;

        //当前的 AVStream 是视频流
        if (avCodecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoIndex = i;
            LOGI("当前是视频数据，索引是%d", videoIndex);
            //视频对应的 format 就是  AVPixelFormat,codec_id 编码器的 id ，例如 h264,aac
            //LOGI("视频帧率 = %d", r2d(as->avg_frame_rate));
            LOGI("视频宽度 = %d", avCodecParameters->width);
            LOGI("视频高度 = %d", avCodecParameters->height);
            LOGI("视频pixel_format = %d", avCodecParameters->format);
            LOGI("视频编解码器 = %s", avcodec_get_name(avCodecParameters->codec_id));
            LOGI("视频bit_rete %ld kb/s", avCodecParameters->bit_rate / 1000);
        } else if (avCodecParameters->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioIndex = i;
            LOGI("当前是音频数据，索引 = %d", audioIndex);
            LOGI("音频采样率 = %d", avCodecParameters->sample_rate);
            LOGI("音频 bit_rate = %ld kb/s", avCodecParameters->bit_rate / 1000);
            LOGI("音频通道数 = %d", avCodecParameters->channels);
            LOGI("音频编解码器 = %s", avcodec_get_name(avCodecParameters->codec_id));
            LOGI("音频采样格式 = %s", av_get_sample_fmt_name((AVSampleFormat) avCodecParameters->format));
            LOGI("音频音频帧大小 = %d", avCodecParameters->frame_size); //音频音频帧大小 = 1024

        }
    }

    //=========================方式2：获取音视频索引=========================================
    //如果在前面没有得到视频或者音频的索引值，那么可以通过以下的 api 获取
    audioIndex = av_find_best_stream(avFormatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    LOGI("最佳音频的索引值 = %d", audioIndex);

    videoIndex = av_find_best_stream(avFormatContext, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
    LOGI("最佳视频的索引值 = %d", videoIndex);

    env->ReleaseStringUTFChars(url, _url);

    //关闭 AVFormatContext
    avformat_close_input(&avFormatContext);
    return NULL;

}