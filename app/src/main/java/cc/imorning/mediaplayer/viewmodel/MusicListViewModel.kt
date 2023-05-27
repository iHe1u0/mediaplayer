package cc.imorning.mediaplayer.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.imorning.mediaplayer.activity.MusicPlayActivity
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.utils.list.MusicHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicListViewModel : ViewModel() {

    private val _musicItems = MutableStateFlow(mutableListOf<MusicItem>())
    val musicItems: StateFlow<MutableList<MusicItem>> get() = _musicItems

    fun update(context: Context) {
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