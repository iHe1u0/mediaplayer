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

    private lateinit var player: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initPlayer(intent?.getStringExtra(URL))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    private fun initPlayer(url: String?) {
        if (url == null) {
            return
        }
        player = MediaPlayer()
        player.setDataSource(url)
        player.prepare()
        player.start()
    }
}