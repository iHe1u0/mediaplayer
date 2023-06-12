package cc.imorning.mediaplayer.utils.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
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
        mediaSessionToken: MediaSession?
    ): NotificationCompat.Builder {

        val channelID = NotificationID.MusicPlay.name

        val builder = NotificationCompat.Builder(context, channelID).apply {
            setSubText(artist)
            setContentTitle(musicName)
            setContentText(artist)
            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.mipmap.ic_media)
            setStyle(mediaSessionToken?.let {
                androidx.media3.session.MediaStyleNotificationHelper.MediaStyle(
                    it
                )
            })
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_seek_to_previous,
                    "上一首",
                    null
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_pause,
                    "暂停",
                    null
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.media3_notification_seek_to_next,
                    "下一首",
                    null
                )
            )

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