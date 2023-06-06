// IMusicPlayerService.aidl
package cc.imorning.mediaplayer;

interface IMusicPlayerService {

    String getMusicId();

    boolean isPlaying();

    void seekTo(long position);

    long getPosition();

    int getRepeatMode();

    void setRepeatMode(int mode);

    int getPlayState();

    void setPlayState(int state);
}