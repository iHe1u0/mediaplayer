package cc.imorning.mediaplayer.utils.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.session.MediaSession
import cc.imorning.mediaplayer.R

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
        Default, MusicPlay
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun buildMusicPlayingNotification(
        musicName: String?,
        artist: String?,
        mediaSession: MediaSession?
    ): NotificationCompat.Builder {

        val controller = mediaSession?.connectedControllers
        val channelID = NotificationID.MusicPlay.name

        val builder = NotificationCompat.Builder(context, channelID).apply {
            setSubText(artist)
            setContentTitle(musicName)
            setContentText(artist)
            // Stop the service when the notification is swiped away
            setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.mipmap.ic_media)
            // Add an app icon and set its accent color
            // Be careful about the color
            // color = Color.BLUE
            // Add controls button
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_seek_to_previous,
                    "上一首",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    )
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_pause,
                    "暂停",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_PAUSE
                    )
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_seek_to_next,
                    "下一首",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    )
                )
            )
            // Take advantage of MediaStyle features
            if (mediaSession != null) {
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionCompatToken)
                        .setShowActionsInCompactView(1)
                        // Add a cancel button
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                context,
                                PlaybackStateCompat.ACTION_STOP
                            )
                        )
                )
            }
        }

        return builder
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