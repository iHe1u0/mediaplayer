package com.imorning.mediaplayer.activity;

import android.os.Bundle;
import android.os.Environment;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.audio.AudioPlayer;

import org.libsdl.app.SDLActivity;

public class MainActivity extends SDLActivity {
    private static final String TAG = "MainActivity";
    private final String fileRootPath = Environment.getExternalStorageDirectory().getPath() + "/1/test/test.";
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioPlayer player = new AudioPlayer();
        player.play(fileRootPath + ".mp3");
    }
}