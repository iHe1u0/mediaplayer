package cc.imorning.mediaplayer

import android.app.Application
import cc.imorning.mediaplayer.utils.ui.NotificationHelper

class MediaPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.getInstance(this).init()
    }
}