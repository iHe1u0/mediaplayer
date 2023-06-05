package cc.imorning.mediaplayer.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cc.imorning.mediaplayer.IMusicPlayerService
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.utils.TimeUtils
import cc.imorning.mediaplayer.utils.list.MusicHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MusicPlayViewModel"

class MusicPlayViewModel : ViewModel() {

    private lateinit var _musicId: String

    private lateinit var _musicPlayService: IMusicPlayerService

    private val _musicItem = MutableStateFlow(MusicItem())
    val musicItem = _musicItem.asStateFlow()

    private val _currentProgress = MutableStateFlow(0f)
    val currentProgress = _currentProgress.asStateFlow()

    private val _maxSeconds = MutableStateFlow("00:00")
    val maxSecond = _maxSeconds.asStateFlow()

    private val _currentSeconds = MutableStateFlow("00:00")
    val currentSeconds = _currentSeconds.asStateFlow()

    fun init(context: Context, musicPlayService: IMusicPlayerService?) {
        if (musicPlayService == null) {
            return
        }
        _musicPlayService = musicPlayService
        _musicId = _musicPlayService.musicId
        viewModelScope.launch(Dispatchers.IO) {
            val item = MusicHelper.getMusicItem(context, musicId = _musicId)
            _musicItem.emit(item!!)
            _maxSeconds.emit(TimeUtils.getFormattedTime(_musicItem.value.duration))
            withContext(Dispatchers.Main) {
                while (true) {
                    if (_musicPlayService.isPlaying) {
                        updateUI(_musicPlayService.position)
                    }
                    delay(1000)
                }
            }
        }
    }

    private suspend fun updateUI(position: Long) {
        val maxTime = _musicItem.value.duration
        _currentProgress.emit(position.toFloat() / maxTime)
        _currentSeconds.emit(TimeUtils.getFormattedTime(position))
    }

    fun updateTime(newTime: Float) {
        viewModelScope.launch(Dispatchers.Main) {
            _currentProgress.emit(newTime)
            _musicPlayService.seekTo((newTime * _musicItem.value.duration).toLong())
        }
    }

    init {
        Log.i(TAG, "init: $musicItem")
    }
}

class MusicPlayViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicPlayViewModel::class.java)) {
            return MusicPlayViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
