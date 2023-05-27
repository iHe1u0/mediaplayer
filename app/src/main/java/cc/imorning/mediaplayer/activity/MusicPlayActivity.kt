package cc.imorning.mediaplayer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import cc.imorning.mediaplayer.R
import cc.imorning.mediaplayer.activity.ui.theme.MediaTheme
import cc.imorning.mediaplayer.data.MusicItem
import cc.imorning.mediaplayer.service.MusicPlayService
import cc.imorning.mediaplayer.viewmodel.MusicPlayViewModel
import cc.imorning.mediaplayer.viewmodel.MusicPlayViewModelFactory
import coil.compose.AsyncImage

private const val TAG = "MusicPlayActivity"

class MusicPlayActivity : BaseActivity() {

    private lateinit var viewModel: MusicPlayViewModel
    private var musicItem: MusicItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        musicItem = intent.getParcelableExtra(ITEM)
        viewModel = ViewModelProvider(
            this,
            MusicPlayViewModelFactory(musicItem = musicItem!!)
        )[MusicPlayViewModel::class.java]

        val service = Intent(this, MusicPlayService::class.java)
        service.putExtra(MusicPlayService.URL, musicItem!!.path)
        startService(service)

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

    companion object {
        const val ITEM: String = "item"
    }

}

@Composable
fun MusicPlayScreen(viewModel: MusicPlayViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AsyncImage(
            model = "https://p2.music.126.net/ryk8Gu64rOhlYn0pc2Q8Ww==/109951168090271827.jpg",
            contentDescription = "歌曲封面",
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.5f, fill = true),
        )
        ProgressContent(viewModel)
        PlayerControlView(viewModel)
    }

}

@Composable
fun ProgressContent(viewModel: MusicPlayViewModel) {

    var sliderPosition by remember { mutableStateOf(0f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Text(
            text = "00:00",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )
        Slider(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                Log.d(TAG, "ProgressContent: $it")
            }
        )
        Text(
            text = "00:00",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )
    }

}

@Composable
fun PlayerControlView(viewModel: MusicPlayViewModel) {
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
            IconButton(onClick = { /* 执行第一个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_loop_one),
                    contentDescription = "循环播放"
                )
            }
            IconButton(onClick = { /* 执行第一个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_left),
                    contentDescription = "上一首"
                )
            }
            IconButton(onClick = { /* 执行第二个IconButton的操作 */ }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_play),
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