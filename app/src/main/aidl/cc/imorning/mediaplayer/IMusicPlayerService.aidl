// IMusicPlayerService.aidl
package cc.imorning.mediaplayer;

interface IMusicPlayerService {

    void seekTo(long position);

    long getPosition();

}