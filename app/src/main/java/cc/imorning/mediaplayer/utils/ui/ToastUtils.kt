package cc.imorning.mediaplayer.utils.ui

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

object ToastUtils {
    /**
     * show a toast
     */
    @JvmStatic
    fun showMsg(context: Context?, message: String?) {
        if (context == null || TextUtils.isEmpty(message)) {
            return
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}