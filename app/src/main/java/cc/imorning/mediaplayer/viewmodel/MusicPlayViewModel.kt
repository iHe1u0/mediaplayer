package cc.imorning.mediaplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import cc.imorning.media.network.music.MusicInfoHelper
import cc.imorning.mediaplayer.IMusicPlayerManager
import cc.imorning.mediaplayer.IMusicStateListener
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

    private lateinit var _playerManager: IMusicPlayerManager

    /**
     * stop update ui when music pause or stop
     */
    private var needUpdateUI = false

    private val _musicItem = MutableStateFlow(MusicItem())
    val musicItem = _musicItem.asStateFlow()

    /**
     * value for SeekBar, value is between 0.0 ~ 1.0
     */
    private val _currentProgress = MutableStateFlow(0f)
    val currentProgress = _currentProgress.asStateFlow()

    /**
     * real time of current position
     */
    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition = _currentPosition.asStateFlow()

    private val _maxTimeText = MutableStateFlow("00:00")
    val maxTimeText = _maxTimeText.asStateFlow()

    private val _currentTimeText = MutableStateFlow("00:00")
    val currentTimeText = _currentTimeText.asStateFlow()

    private val _repeatModeIcon = MutableStateFlow(R.mipmap.ic_loop_all)
    val repeatModeIcon = _repeatModeIcon.asStateFlow()

    private val _playStateIcon = MutableStateFlow(R.mipmap.ic_play)
    val playStateIcon = _playStateIcon.asStateFlow()

    private val _lyric = MutableStateFlow("")
    val lyric = _lyric.asStateFlow()

    /**
     * music player state listener
     */
    private val playerStateListener: IMusicStateListener = object : IMusicStateListener.Stub() {

        override fun onPlayingStateChanged(isPlaying: Boolean) {
            viewModelScope.launch {
                needUpdateUI = if (isPlaying) {
                    _playStateIcon.emit(R.mipmap.ic_pause)
                    true
                } else {
                    _playStateIcon.emit(R.mipmap.ic_play)
                    false
                }
            }
        }

        override fun onMusicItemChanged(name: String?) {
            initLyric(name)
        }

    }

    fun init(context: Context, manager: IMusicPlayerManager) {
        _playerManager = manager
        _musicId = _playerManager.musicId
        _playerManager.addPlayerStateChangedListener(playerStateListener)
        viewModelScope.launch(Dispatchers.IO) {
            val item = MusicHelper.getMusicItem(context, musicId = _musicId)
            _musicItem.emit(item!!)
            _maxTimeText.emit(TimeUtils.getFormattedTime(_musicItem.value.duration))
            initLyric(item.name)
            val maxTime = _musicItem.value.duration
            withContext(Dispatchers.Main) {
                while (true) {
                    if (needUpdateUI) {
                        _currentPosition.emit(_playerManager.position.toFloat())
                        updateUI(maxTime, _currentPosition.value.toLong())
                    }
                    delay(1000)
                }
            }
        }
    }

    private fun initLyric(name: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val lyricApiId = MusicInfoHelper.getMusicId(name.orEmpty())
            var lyricData = MusicInfoHelper.getLyricValue(lyricApiId)
            if (lyricData.isNullOrEmpty()) {
                lyricData = "[999:99]暂无歌词"
            }
            _lyric.emit(lyricData)
        }
    }

    private suspend fun updateUI(max: Long, current: Long) {
        _currentProgress.emit(current.toFloat() / max.toFloat())
        _currentTimeText.emit(TimeUtils.getFormattedTime(current))
    }

    fun updateTime(newTime: Float) {
        viewModelScope.launch(Dispatchers.Main) {
            _currentProgress.emit(newTime)
            _playerManager.seekTo((newTime * _musicItem.value.duration).toLong())
        }
    }

    fun updateRepeatMode() {
        var playerRepeatMode = _playerManager.repeatMode
        if (playerRepeatMode == Player.REPEAT_MODE_ALL) {
            playerRepeatMode = -1
        }
        _playerManager.repeatMode = ++playerRepeatMode
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
        _playerManager.playState = 0
    }
}

class MusicPlayViewModelFactory : ViewModelProvider.Factory {
    @SuppressWarnings("Unchecked")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicPlayViewModel::class.java)) {
            return MusicPlayViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
