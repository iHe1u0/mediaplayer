package cc.imorning.mediaplayer.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.R as mediaR
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import cc.imorning.mediaplayer.IMusicPlayerManager
import cc.imorning.mediaplayer.IMusicStateListener
import cc.imorning.mediaplayer.R
import cc.imorning.mediaplayer.activity.MusicPlayActivity
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.utils.list.MusicHelper
import cc.imorning.mediaplayer.utils.ui.NotificationHelper
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.CopyOnWriteArrayList

private const val TAG = "MusicPlayService"

class MusicPlayService : MediaSessionService(), MediaSession.Callback {

    companion object {
        const val MUSIC_ID = "url"
        val NOTIFICATION_ID = NotificationHelper.NotificationID.MusicPlay.ordinal
    }

    private val localMusicBinder = LocalMusicBinder()

    // music id from intent
    private var musicId = "0"
    private lateinit var notificationHelper: NotificationHelper

    // player for media
    private lateinit var player: ExoPlayer

    /**
     * this music item will save information of current music
     */
    private lateinit var musicItem: MusicItem
    private var mediaLibrarySession: MediaLibrarySession? = null
    private var mediaSession: MediaSession? = null

    /**
     * music list
     */
    private lateinit var mediaItems: MutableList<MediaItem>

    private val musicStateListenerList: CopyOnWriteArrayList<IMusicStateListener> =
        CopyOnWriteArrayList()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        // Create media player
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                /* audioAttributes = */ AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                /* handleAudioFocus = */ true
            )
            .build()
        // Create a MediaSessionCompat
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(this)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    1,
                    Intent(this, MusicPlayActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        notificationHelper = NotificationHelper.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra(MUSIC_ID)
        if (command != null) {
            handleCommand(intent.getStringExtra(MUSIC_ID))
        } else {
            handleCommand(null)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return localMusicBinder
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
        mediaLibrarySession

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems =
            mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }

    @OptIn(UnstableApi::class)
    private fun play(url: String?) {
        if (null == url) {
            return
        }
        mediaItems = mutableListOf(MediaItem.fromUri(url))
        if (player.isPlaying) {
            mediaItems.add(MediaItem.fromUri(url))
        }
        player.setMediaItems(mediaItems, true)
        player.prepare()
        player.play()
        // TODO: remove here
        player.repeatMode = Player.REPEAT_MODE_ALL

        val notificationBuilder = notificationHelper.buildMusicPlayingNotification(
            session = mediaSession!!
        )
        val notification = notificationBuilder.build()
        startForeground(NOTIFICATION_ID, notification)
        player.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                for (listener in musicStateListenerList) {
                    listener.onPlayingStateChanged(isPlaying)
                }
                super.onIsPlayingChanged(isPlaying)
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
        for (listener in musicStateListenerList) {
            listener.onMusicItemChanged(musicItem.name)
        }
        if (null == musicId) {
            return
        }
        if (this.musicId != musicId) {
            this.musicId = musicId
            musicItem = MusicHelper.getMusicItem(this, musicId)!!
            play(musicItem.path)
        }
    }

    @UnstableApi
    private inner class MediaNotificationBuilder(mediaNotification: MediaNotification) :
        MediaNotification.Provider {
        val notification = mediaNotification
        override fun createNotification(
            mediaSession: MediaSession,
            customLayout: ImmutableList<CommandButton>,
            actionFactory: MediaNotification.ActionFactory,
            onNotificationChangedCallback: MediaNotification.Provider.Callback
        ): MediaNotification {
            return notification
        }

        override fun handleCustomCommand(
            session: MediaSession,
            action: String,
            extras: Bundle
        ): Boolean {
            return false
        }
    }

    private inner class LocalMusicBinder : IMusicPlayerManager.Stub() {

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

        override fun getRepeatMode(): Int {
            return player.repeatMode
        }

        override fun setRepeatMode(mode: Int) {
            if (mode >= 0 && mode <= Player.REPEAT_MODE_ALL) {
                player.repeatMode = mode
            }
        }

        override fun getPlayState(): Int {
            if (player.isPlaying) {
                return PlayerState.PLAYING.ordinal
            }
            return PlayerState.PAUSE.ordinal
        }

        override fun setPlayState(state: Int) {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        override fun addPlayerStateChangedListener(listener: IMusicStateListener?) {
            if (null != listener && !musicStateListenerList.contains(listener)) {
                musicStateListenerList.add(listener)
            }
        }

        override fun removePlayerStateChangedListener(listener: IMusicStateListener?) {
            if (null != listener) {
                musicStateListenerList.remove(listener)
            }
        }

    }

    override fun onDestroy() {
        stop()
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
        stopSelf()
        super.onDestroy()
    }

    enum class PlayerState {
        STOP, PLAYING, PAUSE, NULL
    }

    enum class PlayerController {
        PREVIOUS, PLAY, PAUSE, NEXT, STOP
    }
}