package com.imorning.mediaplayer;

import android.util.Log;

import io.flutter.app.FlutterApplication;

public class MediaPlayerApp extends FlutterApplication {
  private static final String TAG = "MediaPlayerApp";

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "onCreate: start App");
  }
}
