package cc.imorning.mediaplayer.viewmodel

import android.content.ServiceConnection
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cc.imorning.mediaplayer.data.MusicItem

private const val TAG = "MusicPlayViewModel"

class MusicPlayViewModel(
    private val musicItem: MusicItem
) : ViewModel() {

    fun init(serviceConnection: ServiceConnection) {

    }

    init {
        Log.i(TAG, "init: $musicItem")
    }
}

class MusicPlayViewModelFactory(private val musicItem: MusicItem) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicPlayViewModel::class.java)) {
            return MusicPlayViewModel(musicItem = musicItem) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
