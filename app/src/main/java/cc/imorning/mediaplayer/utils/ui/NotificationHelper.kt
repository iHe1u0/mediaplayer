package cc.imorning.mediaplayer.utils.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.annotation.OptIn
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.Player
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import cc.imorning.mediaplayer.R
import cc.imorning.mediaplayer.activity.MusicPlayActivity
import com.google.common.collect.ImmutableList

class NotificationHelper private constructor(var context: Context) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: NotificationHelper? = null

        private lateinit var notificationManager: NotificationManager

        fun getInstance(context: Context): NotificationHelper {
            if (null == instance) {
                instance = NotificationHelper(context = context)
                notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            }
            return instance!!
        }
    }

    enum class NotificationID {
        @Deprecated(
            message =
            "for some reason,we can't use 0 for notification id",
            ReplaceWith("Default"), DeprecationLevel.ERROR
        )
        None,
        Default,
        MusicPlay
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    private inner class CustomNotificationProvider(val context: Context, val player: Player) :
        DefaultMediaNotificationProvider(context) {
        override fun getMediaButtons(
            session: MediaSession,
            playerCommands: Player.Commands,
            customLayout: ImmutableList<CommandButton>,
            showPauseButton: Boolean
        ): ImmutableList<CommandButton> {
            val seekToPreviousCommandButton = CommandButton.Builder().apply {
                setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS)
                setIconResId(R.mipmap.ic_left)
                setEnabled(true)
            }.build()
            val playCommandButton = CommandButton.Builder().apply {
                setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
                setIconResId(if (player.isPlaying) R.mipmap.ic_pause else R.mipmap.ic_play)
                setEnabled(true)
            }.build()
            val seekToNextCommandButton = CommandButton.Builder().apply {
                setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT)
                setIconResId(R.mipmap.ic_right)
                setEnabled(true)
            }.build()
            val commandButtons: MutableList<CommandButton> = mutableListOf(
                seekToPreviousCommandButton,
                playCommandButton,
                seekToNextCommandButton
            )
            return ImmutableList.copyOf(commandButtons)
        }
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    fun buildMusicPlayingNotification(session: MediaSession): NotificationCompat.Builder {
        val player = session.player
        val metadata = player.mediaMetadata
        val preAction = NotificationCompat.Action(
            R.drawable.media3_notification_seek_to_previous,
            "Preview",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )
        val playPauseAction = NotificationCompat.Action(
            if (player.isPlaying) R.drawable.media3_notification_pause else R.drawable.media3_notification_play,
            "Play",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        )
        val nextAction = NotificationCompat.Action(
            R.drawable.media3_notification_seek_to_next,
            "Next",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )
        return NotificationCompat.Builder(context, NotificationID.MusicPlay.name).apply {
            setContentTitle(metadata.title)
            setContentText(metadata.albumArtist)
            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.mipmap.ic_media)
            addAction(preAction)
            addAction(playPauseAction)
            addAction(nextAction)
            setStyle(
                MediaStyleNotificationHelper.MediaStyle(session)
                    .setShowCancelButton(false)
                    .setShowActionsInCompactView(0)
            )
            setContentIntent(session.sessionActivity)
        }
    }

    fun notify(id: Int, notification: Notification) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ToastUtils.showMsg(context = context, "请授予通知权限")
            return
        }
        notificationManager.notify(id, notification)
    }

    fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun init() {
        val name = context.getString(R.string.app_name)
        val descriptionText = "正在播放音乐"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        // 构建一个唯一 ID 的 NotificationChannel 实例
        val musicPlayChannel =
            NotificationChannel(NotificationID.MusicPlay.name, name, importance).apply {
                description = descriptionText
            }

        // 在系统管理器中注册新的通知渠道
        notificationManager.createNotificationChannel(musicPlayChannel)

    }

    private fun createRemoteView(@LayoutRes id: Int): RemoteViews {
        return RemoteViews(context.packageName, id)
    }

}