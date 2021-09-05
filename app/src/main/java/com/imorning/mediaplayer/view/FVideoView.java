package com.imorning.mediaplayer.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.imorning.mediaplayer.player.video.VideoPlayer;

public class FVideoView extends SurfaceView {
    private static final String TAG = "FVideoPlayer";
    private VideoPlayer videoPlayer;
    private Surface surface;

    public FVideoView(Context context) {
        this(context, null);
    }

    public FVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHolder().setFormat(PixelFormat.RGBA_8888);
        surface = getHolder().getSurface();
        videoPlayer = VideoPlayer.getPlayer();

    }

    public void play(String videoPath) {
        new Thread(() -> {
            Log.d(TAG, "------>>调用native方法");
            videoPlayer.play(videoPath, surface);
        }).start();
    }
}
