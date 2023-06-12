package cc.imorning.mediaplayer.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import cc.imorning.mediaplayer.IMusicPlayerManager
import cc.imorning.mediaplayer.R
import cc.imorning.mediaplayer.activity.ui.component.LyricsUI
import cc.imorning.mediaplayer.activity.ui.theme.MediaTheme
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.service.MusicPlayService
import cc.imorning.mediaplayer.viewmodel.MusicPlayViewModel
import cc.imorning.mediaplayer.viewmodel.MusicPlayViewModelFactory

private const val TAG = "MusicPlayActivity"

class MusicPlayActivity : BaseActivity() {

    private lateinit var viewModel: MusicPlayViewModel
    private var musicItem: MusicItem? = null

    /**
     * whether bind service
     */
    private var isBound = false

    private var musicPlayerManager: IMusicPlayerManager? = null

    // private val
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicPlayerManager = IMusicPlayerManager.Stub.asInterface(service)
            viewModel.init(this@MusicPlayActivity, musicPlayerManager!!)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        musicItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ITEM, MusicItem::class.java)
        } else {
            intent.getParcelableExtra(ITEM)
        }

        viewModel =
            ViewModelProvider(this, MusicPlayViewModelFactory())[MusicPlayViewModel::class.java]

        val service = Intent(this, MusicPlayService::class.java)
        service.putExtra(MusicPlayService.MUSIC_ID, musicItem!!.id)
        ContextCompat.startForegroundService(this, service)
        bindService(service, serviceConnection, Context.BIND_AUTO_CREATE)

        setContent {
            MediaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayScreen(viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        super.onDestroy()
    }

    companion object {
        const val ITEM: String = "item"
    }

}

@Composable
fun MusicPlayScreen(viewModel: MusicPlayViewModel, modifier: Modifier = Modifier) {

    val musicInfo = viewModel.musicItem.collectAsState()

    val currentPosition = viewModel.currentPosition.collectAsState()
    val lyric = viewModel.lyric.collectAsState()

    Column(modifier = modifier) {
        Text(
            text = musicInfo.value.name.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = musicInfo.value.artists.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
        Box(
            Modifier
                .fillMaxWidth()
                .weight(weight = 0.5f, fill = true)
                .align(Alignment.CenterHorizontally)
        ) {
            // AsyncImage(
            //     model = "https://p2.music.126.net/ryk8Gu64rOhlYn0pc2Q8Ww==/109951168090271827.jpg",
            //     contentDescription = "歌曲封面",
            // )
            LyricsUI(liveTime = currentPosition.value.toLong(), lyricsEntryList = lyric.value)
        }
        ProgressContent(viewModel)
        PlayerControlView(viewModel)
    }

}

@Composable
fun ProgressContent(viewModel: MusicPlayViewModel) {


    val position = viewModel.currentProgress.collectAsState()
    val maxTime = viewModel.maxTimeText.collectAsState()
    val currentTime = viewModel.currentTimeText.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Text(
            text = currentTime.value,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )
        Slider(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            value = position.value,
            onValueChange = {
                viewModel.updateTime(it)
            }
        )
        Text(
            text = maxTime.value,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )
    }

}

@Composable
fun PlayerControlView(viewModel: MusicPlayViewModel) {

    val repeatIcon = viewModel.repeatModeIcon.collectAsState()
    val playStateIcon = viewModel.playStateIcon.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.updateRepeatMode()
            }) {
                Icon(
                    painter = painterResource(id = repeatIcon.value),
                    contentDescription = "循环播放"
                )
            }
            IconButton(onClick = { /* 执行第一个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_left),
                    contentDescription = "上一首"
                )
            }
            IconButton(onClick = {
                viewModel.updatePlayState()
            }) {
                Icon(
                    painter = painterResource(id = playStateIcon.value),
                    contentDescription = "播放"
                )
            }
            IconButton(onClick = { /* 执行第三个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_right),
                    contentDescription = "下一首"
                )
            }
            IconButton(onClick = { /* 执行第三个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_playlist),
                    contentDescription = "播放列表"
                )
            }
        }
    }

}