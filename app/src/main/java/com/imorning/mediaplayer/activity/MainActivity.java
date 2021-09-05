package com.imorning.mediaplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.utils.Player;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = Player.getPlayer();
        Log.d(TAG, "onCreate: " + player.getVersion());
    }
}