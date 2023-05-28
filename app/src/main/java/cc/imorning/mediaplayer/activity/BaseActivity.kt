package cc.imorning.mediaplayer.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cc.imorning.mediaplayer.R
import cc.imorning.mediaplayer.utils.ui.ToastUtils

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 用户授权请求成功，执行接下来的操作...
                } else {
                    // 动态请求失败，做出相应响应（例如提示用户重新尝试请求、关闭应用程序等），不再可以使用相关功能。
                    ToastUtils.showMsg(this, getString(R.string.permission_warning))
                }
                return
            }
            else -> {
                // 默认情况下，直接传递给super处理，这样不会使代码更复杂
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                return
            }
        }
    }

    private fun requestPermissions() {
        var isPermitted = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isPermitted = false
                break
            }
        }
        if (!isPermitted) {
            ActivityCompat.requestPermissions(
                this, permissions.toTypedArray(), REQUEST_PERMISSION_CODE
            )
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1124
        private val permissions = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }
}