package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.audio.AudioPlayer;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioPlayer = AudioPlayer.getPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = audioPlayer.play(Environment.getExternalStorageDirectory().getPath()
                        + "/夏日回音.flac");
                Log.d(TAG, "run: " + code);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        audioPlayer.stop();
        super.onDestroy();

    }
}