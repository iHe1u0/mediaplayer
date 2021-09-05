package com.imorning.mediaplayer.utils;

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

    private native String getVersion();

    public String avcodec_configuration() {
        return getVersion();
    }
}
