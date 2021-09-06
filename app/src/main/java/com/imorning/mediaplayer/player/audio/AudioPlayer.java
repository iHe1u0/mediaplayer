package com.imorning.mediaplayer.player.audio;

public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    private volatile static AudioPlayer player;

    static {
        System.loadLibrary("audioPlayer");
    }

    public static AudioPlayer getPlayer() {
        if (player == null) {
            synchronized (AudioPlayer.class) {
                if (player == null) {
                    player = new AudioPlayer();
                }
            }
        }
        return player;
    }

    public native int play(String url);

    public native int stop();

}
