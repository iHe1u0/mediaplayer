// IMusicPlayerService.aidl
package cc.imorning.mediaplayer;

interface IMusicPlayerService {

    String getMusicId();

    boolean isPlaying();

    void seekTo(long position);

    long getPosition();

}