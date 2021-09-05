package com.imorning.mediaplayer.utils;

import android.view.Surface;

public class Jni {
    static {
        System.loadLibrary("avcodec");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("postproc");
        System.loadLibrary("ffmpegutils");

    }

    /**
     * get avcodec configure
     *
     * @return avcodec configure
     */
    public native String getVersion();

    /**
     * 播放视频流
     *
     * @param videoPath（本地）视频文件路径
     * @param surface
     */
    public native void playVideo(String videoPath, Surface surface);
}
