package cc.imorning.mediaplayer.utils

import android.text.TextUtils
import android.util.Log

object LogUtils {
    const val LOG_TAG = "iMorning"
    private var debugMode = true

    fun enable() {
        debugMode = true
    }

    @JvmStatic
    fun disable() {
        debugMode = false
    }

    @JvmStatic
    fun d(tag: String?, log: String?) {
        if (debugMode) {
            if (!TextUtils.isEmpty(log)) Log.d(tag, log!!)
        }
    }

    @JvmStatic
    fun i(tag: String?, log: String?) {
        if (debugMode) {
            if (!TextUtils.isEmpty(log)) Log.i(tag, log!!)
        }
    }

    @JvmStatic
    fun w(tag: String?, log: String?) {
        if (debugMode) {
            if (!TextUtils.isEmpty(log)) Log.w(tag, log!!)
        }
    }

    @JvmStatic
    fun e(tag: String?, log: String?) {
        if (debugMode) {
            if (!TextUtils.isEmpty(log)) Log.e(tag, log!!)
        }
    }
}