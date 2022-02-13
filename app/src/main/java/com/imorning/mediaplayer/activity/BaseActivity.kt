package com.imorning.mediaplayer.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.imorning.mediaplayer.R
import com.imorning.mediaplayer.utils.ui.ToastUtils
import com.permissionx.guolindev.PermissionX

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { _: Boolean, _: List<String?>?, deniedList: List<String?>? ->
                if (deniedList != null) {
                    if (deniedList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if ((this.javaClass.canonicalName)!! == MainActivity::class.qualifiedName) {
                            ToastUtils.showMsg(
                                applicationContext,
                                getString(R.string.permission_warning)
                            )
                        }
                    }
                }
            }
    }

    companion object {
        private const val TAG = "BaseActivity"
    }
}