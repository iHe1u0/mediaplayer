package cc.imorning.mediaplayer.service

import android.content.Intent
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.annotation.OptIn
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.RepeatModeUtil
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import cc.imorning.mediaplayer.IMusicPlayerService
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.utils.list.MusicHelper
import cc.imorning.mediaplayer.utils.ui.NotificationHelper
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val TAG = "MusicPlayService"

private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class MusicPlayService : MediaBrowserServiceCompat(), MediaSession.Callback {

    companion object {
        const val MUSIC_ID = "url"
        val NOTIFICATION_ID = NotificationHelper.NotificationID.MusicPlay.ordinal
    }

    // music id from intent
    private var musicId = "0"
    private lateinit var notificationHelper: NotificationHelper

    // player for media
    private lateinit var player: ExoPlayer

    /**
     * this music item will save information of current music
     */
    private lateinit var musicItem: MusicItem
    private var mediaSession: MediaSession? = null

    /**
     * music list
     */
    private lateinit var mediaItems: MutableList<MediaItem>

    /**
     * play state
     */
    private var isPlaying = false

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        // Create media player
        player = ExoPlayer.Builder(this).build()
        // Create a MediaSessionCompat
        mediaSession =
                MediaSession.Builder(this, player).setCallback(LocalMusicSessionCallback()).build()
        notificationHelper = NotificationHelper.getInstance(this)
    }

    private inner class LocalMusicSessionCallback : MediaSession.Callback

    private val localMusicBinder = LocalMusicBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleCommand(intent?.getStringExtra(MUSIC_ID))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return localMusicBinder
    }

    override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems =
                mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }

    override fun onGetRoot(
            clientPackageName: String,
            clientUid: Int,
            rootHints: Bundle?
    ): BrowserRoot {
        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        return BrowserRoot(MY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
            parentMediaId: String,
            result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        //  Browsing not allowed
        if (MY_EMPTY_MEDIA_ROOT_ID == parentMediaId) {
            result.sendResult(null)
            return
        }

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == parentMediaId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        // result.sendResult(mediaItems)
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun play(url: String?) {
        if (null == url) {
            return
        }
        mediaItems = mutableListOf(MediaItem.fromUri(url))
        if (player.isPlaying) {
            // stop()
            // player.clearMediaItems()
            mediaItems.add(MediaItem.fromUri(url))
        }
        player.setMediaItems(mediaItems, true)
        player.prepare()
        player.play()
        // TODO: remove here
        player.repeatMode = Player.REPEAT_MODE_ALL
        val notification = notificationHelper.buildMusicPlayingNotification(
                musicName = musicItem.name,
                artist = musicItem.artists,
                mediaSession = mediaSession
        )
        startForeground(NOTIFICATION_ID, notification.build())

        player.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
            ) {
                Log.i(
                        TAG,
                        "onPositionDiscontinuity: ${oldPosition.positionMs} ${newPosition.positionMs} $reason"
                )
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            }
        })
    }

    private fun pause() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    private fun stop() {
        if (player.isPlaying) {
            player.stop()
        }
    }

    private fun handleCommand(musicId: String?) {
        if (null == musicId) {
            return
        }
        this.musicId = musicId
        musicItem = MusicHelper.getMusicItem(this, musicId)!!
        play(musicItem.path)
    }

    private inner class LocalMusicBinder : IMusicPlayerService.Stub() {

        fun getMusicService(): MusicPlayService {
            return this@MusicPlayService
        }

        override fun asBinder(): IBinder {
            return this
        }

        override fun getMusicId(): String {
            return this@MusicPlayService.musicId
        }

        @Synchronized
        override fun seekTo(position: Long) {
            if (position >= 0) {
                player.seekTo(position)
            }
        }

        @Synchronized
        override fun getPosition(): Long {
            if (player.isPlaying) {
                return player.currentPosition
            }
            return 0
        }

        override fun isPlaying(): Boolean {
            return player.playbackState != Player.STATE_ENDED
        }
    }

    override fun onDestroy() {
        stop()
        player.release()
        mediaSession?.release()
        mediaSession = null
        stopSelf()
        super.onDestroy()
    }
}