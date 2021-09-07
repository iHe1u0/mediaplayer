package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.audio.AudioPlayer;
import com.imorning.mediaplayer.utils.YuvUtils;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioPlayer = AudioPlayer.getPlayer();
        /**
         * new Thread(new Runnable() {
        @Override public void run() {
        int code = audioPlayer.play(Environment.getExternalStorageDirectory().getPath()
        + "/test.mp3");
        Log.d(TAG, "run: " + code);
        }
        }).start();
         **/
        YuvUtils yuvUtils = new YuvUtils();
        yuvUtils.sayHello();
    }

    @Override
    protected void onDestroy() {
        audioPlayer.stop();
        super.onDestroy();

    }
}