// IMusicPlayerManager.aidl
package cc.imorning.mediaplayer;

import cc.imorning.mediaplayer.IMusicStateListener;

interface IMusicPlayerManager {

    String getMusicId();

    boolean isPlaying();

    void seekTo(long position);

    long getPosition();

    int getRepeatMode();

    void setRepeatMode(int mode);

    int getPlayState();

    void setPlayState(int state);

    void addPlayerStateChangedListener(IMusicStateListener listener);

    void removePlayerStateChangedListener(IMusicStateListener listener);
}