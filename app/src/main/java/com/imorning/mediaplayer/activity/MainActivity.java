package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;

import com.imorning.mediaplayer.databinding.ActivityMainBinding;
import com.imorning.mediaplayer.player.audio.AudioPlayer;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private final String fileRootPath = Environment.getExternalStorageDirectory().getPath();
    private final String MP3 = "/Piknik - Полюшко-поле.mp3";
    private final String FLAC = "/Rauf & Faik - Колыбельная.flac";
    private final String CRASH_MEDIA = "/short.mp3";
    private ActivityMainBinding mainBinding;
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        audioPlayer = new AudioPlayer();
        audioPlayer.setDataSource(fileRootPath + MP);
        mainBinding.tvInfo.setText(audioPlayer.getMediaInfo());
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioPlayer.play();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}