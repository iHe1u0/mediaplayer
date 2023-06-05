package cc.imorning.mediaplayer.utils

import java.util.Locale

object TimeUtils {

    private const val SECONDS_IN_MINUTE = 60

    /**
     * 将给定时间（以毫秒为单位）格式化为 "分:秒" 的字符串形式。
     *
     * @param msTime 给定时间（以毫秒为单位）
     * @return 格式化后的 "分:秒" 字符串形式
     */
    fun getFormattedTime(msTime: Long): String {
        val timeInSeconds = msTime / 1000
        val minutes = timeInSeconds / SECONDS_IN_MINUTE
        val seconds = timeInSeconds % SECONDS_IN_MINUTE
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

}