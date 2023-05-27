package cc.imorning.mediaplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.imorning.mediaplayer.data.MusicItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicListViewModel : ViewModel() {

    private val _musicItems = MutableStateFlow(mutableListOf<MusicItem>())
    val musicItems: StateFlow<MutableList<MusicItem>> get() = _musicItems

    fun update() {
        viewModelScope.launch {
            val m = mutableListOf<MusicItem>()
            m.add(MusicItem(name = "理想三旬", artists = "陈鸿宇", id = 0))
            _musicItems.emit(m)
        }
    }
}