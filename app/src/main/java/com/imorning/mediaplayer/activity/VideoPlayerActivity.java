package com.imorning.mediaplayer.activity;

import android.os.Bundle;

import com.imorning.mediaplayer.R;

import org.libsdl.app.SDLActivity;

public class VideoPlayerActivity extends SDLActivity {
    static {
        System.loadLibrary("videoPlayer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
    }
}