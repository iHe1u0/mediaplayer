package com.imorning.mediaplayer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.util.MimeTypes;
import com.imorning.mediaplayer.R;

public class MainActivity extends BaseActivity {
  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Intent intent=new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(Uri.parse("https://media.w3.org/2010/05/sintel/trailer.mp4"), MimeTypes.VIDEO_MP4);
    startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
