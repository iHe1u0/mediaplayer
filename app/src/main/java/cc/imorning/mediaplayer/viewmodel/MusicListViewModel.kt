package cc.imorning.mediaplayer.viewmodel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.imorning.mediaplayer.activity.MusicPlayActivity
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.utils.list.MusicHelper
import cc.imorning.mediaplayer.utils.ui.ToastUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicListViewModel : ViewModel() {

    private val _musicItems = MutableStateFlow(mutableListOf<MusicItem>())
    val musicItems: StateFlow<MutableList<MusicItem>> get() = _musicItems

    fun update(context: Context) {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showMsg(context, "请授予读写存储的权限")
            return
        }
        viewModelScope.launch {
            val medias = MusicHelper.getAllMusic(context = context)
            if (medias.isNotEmpty()) {
                _musicItems.value = medias
            }
        }
    }

    fun play(context: Context, musicItem: MusicItem) {
        val musicPlayActivity = Intent(context, MusicPlayActivity::class.java)
        musicPlayActivity.putExtra(MusicPlayActivity.ITEM, musicItem)
        context.startActivity(musicPlayActivity)
    }
}