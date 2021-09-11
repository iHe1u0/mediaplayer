package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.audio.AudioPlayer;
import com.imorning.mediaplayer.player.video.VideoPlayer;

import org.libsdl.app.SDLActivity;

public class MainActivity extends SDLActivity {
    private static final String TAG = "MainActivity";
    private final String fileRootPath = Environment.getExternalStorageDirectory().getPath() + "/1/test/test.";
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioPlayer = AudioPlayer.getPlayer();
        int code = VideoPlayer.nativeInit(fileRootPath + "mp4");
        Log.d(TAG, "onCreate: " + code);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = audioPlayer.play(fileRootPath + "mp3");
                Log.d(TAG, "run: " + code);
            }
        });//.start();

    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
        super.onDestroy();

    }
}