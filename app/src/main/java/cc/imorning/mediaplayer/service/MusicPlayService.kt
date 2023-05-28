package cc.imorning.mediaplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

private const val TAG = "MusicPlayService"

class MusicPlayService : Service() {

    companion object {
        const val URL = "url"
    }

    private var player: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleCommand(intent?.getStringExtra(URL))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    private fun play(url: String): Unit {
        if (player?.isPlaying == true) {
            stop()
        }
        player = MediaPlayer()
        player!!.apply {
            setDataSource(url)
            prepare()
            start()
        }
    }

    private fun pause() {
        if (player?.isPlaying == true) {
            player?.pause()
        }
    }

    private fun stop(): Unit {
        if (player?.isPlaying == true) {
            player!!.apply {
                stop()
                release()
            }
            player = null
        }
    }

    private fun handleCommand(url: String?) {
        if (url == null) {
            return
        }
        play(url)
    }
}