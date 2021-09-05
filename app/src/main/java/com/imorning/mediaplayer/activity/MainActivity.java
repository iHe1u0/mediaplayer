package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.video.VideoPlayer;
import com.imorning.mediaplayer.view.FVideoView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";


    private FVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.testVideoPlayer);
        videoView.play("/sdcard/test.mp4");
    }
}