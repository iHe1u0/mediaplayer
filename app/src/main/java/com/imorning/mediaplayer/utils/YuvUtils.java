package com.imorning.mediaplayer.utils;

public class YuvUtils {
    static {
        System.loadLibrary("yuvutils");
    }

    public native void sayHello();
}
