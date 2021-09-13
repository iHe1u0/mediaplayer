package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;

import com.imorning.mediaplayer.databinding.ActivityMainBinding;
import com.imorning.mediaplayer.player.audio.AudioPlayer;

import org.libsdl.app.SDLActivity;

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
        AudioPlayer player = new AudioPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                player.play(fileRootPath + "mp4");
            }
        }).start();
    }
}