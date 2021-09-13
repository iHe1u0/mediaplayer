package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.imorning.mediaplayer.databinding.ActivityMainBinding;
import com.imorning.mediaplayer.player.audio.AudioPlayer;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private final String fileRootPath = Environment.getExternalStorageDirectory().getPath() + "/1/test/test.";
    private ActivityMainBinding mainBinding;
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        audioPlayer = new AudioPlayer();
        audioPlayer.setFilePath(fileRootPath + "mp3");
        mainBinding.tvInfo.setText(audioPlayer.getFilePath());
        new Thread(() -> {
            audioPlayer.play();
        }).start();

    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
        super.onDestroy();
    }
}