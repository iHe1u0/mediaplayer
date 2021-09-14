package com.imorning.mediaplayer.player.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.imorning.mediaplayer.player.Player;

import java.util.Arrays;

public class AudioPlayer extends Player {

    private static final String TAG = "AudioPlayer";

    private AudioTrack audioTrack;

    @Override
    public void play() {
        if (getFilePath() == null) {
            Log.e(TAG, "File path is null.");
            return;
        }
        if (!dataSourceFile.exists()) {
            Log.e(TAG, dataSourceFile.getAbsolutePath() + " not found");
            return;
        }
        nativePlay(getFilePath());
    }

    @Override
    public void pause() {
        nativePause();
    }

    @Override
    public void stop() {
        nativeStop();
    }

    public void createTrack(int sampleRateInHn, int nbChannel) {
        int channelConfig;
        if (nbChannel == 2) {
            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        } else {
            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        }
        int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHn, channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHn, channelConfig, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    public void playTrack(byte[] buffer, int length) {
        if (audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.write(buffer, 0, length);
        }
    }

    @Override
    public void seekTo(long time) {
        nativeSeekTo(time);
    }

    private native int nativePlay(String path);

    private native int nativePause();

    private native int nativeStop();

    private native int nativeSeekTo(long time);
}
