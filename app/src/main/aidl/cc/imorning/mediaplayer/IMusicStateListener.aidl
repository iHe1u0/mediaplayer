// IMusicStateListener.aidl
package cc.imorning.mediaplayer;

interface IMusicStateListener {

    void onPlayingStateChanged(boolean isPlaying);

    void onMusicItemChanged(String name);

}