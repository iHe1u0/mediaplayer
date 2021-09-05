package com.imorning.mediaplayer.utils;

import android.content.Context;
import android.util.Log;

public class Player {
    private static final String TAG = "PlayerUtils";
    public static Jni jni;
    private volatile static Player player;

    static {
        jni = new Jni();
    }

    private Player() {
    }

    public static Player getPlayer() {
        if (player == null) {
            synchronized (Player.class) {
                if (player == null) {
                    player = new Player();
                }
            }
        }
        return player;
    }

    public String getVersion() {
        return jni.avcodec_configuration();
    }
}
