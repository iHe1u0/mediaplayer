package com.imorning.mediaplayer.player.video;

import android.view.Surface;

import com.imorning.mediaplayer.utils.Jni;

public class VideoPlayer {
    private static final String TAG = "VideoPlayer";
    public static Jni jni;
    private volatile static VideoPlayer player;

    static {
        jni = new Jni();
    }

    public static VideoPlayer getPlayer() {
        if (player == null) {
            synchronized (VideoPlayer.class) {
                if (player == null) {
                    player = new VideoPlayer();
                }
            }
        }
        return player;
    }

    public void play(String videoPath, Surface surface) {
        jni.playVideo(videoPath, surface);
    }
}
