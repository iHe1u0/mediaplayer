package cc.imorning.mediaplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import cc.imorning.mediaplayer.IMusicPlayerService
import cc.imorning.mediaplayer.R
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

    private val _repeatModeIcon = MutableStateFlow(R.mipmap.ic_loop_all)
    val repeatModeIcon = _repeatModeIcon.asStateFlow()

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

    fun updateRepeatMode() {
        var playerRepeatMode = _musicPlayService.repeatMode
        if (playerRepeatMode == Player.REPEAT_MODE_ALL) {
            playerRepeatMode = -1
        }
        _musicPlayService.repeatMode = ++playerRepeatMode
        viewModelScope.launch(Dispatchers.Main) {
            when (playerRepeatMode) {
                Player.REPEAT_MODE_ALL -> {
                    _repeatModeIcon.emit(R.mipmap.ic_loop_all)
                }

                Player.REPEAT_MODE_OFF -> {
                    _repeatModeIcon.emit(R.mipmap.ic_no_loop)
                }

                Player.REPEAT_MODE_ONE -> {
                    _repeatModeIcon.emit(R.mipmap.ic_loop_one)
                }
            }
        }
    }

    fun updatePlayState() {
        _musicPlayService.playState = 0
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
