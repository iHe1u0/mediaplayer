package com.imorning.mediaplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.imorning.mediaplayer.R;
import com.imorning.mediaplayer.player.video.VideoPlayer;
import com.imorning.mediaplayer.utils.ui.LogUtils;

public class VideoPlayerActivity extends BaseActivity {

  private static final String TAG = "VideoPlayerActivity";
  private String filePath = null;
  private VideoPlayer videoPlayer;

  // record play state, replay video if it's pause by onStop
  private boolean isPauseByOnStop = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_player);
    videoPlayer = findViewById(R.id.video_player);
    Intent intent = getIntent();
    if (intent != null) {
      filePath = getIntent().getDataString();
      videoPlayer.setUp(filePath, true, filePath);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.activity_video_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (videoPlayer != null && isPauseByOnStop) {
      videoPlayer.onVideoResume();
      isPauseByOnStop = false;
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (videoPlayer != null && videoPlayer.isInPlayingState()) {
      LogUtils.i(TAG, "video is playing and try to pause");
      videoPlayer.onVideoPause();
      isPauseByOnStop = true;
    }
  }
}
