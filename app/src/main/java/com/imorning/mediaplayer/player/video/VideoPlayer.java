package com.imorning.mediaplayer.player.video;

import com.imorning.mediaplayer.player.Player;

public class VideoPlayer extends Player {

    @Override
    public void play(String path) {
        _play(path);
    }

    @Override
    public void pause() {
        _pause();
    }

    @Override
    public void stop() {
        _stop();
    }

    @Override
    public void seekTo(long time) {
        _seekTo(time);
    }

    private native int _play(String path);

    private native int _pause();

    private native int _stop();

    private native int _seekTo(long time);
}
