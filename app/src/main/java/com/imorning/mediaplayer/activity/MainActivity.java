package com.imorning.mediaplayer.activity;

import android.os.Bundle;

import com.imorning.mediaplayer.R;

public class MainActivity extends BaseActivity {
  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
