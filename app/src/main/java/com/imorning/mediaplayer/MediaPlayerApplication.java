package com.imorning.mediaplayer;

import android.app.Application;

import com.danikula.videocache.HttpProxyCacheDebuger;
import com.imorning.mediaplayer.utils.ui.LogUtils;
import com.shuyu.gsyvideoplayer.utils.Debuger;

public class MediaPlayerApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    // if it's a release version, then disable to print logs...
    if (!BuildConfig.DEBUG) {
      LogUtils.disable();
      HttpProxyCacheDebuger.disable();
      Debuger.disable();
    }
  }
}
