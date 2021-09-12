package com.imorning.mediaplayer.player;


public abstract class Player {
    static {
        System.loadLibrary("Player");
    }

    /**
     * play media
     *
     * @param path File path
     */
    public abstract void play(String path);

    /**
     * pause
     */
    public abstract void pause();

    /**
     * stop Play and release resource
     */
    public abstract void stop();

    /**
     * seek to time
     *
     * @param time time format to seconds
     */
    public abstract void seekTo(long time);
}
